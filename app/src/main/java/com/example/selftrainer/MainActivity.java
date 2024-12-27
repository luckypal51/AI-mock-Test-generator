package com.example.selftrainer;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.selftrainer.Fragments.HomeFragment;
import com.example.selftrainer.Fragments.ProfileFragment;
import com.example.selftrainer.Fragments.TestFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    ProfileFragment profileFragment;
    HomeFragment homeFragment;
    TestFragment testFragment;
    ImageView searchbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        frameLayout = findViewById(R.id.fram_layout);

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        testFragment = new TestFragment();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.home_nav){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram_layout,homeFragment).commit();
                }
                if (item.getItemId()==R.id.profile_nav){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram_layout,profileFragment).commit();
                }
                if (item.getItemId()==R.id.cart_nav){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram_layout,testFragment).commit();
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.home_nav);
    }
}