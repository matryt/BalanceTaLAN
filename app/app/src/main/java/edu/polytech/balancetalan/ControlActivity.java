package edu.polytech.balancetalan;

import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class ControlActivity extends AppCompatActivity implements Notifiable, Menuable {
    private final String TAG = "BalanceTaLan " + getClass().getSimpleName();
    private int seekBarValue = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        int menuNumber = 1;

        Intent intent = getIntent();
        if(intent!=null){
            menuNumber = intent.getIntExtra("button_number",1);
            Log.d(TAG,"received menu#"+menuNumber);
        }

        Bundle args = new Bundle();
        args.putInt(getString(R.string.index), menuNumber);
        MenuFragment menu = new MenuFragment();
        menu.setArguments(args);
        Log.d(TAG,"send to MenuFragment menu#"+menuNumber);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.menu_fragment, menu);
        transaction.commit();
    }

    @Override
    public void onMenuChange(int index) {
        Fragment fragment = null;
        switch (index){
            case 0: fragment = new Screen1Fragment(); break;
            case 1: {
                fragment = new Screen2Fragment();
                //Bundle args = new Bundle();
                //args.putInt(getString(R.string.seekbarvalue), seekBarValue);
                //fragment.setArguments(args);
            }  break;
            case 2: fragment = new Screen3Fragment(); break;
            case 3: fragment = new MapFragment(); break;
            case 4: fragment = new Screen5Fragment(); break;
            case 5: fragment = new MapFragment(); break;
            default: fragment = new Screen1Fragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_main, fragment);
        transaction.addToBackStack(null); // to be able to browse with back button...
        transaction.commit();
    }

    @Override
    public void onClick(int numFragment) {
        Log.d(TAG, "Le fragment " + numFragment + "a été cliqué !");
    }

    @Override
    public void onDataChange(int numFragment, Object object) {
        Log.d(TAG, "Le fragment " + numFragment + " a eu un changement de données !");
        if (numFragment == 2) {
            String searchText = (String) object;
            Log.d(TAG, "Le contenu de la barre de recherche est : " + searchText);
        }
    }
}