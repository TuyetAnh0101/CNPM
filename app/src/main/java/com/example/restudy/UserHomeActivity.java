package com.example.restudy;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.restudy.UserFragment.UserHomeFragment;
import com.example.restudy.UserFragment.UserFragment;
import com.example.restudy.UserFragment.PostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class UserHomeActivity extends AppCompatActivity {

    BottomNavigationView bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        bottom = findViewById(R.id.menu); // phải đúng ID trong layout

        // Load fragment mặc định
        loadFragment(new UserHomeFragment());

        // Gán listener cho bottom menu
        bottom.setOnItemSelectedListener(getItemBottomListener());
    }

    // Phương thức xử lý lựa chọn menu bằng if-else
    private NavigationBarView.OnItemSelectedListener getItemBottomListener() {
        return new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();

                if (id == R.id.userhome) {
                    selectedFragment = new UserHomeFragment();
                } else if (id == R.id.post) {
                    selectedFragment = new PostFragment();
                } else if (id == R.id.user_user) {
                    selectedFragment = new UserFragment();
                } else {
                    return false;
                }
                loadFragment(selectedFragment);
                return true;
            }
        };
    }

    // Hàm thay thế fragment hiển thị
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment) // ID phải đúng với FrameLayout trong XML
                .commit();
    }
}
