package com.example.restudy.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restudy.R;
import com.example.restudy.database.UserDatabaseHelper;
import com.example.restudy.model.User;

public class DangKy extends AppCompatActivity {

    private EditText edUserNameRe, password_EditText, confirmPassword_EditText, email_EditText, edPhone;
    private RadioGroup rgSex;
    private RadioButton rbMale, rbFemale;
    private Button btRegister;
    private ImageButton imbBack;

    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky); // Đảm bảo đúng tên file layout XML

        dbHelper = new UserDatabaseHelper(this);

        // Ánh xạ view theo id trong XML
        imbBack = findViewById(R.id.imbBack);
        edUserNameRe = findViewById(R.id.edUserNameRe);
        password_EditText = findViewById(R.id.password_EditText);
        confirmPassword_EditText = findViewById(R.id.confirmPassword_EditText);
        email_EditText = findViewById(R.id.email_EditText);
        edPhone = findViewById(R.id.edPhone);
        rgSex = findViewById(R.id.rgSex);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btRegister = findViewById(R.id.btRegister);

        imbBack.setOnClickListener(v -> finish()); // Nút back đóng activity

        btRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = edUserNameRe.getText().toString().trim();
        String password = password_EditText.getText().toString();
        String confirmPassword = confirmPassword_EditText.getText().toString();
        String email = email_EditText.getText().toString().trim();
        String phone = edPhone.getText().toString().trim();

        // Lấy giới tính
        String sex = "Nam";
        int selectedSexId = rgSex.getCheckedRadioButtonId();
        if (selectedSexId == R.id.rbFemale) {
            sex = "Nữ";
        }

        // Kiểm tra dữ liệu
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.isUserExists(username)) {
            Toast.makeText(this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo user mới, role và active mặc định (nếu có)
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        user.setSex(sex);
        user.setRole("user");   // Mặc định role "user"
        user.setActive(true);   // Mặc định kích hoạt

        dbHelper.addUser(user);
        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
