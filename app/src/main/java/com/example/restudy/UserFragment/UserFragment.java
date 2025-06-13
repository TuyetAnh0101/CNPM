package com.example.restudy.UserFragment;

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
import android.widget.TextView;

import com.example.restudy.MainActivity;
import com.example.restudy.R;
import com.example.restudy.model.SessionManager;
import com.example.restudy.model.User;
import com.example.restudy.ui.DangNhap;
import com.google.android.material.card.MaterialCardView;

public class UserFragment extends Fragment {

    private MaterialCardView layoutProfile;
    private Button btnLogin, btnLogout;
    private TextView tvUserName, tvUserEmail, tvUserSex, tvUserPhone, tvUserRole, tvUserActive;

    private SharedPreferences sharedPreferences;

    public UserFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initViews(view);
        setupListeners();
        loadUserState();
        return view;
    }

    private void initViews(View view) {
        layoutProfile = view.findViewById(R.id.layoutProfile);

        btnLogin = view.findViewById(R.id.btnLogin);
        btnLogout = view.findViewById(R.id.btnLogout);

        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserSex = view.findViewById(R.id.tvUserSex);
        tvUserPhone = view.findViewById(R.id.tvUserPhone);
        tvUserRole = view.findViewById(R.id.tvUserRole);
        tvUserActive = view.findViewById(R.id.tvUserActive);

        sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), DangNhap.class));
        });

        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Xác nhận đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Có", (dialog, which) -> logoutUser())
                    .setNegativeButton("Không", null)
                    .show();
        });
    }

    private void loadUserState() {
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            User user = getUserFromPrefs();

            layoutProfile.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);

            tvUserName.setText(user.getName());
            tvUserEmail.setText("Email: " + user.getEmail());
            tvUserSex.setText("Giới tính: " + user.getSex());
            tvUserPhone.setText("SĐT: " + user.getPhone());
            tvUserRole.setText("Vai trò: " + user.getRole());
            tvUserActive.setText("Trạng thái: " + (user.isActive() ? "Đang hoạt động" : "Bị khóa"));
        } else {
            layoutProfile.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    private void logoutUser() {
        // Xóa trạng thái đăng nhập
        SessionManager.logout(requireContext());

        // Xóa toàn bộ thông tin người dùng
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Chuyển về MainActivity và xóa ngăn xếp back stack
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private User getUserFromPrefs() {
        User user = new User();
        user.setId(sharedPreferences.getInt("userId", 0));
        user.setName(sharedPreferences.getString("userName", "Người dùng"));
        user.setEmail(sharedPreferences.getString("userEmail", "Chưa có"));
        user.setSex(sharedPreferences.getString("userSex", "Chưa cập nhật"));
        user.setPhone(sharedPreferences.getString("userPhone", "Chưa cập nhật"));
        user.setRole(sharedPreferences.getString("userRole", "Người dùng"));
        user.setActive(sharedPreferences.getBoolean("userActive", true));
        return user;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserState(); // Reload user info if returned from login
    }
}
