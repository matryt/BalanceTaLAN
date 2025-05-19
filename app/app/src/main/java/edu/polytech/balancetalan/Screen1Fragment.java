package edu.polytech.balancetalan;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Screen1Fragment extends Fragment implements AdapterView.OnItemSelectedListener{
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
            R.id.zone_input_edit_text,
            R.id.number_input_edit_text
    };
    private Map<TextInputEditText, Boolean> textInputMap = new HashMap<>();
    private Notifiable notifiable;
    private final String TAG = "BalanceTaLan " + getClass().getSimpleName();

   public Screen1Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen1, container, false);

        Spinner spinner = view.findViewById(R.id.spinner);
        setupSpinner(view, spinner);

        Button validateButton = view.findViewById(R.id.send_ticket_button);
        setupTextValidationButton(view, validateButton);
        setupTicketSending(view, validateButton, spinner);

        return view;
    }

    void setupTicketSending(View view, Button validateButton, Spinner spinner){


        validateButton.setOnClickListener(v -> {
            List<String> inputFieldsContent = getInputFieldsContent(view);
            inputFieldsContent.add(ticketTypes[spinner.getSelectedItemPosition()]);
            Log.d(TAG, "Button clicked");
            Log.d(TAG, "Input fields content: " + inputFieldsContent);
            if (notifiable != null) {

            } else {
                Log.e(TAG, "Notifiable is null");
            }
        });
    }

    private List<String> getInputFieldsContent(View view) {
        List<String> inputFieldsContent = new ArrayList<>();

        for (int id : inputIds) {
            TextInputEditText editText = view.findViewById(id);
            inputFieldsContent.add(editText.getText().toString());
        }
        return inputFieldsContent;
    }

    private void setupSpinner(View view, Spinner spinner){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, ticketTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
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
}