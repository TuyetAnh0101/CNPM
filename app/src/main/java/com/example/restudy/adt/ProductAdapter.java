package com.example.restudy.adt;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restudy.R;
import com.example.restudy.Utils;
import com.example.restudy.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private ProductCallback callback;

    // Constructor
    public ProductAdapter(Context context, List<Product> productList, ProductCallback callback) {
        this.context = context;
        this.productList = productList;
        this.callback = callback;
    }

    // ViewHolder tạo từ layout
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_product_item, parent, false);
        return new ProductViewHolder(view);
    }

    // Gắn dữ liệu vào ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Load ảnh từ thư mục assets
        holder.imgProduct.setImageDrawable(null); // Xóa ảnh cũ
        Bitmap bitmap = Utils.convertToBitmapFromAssets(context, product.getImage());
        if (bitmap != null) {
            holder.imgProduct.setImageBitmap(bitmap);
        } else {
            holder.imgProduct.setImageResource(R.drawable.person); // Ảnh mặc định
        }

        // Gán dữ liệu văn bản
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(Utils.formatPrice(product.getPrice()));
        holder.tvProductDescription.setText(product.getDescription());

        // Sự kiện khi nhấn Edit
        holder.btnEdit.setOnClickListener(v -> {
            if (callback != null) {
                callback.onProductEdit(position);
            }
        });

        // Sự kiện khi nhấn Delete
        holder.btnDelete.setOnClickListener(v -> {
            if (callback != null) {
                callback.onProductDelete(position);
            }
        });

        // Sự kiện khi click vào item (xem chi tiết)
        holder.itemView.setOnClickListener(v -> {
            if (callback != null) {
                callback.onProductClick(product.getId());
            }
        });
    }

    // Tổng số item
    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    // Hàm cập nhật danh sách sản phẩm
    public void updateList(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    // ViewHolder cho mỗi item sản phẩm
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductPrice, tvProductDescription;
        Button btnEdit, btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductDescription = itemView.findViewById(R.id.tvProductDescription);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    // Interface callback cho sự kiện click
    public interface ProductCallback {
        void onProductClick(int productId);   // Khi click vào item để xem chi tiết
        void onProductEdit(int position);     // Khi click vào nút sửa
        void onProductDelete(int position);   // Khi click vào nút xóa
    }
}
