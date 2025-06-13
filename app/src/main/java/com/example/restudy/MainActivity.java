package com.example.restudy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.restudy.Fragment.CaNhanFragment;
import com.example.restudy.Fragment.HomeFragment;
import com.example.restudy.UserFragment.UserHomeFragment;
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

        mnBottom = findViewById(R.id.bottomNav);

        // Luôn load UserHomeFragment nếu chưa đăng nhập
        Fragment defaultFragment;
        if (SessionManager.isLoggedIn(this)) {
            if (SessionManager.isAdmin(this)) {
                defaultFragment = new HomeFragment(); // Admin
            } else {
                defaultFragment = new UserHomeFragment(); // User đã đăng nhập
            }
        } else {
            defaultFragment = new UserHomeFragment(); // Người dùng chưa đăng nhập
        }

        loadFragment(defaultFragment);

        // Thiết lập ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Trang chính");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Sự kiện chọn menu dưới
        mnBottom.setOnItemSelectedListener(getItemBottomListener());
    }

    // Xử lý nút Back trên ActionBar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Trở lại màn hình trước
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Listener cho menu dưới
    private NavigationBarView.OnItemSelectedListener getItemBottomListener() {
        return item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();

            if (id == R.id.mnhome) {
                if (SessionManager.isLoggedIn(this) && SessionManager.isAdmin(this)) {
                    selectedFragment = new HomeFragment();
                } else {
                    selectedFragment = new UserHomeFragment(); // cả user và chưa login
                }
            } else if (id == R.id.mncanhan) {
                selectedFragment = new CaNhanFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }

            return false;
        };
    }

    // Load fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .commit();
    }
}
