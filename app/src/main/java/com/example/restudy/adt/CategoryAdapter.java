package com.example.restudy.adt;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restudy.R;
import com.example.restudy.database.CategoryDatabaseHelper;
import com.example.restudy.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private ArrayList<Category> list;
    private CategoryDatabaseHelper db;

    public CategoryAdapter(Context context, ArrayList<Category> list, CategoryDatabaseHelper db) {
        this.context = context;
        this.list = list;
        this.db = db;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = list.get(position);
        holder.txtName.setText(category.getName());
        holder.txtPrice.setText("Giá: " + category.getPrice());

        holder.btnEdit.setOnClickListener(v -> showEditDialog(category));
        holder.btnDelete.setOnClickListener(v -> showDeleteDialog(category));
    }

    private void showEditDialog(Category category) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_category_layout, null);
        EditText edtName = view.findViewById(R.id.edtEditCategoryName);
        EditText edtPrice = view.findViewById(R.id.edtEditCategoryPrice);

        edtName.setText(category.getName());
        edtPrice.setText(String.valueOf(category.getPrice()));

        new AlertDialog.Builder(context)
                .setTitle("Sửa danh mục")
                .setView(view)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String newName = edtName.getText().toString().trim();
                    String priceStr = edtPrice.getText().toString().trim();

                    if (newName.isEmpty() || priceStr.isEmpty()) {
                        Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double newPrice;
                    try {
                        newPrice = Double.parseDouble(priceStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(context, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    category.setName(newName);
                    category.setPrice(newPrice);
                    db.updateCategory(category);
                    reloadData();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showDeleteDialog(Category category) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa danh mục")
                .setMessage("Bạn có chắc chắn muốn xóa \"" + category.getName() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    db.deleteCategory(category.getId());
                    reloadData();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void reloadData() {
        list.clear();
        list.addAll(db.getAllCategories());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageButton btnEdit, btnDelete;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tvCategoryName);
            txtPrice = itemView.findViewById(R.id.tvCategoryPrice);
            btnEdit = itemView.findViewById(R.id.btnEditCategory);
            btnDelete = itemView.findViewById(R.id.btnDeleteCategory);
        }
    }
}
