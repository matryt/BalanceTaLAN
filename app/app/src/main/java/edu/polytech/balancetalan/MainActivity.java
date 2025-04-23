package edu.polytech.balancetalan;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements Notifiable {

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

        findViewById(R.id.statistics).setOnClickListener(clic -> {
            intent.putExtra("button_number", 1);
            startActivity(intent);
        });

        findViewById(R.id.searchpb).setOnClickListener(clic -> {
            intent.putExtra("button_number", 2);
            startActivity(intent);
        });
    }

    @Override
    public void onClick(int numFragment) {

    }

    @Override
    public void onDataChange(int numFragment, Object object) {

    }
}