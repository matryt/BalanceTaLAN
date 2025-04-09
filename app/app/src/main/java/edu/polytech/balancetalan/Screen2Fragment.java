package edu.polytech.balancetalan;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class Screen2Fragment extends Fragment {

    private final String TAG = "BalanceTaLan " + getClass().getSimpleName();
    private final static int NUM_FRAGMENT = 2;
    private Notifiable notifiable;
    private int seekBarValue;

    public Screen2Fragment() {
        Log.d(TAG,"screenFragment type 2 created");
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
        View view =  inflater.inflate(R.layout.fragment_screen2, container, false);
        ((TextView)view.findViewById(R.id.speed)).setText(""+seekBarValue);
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        Button button = view.findViewById(R.id.go);
        seekBar.setProgress(seekBarValue);
        notifiable.onDataChange(NUM_FRAGMENT, seekBarValue);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                seekBarValue = value;
                notifiable.onDataChange(NUM_FRAGMENT, seekBarValue);
                Log.d(TAG, "onProgressChanged = " + seekBarValue);
                ((TextView)view.findViewById(R.id.speed)).setText(""+seekBarValue);
               //TODO: do something
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        button.setOnClickListener(clic -> notifiable.onClick(NUM_FRAGMENT));


        return view;
    }

}