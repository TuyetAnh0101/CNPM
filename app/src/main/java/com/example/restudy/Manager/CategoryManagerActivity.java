package com.example.restudy.Manager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restudy.R;
import com.example.restudy.adt.CategoryAdapter;
import com.example.restudy.database.CategoryDatabaseHelper;
import com.example.restudy.model.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CategoryManagerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private CategoryAdapter adapter;
    private ArrayList<Category> categoryList;
    private CategoryDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manager);

        recyclerView = findViewById(R.id.recyclerViewCategories);
        fabAdd = findViewById(R.id.fabAddCategory);
        db = new CategoryDatabaseHelper(this);

        loadData();

        fabAdd.setOnClickListener(v -> showAddDialog());
    }

    private void loadData() {
        categoryList = db.getAllCategories();
        adapter = new CategoryAdapter(this, categoryList, db);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.add_category_layout, null);
        EditText edtName = view.findViewById(R.id.edtEditCategoryName);
        EditText edtPrice = view.findViewById(R.id.edtEditCategoryPrice);

        new AlertDialog.Builder(this)
                .setTitle("Thêm danh mục")
                .setView(view)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name = edtName.getText().toString().trim();
                    String priceStr = edtPrice.getText().toString().trim();

                    if (name.isEmpty() || priceStr.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double price = Double.parseDouble(priceStr);
                    db.addCategory(name, price);
                    loadData();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
