package com.example.habit_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * MainActivity is the start of the programme
 * The programme adopts the single activity, multiple fragment practice
 * That's why MainActivity is the only activity the programme has
 */

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    NavController navController;

    /**
     * Creates the activity, and assign the navigation to the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(this,R.id.mainPageFragment);
        NavigationUI.setupActionBarWithNavController(this, navController);

    }

    /**
     * Create the app bar for the programme, gives each fragment a chance to redesign
     * @param menu the app bar
     * @return a boolean for telling if the app bar created successfully
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Inflate the back button, applies to all fragment
     * @return a boolean for telling if the back button created successfully
     */
    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }

}