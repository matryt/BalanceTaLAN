package edu.polytech.balancetalan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.File;

public class MapFragment extends Fragment {
    private final String TAG = "BalanceTaLan " + getClass().getSimpleName();
    private Notifiable notifiable;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    private MapView map;
    private Marker currentLocationMarker;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    public MapFragment() {
        Log.d(TAG, "mapFragment created");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setOsmdroidBasePath(new File(requireContext().getCacheDir(), "osmdroid"));
        Configuration.getInstance().setOsmdroidTileCache(new File(requireContext().getCacheDir(), "osmdroid/tiles"));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        startLocationUpdates();
                    } else {
                        Log.d(TAG, "Permission not granted");
                        explain();
                    }
                });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult.getLastLocation() == null) return;

                Location location = locationResult.getLastLocation();
                GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

                if (currentLocationMarker == null) {
                    currentLocationMarker = addMarker(
                            org.osmdroid.library.R.drawable.person,
                            geoPoint,
                            "Moi",
                            "Ma position actuelle",
                            org.osmdroid.library.R.drawable.person
                    );
                    map.getOverlays().add(currentLocationMarker);
                } else {
                    currentLocationMarker.setPosition(geoPoint);
                }

                map.getController().animateTo(geoPoint);
                map.invalidate();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        map = view.findViewById(R.id.map);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        requestPermissionsIfNecessary();

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(18.5);
        GeoPoint startPoint = new GeoPoint(43.6156, 7.0718);
        mapController.setCenter(startPoint);

        map.getOverlays().add(addMarker(
                org.osmdroid.library.R.drawable.marker_default,
                new GeoPoint(43.550953, 7.017807),
                "fred",
                "my home",
                R.drawable.home
        ));


    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(2000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void requestPermissionsIfNecessary() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            startLocationUpdates();
        }
    }

    private Marker addMarker(int icon, GeoPoint location, String title, String description, int imageResource) {
        Marker marker = new Marker(map);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(ResourcesCompat.getDrawable(getResources(), icon, null));
        marker.setPosition(location);
        marker.setTitle(title);
        marker.setSubDescription(description);
        marker.setImage(ResourcesCompat.getDrawable(getResources(), imageResource, null));
        marker.setPanToView(false);
        marker.setDraggable(false);
        return marker;
    }

    public void explain() {
        Log.d(TAG, "Explain");
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Autorisation de Géolocalisation")
                    .setMessage("Nous avons besoin de votre position pour vous offrir une meilleure expérience. Veuillez accepter l'utilisation de la géolocalisation.")
                    .setPositiveButton("Accepter", (dialog, which) -> {
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                        dialog.dismiss();
                    })
                    .setNegativeButton("Refuser", (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Permission refusée définitivement")
                    .setMessage("Vous avez refusé la permission de géolocalisation. Veuillez l'activer manuellement dans les paramètres de l'application.")
                    .setPositiveButton("Ouvrir les paramètres", (dialog, which) -> {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", requireContext().getPackageName(), null));
                        startActivity(intent);
                    })
                    .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
            Log.d(TAG, "Class " + requireActivity().getClass().getSimpleName() + " implements Notifiable.");
        } else {
            throw new AssertionError("Classe " + requireActivity().getClass().getName() + " ne met pas en œuvre Notifiable.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
        stopLocationUpdates();
    }
}
