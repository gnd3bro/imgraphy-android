package com.teamig.imgraphy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.teamig.imgraphy.service.Imgraphy;
import com.teamig.imgraphy.ui.upload.UploadViewModel;

public class SplashActivity extends AppCompatActivity {

    private Imgraphy imgraphy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", null);

        if (userID == null) {
            onInit();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void onInit() {
        imgraphy = new Imgraphy();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("서비스 이용 약관");
        builder.setMessage("<서비스 이용 약관에 대한 내용>");

        builder.setPositiveButton("동의", (dialog, which) -> {
            Toast.makeText(this,"서비스 이용 약관에 동의하셨습니다.", Toast.LENGTH_LONG).show();
            imgraphy.generateID(true).observe(this, result -> {
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit();
                editor.putString("userID", result.log);
                editor.apply();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            });
        });

        builder.setNegativeButton("거절", (dialog, which) -> {
            Toast.makeText(this,"서비스 이용 약관에 거절하셨습니다.", Toast.LENGTH_LONG).show();
            closeApp();
        });

        builder.show();
    }

    private void closeApp() {
        moveTaskToBack(true);
        finishAndRemoveTask();
        System.exit(0);
    }
}