package com.teamig.imgraphy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavArgs;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamig.imgraphy.ui.account.AccountFragmentDirections;
import com.teamig.imgraphy.ui.graphy.GraphyFragmentArgs;
import com.teamig.imgraphy.ui.graphy.GraphyFragmentDirections;
import com.teamig.imgraphy.ui.recommend.RecommendFragment;
import com.teamig.imgraphy.ui.recommend.RecommendFragmentDirections;
import com.teamig.imgraphy.ui.upload.UploadFragmentDirections;

public class MainActivity extends AppCompatActivity {

    private String userID;

    private BottomNavigationView bottomNavigationView;
    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userID = getIntent().getStringExtra("userID");

        if (savedInstanceState == null) {
            setUpBottomNavigation();
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setUpBottomNavigation();
    }

    @Override
    public boolean onNavigateUp() {
        return navController.navigateUp();
    }

    private void setUpBottomNavigation() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_graphy) {
                navController.navigate(GraphyFragmentDirections.actionGlobalNavigationGraphy(userID, null));
            } else if (itemId == R.id.navigation_recommend) {
                navController.navigate(RecommendFragmentDirections.actionGlobalNavigationRecommend(userID));
            } else if (itemId == R.id.navigation_upload) {
                navController.navigate(UploadFragmentDirections.actionGlobalNavigationUpload(userID));
            } else if (itemId == R.id.navigation_account) {
                navController.navigate(AccountFragmentDirections.actionGlobalNavigationAccount(userID));
            }

            return true;
        });

        navController.navigate(GraphyFragmentDirections.actionGlobalNavigationGraphy(userID, null));
    }
}