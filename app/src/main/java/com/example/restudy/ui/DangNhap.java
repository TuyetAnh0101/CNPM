package com.example.restudy.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restudy.MainActivity;
import com.example.restudy.R;
import com.example.restudy.database.UserDatabaseHelper;  // đổi import sang đúng thư viện bạn gửi
import com.example.restudy.model.User;

public class DangNhap extends AppCompatActivity {

    private EditText email_EditText, password_EditText;
    private Button btLogin, btRegister;
    private TextView tvForgotPassword;
    private SharedPreferences sharedPreferences;
    private UserDatabaseHelper userDatabaseHelper;  // đổi sang UserDatabaseHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        email_EditText = findViewById(R.id.email_EditText);
        password_EditText = findViewById(R.id.password_EditText);
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userDatabaseHelper = new UserDatabaseHelper(this);  // khởi tạo lớp database helper

        checkLoginStatus();

        btLogin.setOnClickListener(v -> localLogin());

        btRegister.setOnClickListener(v -> {
            Intent i = new Intent(DangNhap.this, DangKy.class);
            startActivity(i);
        });

        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng đang được cập nhật", Toast.LENGTH_SHORT).show();
        });
    }

    private void checkLoginStatus() {
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            Intent i = new Intent(DangNhap.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void localLogin() {
        String username = email_EditText.getText().toString().trim();  // đổi tên biến cho hợp với username
        String password = password_EditText.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            email_EditText.setError("Vui lòng nhập username");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            password_EditText.setError("Vui lòng nhập mật khẩu");
            return;
        }

        // Dùng hàm loginUser trong UserDatabaseHelper
        boolean loginSuccess = userDatabaseHelper.loginUser(username, password);

        if (loginSuccess) {
            // Lấy đối tượng User sau khi đăng nhập đúng
            User user = userDatabaseHelper.getUser(username);
            if (user != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("userEmail", user.getEmail());
                editor.putString("userName", user.getName());
                editor.apply();

                Intent intent = new Intent(DangNhap.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Lỗi lấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Username hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
        }
    }
}
