package com.example.restudy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.restudy.Fragment.HomeFragment;
import com.example.restudy.Fragment.DangBanFragment;
import com.example.restudy.Fragment.CaNhanFragment;
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

        // Load HomeFragment lần đầu tiên khi activity được tạo
        loadFragment(new HomeFragment());

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
            // Nếu bạn không dùng backstack cho bottom nav, có thể gọi finish() luôn
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private NavigationBarView.OnItemSelectedListener getItemBottomListener() {
        return new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();

                if (id == R.id.mnhome) {
                    selectedFragment = new HomeFragment();
                } else if (id == R.id.mndangban) {
                    selectedFragment = new DangBanFragment();
                } else if (id == R.id.mncanhan) {
                    selectedFragment = new CaNhanFragment();
                } else {
                    return false;
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    return true;
                }

                return false;
            }
        };
    }
    // Load fragment mà KHÔNG thêm vào backstack
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .commit();
    }
}
