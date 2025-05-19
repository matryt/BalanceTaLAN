package edu.polytech.balancetalan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.provider.Settings;

public class Screen1Fragment extends Fragment {
    private final static int NUM_FRAGMENT = 1;
    private Notifiable notifiable;
    private final String TAG = "BalanceTaLan " + getClass().getSimpleName();

    // Déclaration du ActivityResultLauncher pour la prise de photo
    private ActivityResultLauncher<Intent> takePictureLauncher;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    public Screen1Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Fragment created");
        super.onCreate(savedInstanceState);

        // Demander la permission caméra
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // Lancer la prise de photo si la permission est accordée
                takePicture();
            } else {
                Log.d(TAG, "No permission granted");
                explain();
            }
        });

        // Initialisation de l'ActivityResultLauncher pour prendre une photo
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                // Récupérer l'image et l'afficher
                Bitmap photo = (Bitmap) result.getData().getExtras().get("data");

                // Trouver le ImageView et le bouton
                ImageView imageViewPhoto = getView().findViewById(R.id.imageViewPhoto);
                imageViewPhoto.setImageBitmap(photo); // Afficher la photo

                // Rendre le bouton invisible
                getView().findViewById(R.id.buttonImage).setVisibility(View.GONE);

                // Rendre l'ImageView visible
                imageViewPhoto.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen1, container, false);

        // Lorsque le bouton image est cliqué
        rootView.findViewById(R.id.buttonImage).setOnClickListener(view -> {
            // Vérifier si la permission caméra est accordée
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted");
                takePicture(); // Prendre la photo si la permission est accordée
            } else {
                Log.d(TAG, "Launching permission request");
                requestPermissionLauncher.launch(Manifest.permission.CAMERA); // Demander la permission si non accordée
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
            Log.d(TAG, "La classe " + notifiable.getClass().getSimpleName() + " implémente Notifiable.");
        } else {
            throw new AssertionError("La classe " + notifiable.getClass().getSimpleName() + " n'implémente pas Notifiable.");
        }
    }

    private void explain() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission bloquée")
                    .setMessage("La permission caméra a été \"définitivement\" refusée. Vous devez l'activer à nouveau \"manuellement\" depuis les paramètres.")
                    .setPositiveButton("Ouvrir les paramètres", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    })
                    .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission CAMERA refusée")
                    .setMessage("Impossible d'utiliser la caméra sans permission. Redemander l'autorisation ?")
                    .setPositiveButton("Redemander", (dialog, which) -> requestPermissionLauncher.launch(Manifest.permission.CAMERA))
                    .setNegativeButton("NON !", null)
                    .show();
        }
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureLauncher.launch(intent);
    }
}
