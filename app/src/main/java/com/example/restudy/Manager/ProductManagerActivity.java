package com.example.restudy.Manager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restudy.adt.ProductAdapter;
import com.example.restudy.R;
import com.example.restudy.database.CategoryDatabaseHelper;
import com.example.restudy.database.ProductDatabaseHelper;
import com.example.restudy.model.Category;
import com.example.restudy.model.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProductManagerActivity extends AppCompatActivity implements ProductAdapter.ProductCallback {

    private RecyclerView recyclerViewProducts;
    private Button btnAddProduct;
    private ProductDatabaseHelper productDB;
    private CategoryDatabaseHelper categoryDB;

    private ArrayList<Product> productList = new ArrayList<>();
    private ArrayList<Category> categoryList = new ArrayList<>();
    private ProductAdapter productAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manager);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.product_manager), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        productDB = new ProductDatabaseHelper(this);
        categoryDB = new CategoryDatabaseHelper(this);

        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this, productList, this);
        recyclerViewProducts.setAdapter(productAdapter);

        loadData();

        btnAddProduct.setOnClickListener(v -> openAddDialog());
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(productAddedReceiver, new IntentFilter("com.example.restudy.PRODUCT_ADDED"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(productAddedReceiver);
    }

    private void loadData() {
        productList.clear();
        ArrayList<Product> listFromDB = productDB.getAllProducts();
        if (listFromDB != null) {
            productList.addAll(listFromDB);
            Log.d("ProductManager", "Loaded products count: " + listFromDB.size());
        }
        productAdapter.notifyDataSetChanged();

        categoryList = categoryDB.getAllCategories();
        if (categoryList == null) categoryList = new ArrayList<>();
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.addpoductlayout);
        dialog.setCancelable(true);

        EditText edtImagePath = dialog.findViewById(R.id.edtImagePath);
        EditText edtName = dialog.findViewById(R.id.edtProductName);
        EditText edtDescription = dialog.findViewById(R.id.edtProductDescription);
        EditText edtPrice = dialog.findViewById(R.id.edtProductPrice);
        EditText edtQuantity = dialog.findViewById(R.id.edtProductQuantity);
        Spinner spinnerCategory = dialog.findViewById(R.id.spinnerCategory);
        Switch switchStatus = dialog.findViewById(R.id.switchStatus);
        Button btnSave = dialog.findViewById(R.id.btnSaveProduct);

        if (categoryList.isEmpty()) {
            Toast.makeText(this, "Chưa có danh mục nào. Vui lòng thêm trước.", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            return;
        }

        ArrayAdapter<Category> adapterSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryList);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterSpinner);

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String quantityStr = edtQuantity.getText().toString().trim();
            String imagePath = edtImagePath.getText().toString().trim();
            boolean isActive = switchStatus.isChecked();

            if (name.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty() || imagePath.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);

                Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
                int categoryId = selectedCategory.getId();

                String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                long newId = productDB.addProduct(name, price, description, imagePath, categoryId, quantity, isActive ? 1 : 0, now, now);
                if (newId != -1) {
                    Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    loadData();
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá và số lượng phải là số hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    public void onProductEdit(int position) {
        Product product = productList.get(position);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.addpoductlayout);
        dialog.setCancelable(true);

        EditText edtImagePath = dialog.findViewById(R.id.edtImagePath);
        EditText edtName = dialog.findViewById(R.id.edtProductName);
        EditText edtDescription = dialog.findViewById(R.id.edtProductDescription);
        EditText edtPrice = dialog.findViewById(R.id.edtProductPrice);
        EditText edtQuantity = dialog.findViewById(R.id.edtProductQuantity);
        Spinner spinnerCategory = dialog.findViewById(R.id.spinnerCategory);
        Switch switchStatus = dialog.findViewById(R.id.switchStatus);
        Button btnSave = dialog.findViewById(R.id.btnSaveProduct);

        edtName.setText(product.getName());
        edtDescription.setText(product.getDescription());
        edtPrice.setText(String.valueOf(product.getPrice()));
        edtQuantity.setText(String.valueOf(product.getQuantity()));
        edtImagePath.setText(product.getImage());
        switchStatus.setChecked(product.getStatus());
        ArrayAdapter<Category> adapterSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryList);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterSpinner);

        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId() == product.getCategoryId()) {
                spinnerCategory.setSelection(i);
                break;
            }
        }

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String quantityStr = edtQuantity.getText().toString().trim();
            String imagePath = edtImagePath.getText().toString().trim();
            boolean isActive = switchStatus.isChecked();

            if (name.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty() || imagePath.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);

                Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
                int categoryId = selectedCategory.getId();

                String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                int result = productDB.updateProduct(
                        product.getId(),
                        name,
                        price,
                        description,
                        imagePath,
                        categoryId,
                        quantity,
                        isActive ? 1 : 0,
                        now
                );

                if (result > 0) {
                    Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    loadData();
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá và số lượng phải là số hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    public void onProductDelete(int position) {
        Product product = productList.get(position);
        int result = productDB.deleteProduct(product.getId());
        if (result > 0) {
            productList.remove(position);
            productAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private final BroadcastReceiver productAddedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadData();
            Toast.makeText(ProductManagerActivity.this, "Cập nhật danh sách sản phẩm mới", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onProductClick(int position) {
        Product clickedProduct = productList.get(position);
        Toast.makeText(this, "Clicked: " + clickedProduct.getName(), Toast.LENGTH_SHORT).show();
    }
}
