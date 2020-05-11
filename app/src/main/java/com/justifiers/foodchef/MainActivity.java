package com.justifiers.foodchef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.justifiers.foodchef.BottomNavigationView.DownloadsFragment;
import com.justifiers.foodchef.BottomNavigationView.SearchFragment;
import com.justifiers.foodchef.BottomNavigationView.ShoppingListFragment;
import com.justifiers.foodchef.LoginAndSignUp.LoginFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Declare Variables here
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("SettingsActivity", Activity.MODE_PRIVATE);
        String language = preferences.getString("Language", "");
        setLocale(language);
        setContentView(R.layout.activity_main);

        // initializing the variables
        bottomNavigation = findViewById(R.id.bottom_navigation_view);
        bottomNavigation.setOnNavigationItemSelectedListener(navListen);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SearchFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    // listens to the selected navigation item and sets the fragment for it
    private BottomNavigationView.OnNavigationItemSelectedListener navListen = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem navItem) {
            Fragment selectedFrag = null;

            switch(navItem.getItemId()) {
                case R.id.nav_search:
                    selectedFrag = new SearchFragment();
                    break;
                case R.id.nav_downloads:
                    selectedFrag = new DownloadsFragment();
                    break;
                case R.id.nav_shopping_list:
                    selectedFrag = new ShoppingListFragment();
                    break;
                case R.id.nav_profile:
                    // make settings visible only for profile fragment
                    selectedFrag = new LoginFragment();
                    break;
            }
            if(selectedFrag != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFrag).commit();
            }
            return true;
        }
    };

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


    private void setLocale(String language){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>= 21){
            config.setLocale(new Locale(language));
        } else {
            config.locale = new Locale(language);
        }
        resources.updateConfiguration(config, dm);
        // save the settings
        SharedPreferences.Editor lang_editor = getSharedPreferences("SettingsActivity", MODE_PRIVATE).edit();
        lang_editor.putString("Language", language);
        lang_editor.apply();
    }
}
