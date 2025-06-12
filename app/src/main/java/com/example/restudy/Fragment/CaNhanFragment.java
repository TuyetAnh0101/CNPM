package com.example.restudy.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.restudy.R;
import com.example.restudy.model.User;
import com.example.restudy.ui.DangNhap;

public class CaNhanFragment extends Fragment {

    private Button btnLogin, btnLogout;
    private LinearLayout layoutProfile;
    private TextView tvUserName, tvUserEmail, tvUserSex, tvUserPhone, tvUserRole, tvUserActive;

    private SharedPreferences sharedPreferences;

    public CaNhanFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ca_nhan, container, false);

        btnLogin = view.findViewById(R.id.btnLogin);
        btnLogout = view.findViewById(R.id.btnLogout);

        layoutProfile = view.findViewById(R.id.layoutProfile);

        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserSex = view.findViewById(R.id.tvUserSex);
        tvUserPhone = view.findViewById(R.id.tvUserPhone);
        tvUserRole = view.findViewById(R.id.tvUserRole);
        tvUserActive = view.findViewById(R.id.tvUserActive);

        sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);

        checkLoginStatus();

        btnLogin.setOnClickListener(v -> {
            // Chuyển sang màn hình đăng nhập
            Intent intent = new Intent(getActivity(), DangNhap.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> showLogoutConfirmationDialog());

        return view;
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Có", (dialog, which) -> logoutUser())
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        checkLoginStatus(); // Cập nhật lại giao diện sau khi đăng xuất
    }

    private void checkLoginStatus() {
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Nếu đã đăng nhập, hiện thông tin người dùng
            User user = loadUserFromPrefs();

            layoutProfile.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);

            tvUserName.setText(user.getName());
            tvUserEmail.setText("Email: " + user.getEmail());
            tvUserSex.setText("Giới tính: " + user.getSex());
            tvUserPhone.setText("Số điện thoại: " + user.getPhone());
            tvUserRole.setText("Vai trò: " + user.getRole());
            tvUserActive.setText("Tình trạng: " + (user.isActive() ? "Đang hoạt động" : "Không hoạt động"));

        } else {
            // Chưa đăng nhập, ẩn thông tin cá nhân, chỉ hiện nút đăng nhập
            layoutProfile.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    private User loadUserFromPrefs() {
        User user = new User();

        user.setId(sharedPreferences.getInt("userId", 0));
        user.setName(sharedPreferences.getString("userName", "Người dùng"));
        user.setEmail(sharedPreferences.getString("userEmail", "Chưa cập nhật"));
        user.setSex(sharedPreferences.getString("userSex", "Chưa cập nhật"));
        user.setPhone(sharedPreferences.getString("userPhone", "Chưa cập nhật"));
        user.setRole(sharedPreferences.getString("userRole", "Người dùng"));
        user.setActive(sharedPreferences.getBoolean("userActive", true));

        return user;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLoginStatus();
    }
}
