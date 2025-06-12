package com.example.restudy.Manager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restudy.R;
import com.example.restudy.database.PackagesDatabaseHelper;
import com.example.restudy.model.Packages;
import com.example.restudy.adt.PackagesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PackagesManagerActivity extends AppCompatActivity {

    private RecyclerView rcvPackages;
    private FloatingActionButton btnAddPackage;
    private PackagesAdapter adapter;
    private PackagesDatabaseHelper dbHelper;
    private List<Packages> packageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages_manager); // đảm bảo file layout này có RecyclerView và FAB

        rcvPackages = findViewById(R.id.recyclerPackages);
        btnAddPackage = findViewById(R.id.btnAddPackage);
        dbHelper = new PackagesDatabaseHelper(this);

        loadPackages();

        btnAddPackage.setOnClickListener(view -> showAddDialog());
    }

    private void loadPackages() {
        packageList = dbHelper.getAllPackages();
        adapter = new PackagesAdapter(packageList, this::showEditDialog, this::confirmDelete);
        rcvPackages.setLayoutManager(new LinearLayoutManager(this));
        rcvPackages.setAdapter(adapter);
    }

    private void showAddDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_packages_layout, null);

        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtPrice = view.findViewById(R.id.edtPrice);
        EditText edtDuration = view.findViewById(R.id.edtDuration);
        EditText edtMaxPosts = view.findViewById(R.id.edtMaxPosts);
        Switch swCanPin = view.findViewById(R.id.swCanPin);

        new AlertDialog.Builder(this)
                .setTitle("Thêm gói mới")
                .setView(view)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    try {
                        String name = edtName.getText().toString().trim();
                        int price = Integer.parseInt(edtPrice.getText().toString().trim());
                        int duration = Integer.parseInt(edtDuration.getText().toString().trim());
                        int maxPosts = Integer.parseInt(edtMaxPosts.getText().toString().trim());
                        boolean canPin = swCanPin.isChecked();

                        dbHelper.addPackage(name, price, duration, maxPosts, canPin);
                        loadPackages();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showEditDialog(Packages pkg) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_packages_layout, null);

        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtPrice = view.findViewById(R.id.edtPrice);
        EditText edtDuration = view.findViewById(R.id.edtDuration);
        EditText edtMaxPosts = view.findViewById(R.id.edtMaxPosts);
        Switch swCanPin = view.findViewById(R.id.swCanPin);

        edtName.setText(pkg.getName());
        edtPrice.setText(String.valueOf(pkg.getPrice()));
        edtDuration.setText(String.valueOf(pkg.getDuration()));
        edtMaxPosts.setText(String.valueOf(pkg.getMaxPosts()));
        swCanPin.setChecked(pkg.isCanPin());

        new AlertDialog.Builder(this)
                .setTitle("Chỉnh sửa gói")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    try {
                        dbHelper.updatePackage(pkg.getId(),
                                edtName.getText().toString().trim(),
                                Integer.parseInt(edtPrice.getText().toString().trim()),
                                Integer.parseInt(edtDuration.getText().toString().trim()),
                                Integer.parseInt(edtMaxPosts.getText().toString().trim()),
                                swCanPin.isChecked());
                        loadPackages();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void confirmDelete(Packages pkg) {
        new AlertDialog.Builder(this)
                .setTitle("Xoá gói")
                .setMessage("Bạn có chắc chắn muốn xoá gói \"" + pkg.getName() + "\" không?")
                .setPositiveButton("Xoá", (dialog, which) -> {
                    dbHelper.deletePackage(pkg.getId());
                    loadPackages();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
