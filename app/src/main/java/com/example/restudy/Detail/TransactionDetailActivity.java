package com.example.restudy.Detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restudy.R;
import com.example.restudy.database.PackagesDatabaseHelper;
import com.example.restudy.database.TransactionsDatabaseHelper;
import com.example.restudy.database.UserDatabaseHelper;
import com.example.restudy.model.Packages;
import com.example.restudy.model.Transactions;
import com.example.restudy.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionDetailActivity extends AppCompatActivity {

    TextView tvTransactionId, tvUserName, tvUserEmail, tvUserPhone;
    TextView tvPackageName, tvAmount, tvStatus, tvCreatedAt, tvPostsLeft;
    TextView tvConfirmation, tvUserNameSign, tvProviderName, tvInvoiceDate;

    TransactionsDatabaseHelper transactionsDB;
    UserDatabaseHelper userDB;
    PackagesDatabaseHelper packageDB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.transaction_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ TextView
        tvTransactionId = findViewById(R.id.tvTransactionId);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserPhone = findViewById(R.id.tvUserPhone);
        tvPackageName = findViewById(R.id.tvPackageName);
        tvAmount = findViewById(R.id.tvAmount);
        tvStatus = findViewById(R.id.tvStatus);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvPostsLeft = findViewById(R.id.tvPostsLeft);
        tvConfirmation = findViewById(R.id.tvConfirmation);
        tvUserNameSign = findViewById(R.id.tvUserNameSign);
        tvProviderName = findViewById(R.id.tvProviderName);
        tvInvoiceDate = findViewById(R.id.tvInvoiceDate);

        // DB
        transactionsDB = new TransactionsDatabaseHelper(this);
        userDB = new UserDatabaseHelper(this);
        packageDB = new PackagesDatabaseHelper(this);

        // Nhận ID giao dịch từ Intent
        int transactionId = getIntent().getIntExtra("transaction_id", -1);
        Log.d("TRANSACTION_DETAIL", "transactionId nhận được từ Intent: " + transactionId);

        if (transactionId != -1) {
            loadTransactionDetail(transactionId);
        } else {
            Log.e("TRANSACTION_DETAIL", "Không tìm thấy transactionId trong Intent");
            Toast.makeText(this, "Không tìm thấy giao dịch", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadTransactionDetail(int id) {
        Transactions t = null;
        for (Transactions item : transactionsDB.getAllTransactions()) {
            if (item.getId() == id) {
                t = item;
                break;
            }
        }

        if (t == null) {
            Log.e("TRANSACTION_DETAIL", "Không tìm thấy giao dịch với ID: " + id);
            Toast.makeText(this, "Giao dịch không tồn tại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("TRANSACTION_DETAIL", "Giao dịch tìm được: " + t.toString());

        User user = userDB.getUserById(t.getUserId());
        Packages pkg = packageDB.getPackageById(t.getPackageId());

        // Set dữ liệu cho giao diện
        tvTransactionId.setText("🆔 Mã giao dịch: #" + t.getId());
        tvUserName.setText("👤 Người dùng: " + (user != null ? user.getUserName() : "Không rõ"));
        tvUserEmail.setText("📧 Email: " + (user != null ? user.getEmail() : "Không rõ"));
        tvUserPhone.setText("📞 Điện thoại: " + (user != null ? user.getPhone() : "Không rõ"));
        tvPackageName.setText("📦 Gói: " + (pkg != null ? pkg.getName() : "Không rõ"));
        tvAmount.setText("💰 Số tiền: " + t.getAmount() + " VNĐ");
        tvStatus.setText("⚙ Trạng thái: " + t.getStatus());
        tvCreatedAt.setText("🕒 Ngày tạo: " + t.getCreatedAt());
        tvPostsLeft.setText("📝 Số bài đăng còn lại: " + (pkg != null ? pkg.getMaxPosts() : "N/A"));

        // Hiển thị xác nhận thanh toán với thời gian hiện tại
        String confirmationTime = getCurrentDateTime();
        tvConfirmation.setText("✅ Đã xác nhận thanh toán lúc: " + confirmationTime);

        // Tên người mua dưới chữ ký
        tvUserNameSign.setText("(" + (user != null ? user.getUserName() : "Người mua") + ")");

        // Tên bên cung cấp mặc định
        tvProviderName.setText("(cuahangbandocu.vn)");

        // Ngày xuất hóa đơn
        tvInvoiceDate.setText("🗓 Ngày xuất hóa đơn: " + getCurrentDate());
    }

    private String getCurrentDateTime() {
        return new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }
}
