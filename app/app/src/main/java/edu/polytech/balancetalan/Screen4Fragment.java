package edu.polytech.balancetalan;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Screen4Fragment extends Fragment {
    private final static int NUM_FRAGMENT = 4;
    private Notifiable notifiable;

    private final String TAG = "BalanceTaLan " + getClass().getSimpleName();

    public Screen4Fragment() {
        Log.d(TAG,"screenFragment type 4 created");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
            Log.d(TAG, "Class " + requireActivity().getClass().getSimpleName() + " implements Notifiable.");
        } else {
            throw new AssertionError("Classe " + requireActivity().getClass().getName() + " ne met pas en œuvre Notifiable.");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_screen4, container, false);


        return view;
    }



}