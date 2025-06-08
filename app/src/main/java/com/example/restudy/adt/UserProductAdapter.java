package com.example.restudy.adt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restudy.R;
import com.example.restudy.model.Product;
import com.example.restudy.Utils;

import java.util.List;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnItemClickListener listener;

    public UserProductAdapter(Context context, List<Product> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layoutuserproductitem, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Hiển thị ảnh, bạn cần triển khai Utils.convertToBitmapFromAssets() hoặc thay bằng Glide/Picasso
        holder.imgProduct.setImageBitmap(Utils.convertToBitmapFromAssets(context, product.getImage()));

        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(Utils.formatPrice(product.getPrice()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    /**
     * Cập nhật danh sách sản phẩm mới và refresh giao diện
     */
    public void updateData(List<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }
}
