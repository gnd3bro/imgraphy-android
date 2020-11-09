package com.teamig.imgraphy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.teamig.imgraphy.service.Imgraphy;

public class SplashActivity extends AppCompatActivity {

    private Imgraphy imgraphy;

    private Button splashInitButton;
    private Button splashAcceptButton;
    private ConstraintLayout splashPolicyContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initReferences();
        initEvents();
    }

    private void initReferences() {
        splashInitButton = (Button) findViewById(R.id.SplashInitButton);
        splashAcceptButton = (Button) findViewById(R.id.SplashAcceptButton);
        splashPolicyContainer = (ConstraintLayout) findViewById(R.id.SplashPolicyContainer) ;

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", null);

        if (userID != null) {
            exitSplash();
        }
    }

    private void initEvents() {
        splashInitButton.setOnClickListener(v -> {
            splashPolicyContainer.setVisibility(View.VISIBLE);
        });

        splashAcceptButton.setOnClickListener(v -> {
            Toast.makeText(this,"서비스 이용 약관에 동의하셨습니다.", Toast.LENGTH_SHORT).show();
            imgraphy = new Imgraphy();

            imgraphy.generateID(true).observe(this, result -> {
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit();

                editor.putString("userID", result.log);
                editor.apply();

                exitSplash();
            });
        });
    }

    private void exitSplash() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}