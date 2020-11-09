package com.teamig.imgraphy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.teamig.imgraphy.R.*;
import com.teamig.imgraphy.ui.account.AccountFragmentDirections;
import com.teamig.imgraphy.ui.graphy.GraphyFragmentDirections;
import com.teamig.imgraphy.ui.upload.UploadFragmentDirections;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", null);

        BottomNavigationView navView = (BottomNavigationView) findViewById(R.id.nav_view);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navView, navController);

        navView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == id.navigation_graphy) {
                navController.navigate(GraphyFragmentDirections.actionGlobalNavigationGraphy(userID, null));
            } else if (itemId == id.navigation_upload) {
                navController.navigate(UploadFragmentDirections.actionGlobalNavigationUpload(userID));
            } else if (itemId == id.navigation_account) {
                navController.navigate(AccountFragmentDirections.actionGlobalNavigationAccount(userID));
            }

            return false;
        });

        navController.navigate(GraphyFragmentDirections.actionGlobalNavigationGraphy(userID, null));
    }
}