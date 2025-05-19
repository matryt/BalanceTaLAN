package edu.polytech.balancetalan;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Screen1Fragment extends Fragment {
    private final static int NUM_FRAGMENT = 1;
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
        view.findViewById(R.id.screen1_button).setOnClickListener(
                click -> {
                    notifiable.onClick(NUM_FRAGMENT);
                }
        );
        return view;
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
}