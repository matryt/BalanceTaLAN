package edu.polytech.balancetalan;

import androidx.fragment.app.FragmentTransaction;
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
    }

    @Override
    public void onMenuChange(int index) {
        Fragment fragment;
        switch (index) {
            case 1:
                fragment = new Screen1Fragment();
                break;
            case 2:
                fragment = new Screen2Fragment();
                break;
            case 3:
                fragment = new Screen3Fragment();
                break;
            case 4:
                fragment = new Screen4Fragment();
                break;
            case 5:
                fragment = new Screen5Fragment();
                break;
            case 6:
                fragment = new Screen6Fragment();
                break;
            default:
                throw new AssertionError("Fragment " + index + "inconnu !");
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_screen1, fragment);
        transaction.commit();
    }

    @Override
    public void onClick(int numFragment) {
        Log.d(TAG, "Le fragment " + numFragment + "a été cliqué !");
    }

    @Override
    public void onDataChange(int numFragment, Object object) {
        Log.d(TAG, "Le fragment " + numFragment + "a eu un changement de données !");
        if (numFragment == 2) {
            seekBarValue = (int) object;
        }
    }
}