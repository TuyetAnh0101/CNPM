package com.example.restudy.Manager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restudy.R;
import com.example.restudy.adt.TransactionsAdapter;
import com.example.restudy.database.PackagesDatabaseHelper;
import com.example.restudy.database.TransactionsDatabaseHelper;
import com.example.restudy.database.UserDatabaseHelper;
import com.example.restudy.model.Packages;
import com.example.restudy.model.Transactions;
import com.example.restudy.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionsManagerActivity extends AppCompatActivity {

    RecyclerView rvTransactions;
    FloatingActionButton btnAdd;
    TransactionsAdapter adapter;
    TransactionsDatabaseHelper transactionsDB;
    UserDatabaseHelper userDB;
    PackagesDatabaseHelper packageDB;
    List<Transactions> transactionList;
    List<User> userList;
    List<Packages> packageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transactions_manager);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.transaction), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        rvTransactions = findViewById(R.id.rvTransactions);
        btnAdd = findViewById(R.id.btnAddTransaction);

        transactionsDB = new TransactionsDatabaseHelper(this);
        userDB = new UserDatabaseHelper(this);
        packageDB = new PackagesDatabaseHelper(this);

        loadData();

        btnAdd.setOnClickListener(v -> showAddTransactionDialog());
    }

    private void loadData() {
        transactionList = transactionsDB.getAllTransactions();
        adapter = new TransactionsAdapter(this, transactionList);
        rvTransactions.setLayoutManager(new LinearLayoutManager(this));
        rvTransactions.setAdapter(adapter);

        for (Transactions t : transactionList) {
            if (isTransactionExpiringSoon(t)) {
                Toast.makeText(this,
                        "⚠ Gói của người dùng ID " + t.getUserId() + " sắp hết hạn!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    private void showAddTransactionDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.add_transactions_layout, null);
        Spinner spUser = view.findViewById(R.id.spUser);
        Spinner spPackage = view.findViewById(R.id.spPackage);
        Spinner spStatus = view.findViewById(R.id.spStatus);
        EditText edtAmount = view.findViewById(R.id.edtAmount);
        EditText edtCreatedAt = view.findViewById(R.id.edtCreatedAt);
        Button btnSave = view.findViewById(R.id.btnSaveTransaction);

        // Load data into spinners
        userList = userDB.getAllUsers();
        packageList = packageDB.getAllPackages();

        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getUserNames());
        ArrayAdapter<String> packageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getPackageNames());
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"success", "fail"});

        spUser.setAdapter(userAdapter);
        spPackage.setAdapter(packageAdapter);
        spStatus.setAdapter(statusAdapter);

        edtCreatedAt.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        btnSave.setOnClickListener(v -> {
            int userId = userList.get(spUser.getSelectedItemPosition()).getId();
            int packageId = packageList.get(spPackage.getSelectedItemPosition()).getId();
            int amount = Integer.parseInt(edtAmount.getText().toString());
            String status = spStatus.getSelectedItem().toString();
            String createdAt = edtCreatedAt.getText().toString();

            Transactions t = new Transactions(0, userId, packageId, amount, status, createdAt);
            transactionsDB.insertTransaction(t);
            loadData();
            Toast.makeText(this, "Đã thêm giao dịch", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
    private boolean isTransactionExpiringSoon(Transactions transaction) {
        Packages pkg = packageDB.getPackageById(transaction.getPackageId());
        if (pkg == null) return false;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date createdAtDate = sdf.parse(transaction.getCreatedAt());

            long durationDays = pkg.getDuration(); // Số ngày
            long millisPerDay = 24 * 60 * 60 * 1000L;
            long expiredTime = createdAtDate.getTime() + durationDays * millisPerDay;

            long now = System.currentTimeMillis();
            long oneDayBeforeExpire = expiredTime - millisPerDay;

            return now >= oneDayBeforeExpire && now <= expiredTime;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private List<String> getUserNames() {
        List<String> names = new ArrayList<>();
        for (User u : userList) names.add(u.getUserName());
        return names;
    }

    private List<String> getPackageNames() {
        List<String> names = new ArrayList<>();
        for (Packages p : packageList) names.add(p.getName());
        return names;
    }
}