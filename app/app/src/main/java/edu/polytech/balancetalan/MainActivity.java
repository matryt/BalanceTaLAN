package edu.polytech.balancetalan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements Notifiable {
    private static final int REQUEST_NOTIFICATION_PERMISSION = 101;
    private static final String TAG = "balance " + HttpAsyncGet.class.getSimpleName();    //Pour affichage en cas d'erreur
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(getApplicationContext(), ControlActivity.class);

        // Animation pour l'image
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.mainlogo_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();

        findViewById(R.id.open_ticket_button).setOnClickListener(clic -> {
            intent.putExtra("button_number", 1);
            startActivity(intent);
        });

        findViewById(R.id.map_button).setOnClickListener(clic -> {
            intent.putExtra("button_number", 4);
            startActivity(intent);
        });

        findViewById(R.id.search_ticket_button).setOnClickListener(clic -> {
            intent.putExtra("button_number", 2);
            startActivity(intent);
        });

        //check permissions to receive notifications
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
                || Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_NOTIFICATION_PERMISSION);
        }

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.d(TAG,"no token received ");
            }
            else {
                Log.d(TAG,"token = "+task.getResult());
            }
        });
    }

    // callback after user action: authorization for notifications yes/no
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Log.d(TAG, "Permission POST_NOTIFICATIONS accepted");
            else {
                Log.e(TAG, "Permission POST_NOTIFICATIONS refused");
            }
    }

    @Override
    public void onClick(int numFragment) {

    }

    @Override
    public void onDataChange(int numFragment, Object object) {

    }
}