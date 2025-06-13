package com.example.restudy;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.restudy.Fragment.HomeFragment; // Menu admin
import com.example.restudy.UserFragment.PostFragment;
import com.example.restudy.UserFragment.UserFragment;
import com.example.restudy.UserFragment.UserHomeFragment;
import com.example.restudy.model.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class UserHomeActivity extends AppCompatActivity {

    BottomNavigationView bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        bottom = findViewById(R.id.menu); // ID phải đúng với layout

        // Nếu người dùng đã đăng nhập → hiện UserHomeFragment, ngược lại hiện menu admin
        if (SessionManager.isLoggedIn(this)) {
            loadFragment(new UserHomeFragment());
        } else {
            loadFragment(new HomeFragment()); // Menu admin
        }

        // Gán listener cho menu
        bottom.setOnItemSelectedListener(getItemBottomListener());
    }

    private NavigationBarView.OnItemSelectedListener getItemBottomListener() {
        return new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();

                if (id == R.id.userhome) {
                    if (SessionManager.isLoggedIn(UserHomeActivity.this)) {
                        selectedFragment = new UserHomeFragment();
                    } else {
                        selectedFragment = new HomeFragment(); // Hiện menu admin
                    }

                } else if (id == R.id.post) {
                    if (SessionManager.isLoggedIn(UserHomeActivity.this)) {
                        selectedFragment = new PostFragment();
                    } else {
                        Toast.makeText(UserHomeActivity.this, "Bạn cần đăng nhập để đăng bài", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                } else if (id == R.id.user_user) {
                    if (SessionManager.isLoggedIn(UserHomeActivity.this)) {
                        selectedFragment = new UserFragment();
                    } else {
                        selectedFragment = new HomeFragment();
                    }
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    return true;
                }

                return false;
            }
        };
    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
