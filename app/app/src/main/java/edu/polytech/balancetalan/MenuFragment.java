package edu.polytech.balancetalan;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {
    private final String TAG = "frallo " + getClass().getSimpleName();
    private Menuable menuable;
    private int currentActivatedIndex = 0;

    public MenuFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Menuable) {
            menuable = (Menuable) requireActivity();
            Log.d(TAG, "Class " + requireActivity().getClass().getSimpleName() + " implements Notifiable.");
        } else {
            throw new AssertionError("Classe " + requireActivity().getClass().getName() + " ne met pas en œuvre Notifiable.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        //get current activated index menu
        if (getArguments() != null) {
            currentActivatedIndex = getArguments().getInt(getString(R.string.index), 1) - 1;  //convert menu number to index
        }

        //notify activity the menu is selected
        menuable.onMenuChange(currentActivatedIndex);
        return view;
    }
}