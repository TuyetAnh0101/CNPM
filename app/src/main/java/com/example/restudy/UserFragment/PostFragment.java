package com.example.restudy.UserFragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.restudy.R;
import com.example.restudy.database.CategoryDatabaseHelper;
import com.example.restudy.database.ProductDatabaseHelper;
import com.example.restudy.model.Category;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PostFragment extends Fragment {

    private ImageView imgProduct;
    private EditText etProductName, etProductDescription, etProductPrice, etProductQuantity;
    private Spinner spinnerCategory;
    private Button btnPostProduct;

    private Uri selectedImageUri;
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<Integer> categoryIds = new ArrayList<>();

    private ProductDatabaseHelper productDBHelper;
    private CategoryDatabaseHelper categoryDBHelper;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    public PostFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productDBHelper = new ProductDatabaseHelper(requireContext());
        categoryDBHelper = new CategoryDatabaseHelper(requireContext());

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        imgProduct.setImageURI(selectedImageUri);
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_post, container, false);

        imgProduct = root.findViewById(R.id.img_product);
        etProductName = root.findViewById(R.id.et_product_name);
        etProductDescription = root.findViewById(R.id.et_product_description);
        etProductPrice = root.findViewById(R.id.et_product_price);
        etProductQuantity = root.findViewById(R.id.et_product_quantity);
        spinnerCategory = root.findViewById(R.id.spinner_category);
        btnPostProduct = root.findViewById(R.id.btn_post_product);

        loadCategories();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        if (categories.isEmpty()) {
            btnPostProduct.setEnabled(false);
            Toast.makeText(requireContext(), "Chưa có danh mục nào, vui lòng thêm danh mục trước.", Toast.LENGTH_LONG).show();
        } else {
            btnPostProduct.setEnabled(true);
        }

        imgProduct.setOnClickListener(v -> openImagePicker());
        btnPostProduct.setOnClickListener(v -> postProduct());

        return root;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void loadCategories() {
        categories.clear();
        categoryIds.clear();

        ArrayList<Category> listFromDB = categoryDBHelper.getAllCategories();
        if (listFromDB != null && !listFromDB.isEmpty()) {
            for (Category cat : listFromDB) {
                categoryIds.add(cat.getId());
                categories.add(cat.getName());
            }
        }
    }

    private void postProduct() {
        String name = etProductName.getText().toString().trim();
        String description = etProductDescription.getText().toString().trim();
        String priceStr = etProductPrice.getText().toString().trim();
        String quantityStr = etProductQuantity.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty() || selectedImageUri == null) {
            Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin và chọn ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        int price, quantity;
        try {
            price = Integer.parseInt(priceStr);
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Giá và số lượng phải là số hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPos = spinnerCategory.getSelectedItemPosition();
        if (selectedPos < 0 || selectedPos >= categoryIds.size()) {
            Toast.makeText(requireContext(), "Danh mục không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        int categoryId = categoryIds.get(selectedPos);
        String imageUriStr = selectedImageUri.toString();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        long result = productDBHelper.addProduct(
                name,
                (double) price,
                description,
                imageUriStr,
                categoryId,
                quantity,
                1, // trạng thái mặc định
                now,
                now
        );

        if (result != -1) {
            Toast.makeText(requireContext(), "Đăng bài thành công!", Toast.LENGTH_LONG).show();
            resetForm();

            // Gửi broadcast thông báo sản phẩm mới được thêm
            Intent intent = new Intent("com.example.restudy.PRODUCT_ADDED");
            requireContext().sendBroadcast(intent);
        } else {
            Toast.makeText(requireContext(), "Đăng bài thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetForm() {
        etProductName.setText("");
        etProductDescription.setText("");
        etProductPrice.setText("");
        etProductQuantity.setText("");
        if (!categories.isEmpty()) spinnerCategory.setSelection(0);
        imgProduct.setImageResource(android.R.drawable.ic_menu_camera);
        selectedImageUri = null;
    }
}
