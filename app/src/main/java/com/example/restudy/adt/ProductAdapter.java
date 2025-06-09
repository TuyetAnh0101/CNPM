package com.example.restudy.adt;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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

    public ProductAdapter(Context context, List<Product> productList, ProductCallback callback) {
        this.context = context;
        this.productList = productList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_product_item, parent, false);
        return new ProductViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.imgProduct.setImageDrawable(null); // Xóa ảnh cũ trước khi set mới
        Bitmap bitmap = Utils.convertToBitmapFromAssets(context, product.getImage());
        if (bitmap != null) {
            holder.imgProduct.setImageBitmap(bitmap);
        } else {
            holder.imgProduct.setImageResource(R.drawable.person);
        }


        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(Utils.formatPrice(product.getPrice()));
        holder.tvProductDescription.setText(product.getDescription());

        holder.btnEdit.setOnClickListener(v -> {
            if (callback != null) {
                callback.onProductEdit(position);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (callback != null) {
                callback.onProductDelete(position);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (callback != null) {
                callback.onProductClick(product.getId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

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
    public void updateList(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    public interface ProductCallback {
        void onProductClick(int productId);
        void onProductEdit(int position);
        void onProductDelete(int position);
    }
}
