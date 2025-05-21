package edu.polytech.balancetalan;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Screen1Fragment extends Fragment implements AdapterView.OnItemSelectedListener, PostExecuteActivity{

    private final static int NUM_FRAGMENT = 1;
    private static final String[] ticketTypes = {
            "Autre",
            "Réseau",
            "Confort",
            "Équipement",
            "Triche",
            "Installation"};
    int[] inputIds = {
            R.id.title_input_edit_text,
            R.id.description_input_edit_text,
            R.id.lastname_input_edit_text,
            R.id.firstname_input_edit_text,
    };

    TextInputEditText titleInput, descriptionInput, lastNameInput, firstNameInput, zoneInput, numberInput;

    private Map<TextInputEditText, Boolean> textInputMap = new HashMap<>();
    private Notifiable notifiable;
    private final String TAG = "BalanceTaLan " + getClass().getSimpleName();

    private ActivityResultLauncher<Intent> takePictureLauncher;

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private HttpAsyncPost asyncPost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        asyncPost = new HttpAsyncPost("https://api-balancetalan.mathieucuvelier.fr/tickets");
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

    private List<Area> getAreasFromApi() {
        List<Area> areas = new ArrayList<>();
        areas.add(new Area('A', List.of(new Place(1), new Place(2), new Place(3))));
        areas.add(new Area('B', List.of(new Place(4), new Place(5))));
        return areas;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen1, container, false);

        titleInput = view.findViewById(R.id.title_input_edit_text);
        descriptionInput = view.findViewById(R.id.description_input_edit_text);
        lastNameInput = view.findViewById(R.id.lastname_input_edit_text);
        firstNameInput = view.findViewById(R.id.firstname_input_edit_text);

        List<Area>areaList = getAreasFromApi();

        List<String> areaLetters = new ArrayList<>();
        for (Area area : areaList) {
            areaLetters.add(String.valueOf(area.getLetter()));
        }

        Spinner type_spinner = view.findViewById(R.id.type_spinner);
        Spinner area_spinner = view.findViewById(R.id.area_spinner);
        Spinner place_spinner = view.findViewById(R.id.table_spinner);
        typeSetupSpinner(view, type_spinner);
        areaSetupSpinner(view, area_spinner, areaList, place_spinner);





        Button validateButton = view.findViewById(R.id.send_ticket_button);
        setupTextValidationButton(view, validateButton);
        setupTicketSending(view, validateButton, type_spinner);

        // Lorsque le bouton image est cliqué
        view.findViewById(R.id.buttonImage).setOnClickListener(v -> {
            // Vérifier si la permission caméra est accordée
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted");
                takePicture(); // Prendre la photo si la permission est accordée
            } else {
                Log.d(TAG, "Launching permission request");
                requestPermissionLauncher.launch(Manifest.permission.CAMERA); // Demander la permission si non accordée
            }
        });

        return view;
    }

    void setupTicketSending(View view, Button validateButton, Spinner spinner){


        validateButton.setOnClickListener(v -> {
            SentTicket ticket = getInputFieldsContent(view, spinner);
            Log.d(TAG, "Button clicked");
            Log.d(TAG, "Input fields content: " + ticket);
            asyncPost.sendData(ticket, this, new ProgressDialog(requireContext()));
        });
    }

    private String getInputText(TextInputEditText input) {
       return Objects.requireNonNull(input.getText()).toString();
    }

    private SentTicket getInputFieldsContent(View view, Spinner spinner) {
        String title = getInputText(titleInput);
        String description = getInputText(descriptionInput);
        String firstName = getInputText(firstNameInput);
        String lastName = getInputText(lastNameInput);
        String zone = getInputText(zoneInput);
        String number = getInputText(numberInput);

        ImageView imageView = getView().findViewById(R.id.imageViewPhoto);
        Bitmap imageBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        return new SentTicket(
                title,
                firstName,
                lastName,
                number,
                ticketTypes[spinner.getSelectedItemPosition()],
                List.of(imageBitmap),
                zone,
                requireContext(),
                description
        );
    }

    private void typeSetupSpinner(View view, Spinner spinner){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, ticketTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void areaSetupSpinner(View view, Spinner areaSpinner, List<Area> areaList, Spinner placeSpinner) {
        List<String> areaLetters = new ArrayList<>();
        for (Area area : areaList) {
            areaLetters.add(String.valueOf(area.getLetter()));
        }
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, areaLetters);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areaAdapter);

        // Update place spinner when area changes
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                placeSetupSpinner(view, placeSpinner, areaList.get(position).getPlaces());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Initialize place spinner for the first area
        if (!areaList.isEmpty()) {
            placeSetupSpinner(view, placeSpinner, areaList.get(0).getPlaces());
        }
    }

    private void placeSetupSpinner(View view, Spinner placeSpinner, List<Place> places) {
        List<String> placeNumbers = new ArrayList<>();
        for (Place place : places) {
            placeNumbers.add(String.valueOf(place.getNumber()));
        }
        ArrayAdapter<String> placeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, placeNumbers);
        placeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placeSpinner.setAdapter(placeAdapter);
    }
    private void setupTextValidationButton(View view, Button validateButton){
        validateButton.setEnabled(false);

        for (int id : inputIds) {
            TextInputEditText editText = view.findViewById(id);
            textInputMap.put(editText, false);
        }
        // Initially disable the button

        for (Map.Entry<TextInputEditText, Boolean> entry : textInputMap.entrySet()) {
            TextInputEditText editText = entry.getKey();
            addInputChecker(editText, validateButton);
        }
    }

    private void addInputChecker(TextInputEditText editText, Button button) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputMap.put(editText, s.length() > 0);
                button.setEnabled(areAllInputsValid());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });
    }

    private boolean areAllInputsValid() {
        for (Boolean isValid : textInputMap.values()) {
            if (!isValid) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
            Log.d(TAG, "La classe " + notifiable.getClass().getSimpleName() + " implémente Notifiable.");
        }
        else throw new AssertionError("La classe " + notifiable.getClass().getSimpleName() + " n'implémente pas Notifiable.");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
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

    @Override
    public void onPostExecute(List itemList) {
    }

    @Override
    public void runOnUiThread(Runnable runable) {
       requireActivity().runOnUiThread(runable);
    }
}