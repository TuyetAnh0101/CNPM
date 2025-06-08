package com.example.restudy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.restudy.Fragment.HomeFragment;
import com.example.restudy.UserFragment.UserHomeFragment;
import com.example.restudy.Fragment.CaNhanFragment;
import com.example.restudy.model.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView mnBottom;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tạm set đăng nhập và vai trò admin để test
        SessionManager.login(true);

        mnBottom = findViewById(R.id.bottomNav);

        // Load fragment mặc định theo trạng thái đăng nhập và role
        if (!SessionManager.isLoggedIn()) {
            loadFragment(new UserHomeFragment());  // Chưa đăng nhập thì hiển thị giao diện user
        } else {
            if (SessionManager.isAdmin()) {
                loadFragment(new HomeFragment());  // Admin thì hiển thị HomeFragment
            } else {
                loadFragment(new UserHomeFragment());  // User thường thì UserHomeFragment
            }
        }
        // Thiết lập ActionBar với nút Back
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Main");
            actionBar.setDisplayHomeAsUpEnabled(true); // Hiển thị nút Back
        }

        // Thiết lập listener cho BottomNavigationView
        mnBottom.setOnItemSelectedListener(getItemBottomListener());
    }

    // Xử lý sự kiện khi nhấn nút Back trên ActionBar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private NavigationBarView.OnItemSelectedListener getItemBottomListener() {
        return item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.mnhome) {
                if (!SessionManager.isLoggedIn()) {
                    selectedFragment = new UserHomeFragment();
                } else {
                    if (SessionManager.isAdmin()) {
                        selectedFragment = new HomeFragment();
                    } else {
                        selectedFragment = new UserHomeFragment();
                    }
                }
            } else if (item.getItemId() == R.id.mncanhan) {
                selectedFragment = new CaNhanFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        };
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .commit();
    }
}
