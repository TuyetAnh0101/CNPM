package com.example.restudy.adt;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restudy.R;
import com.example.restudy.Utils;
import com.example.restudy.model.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnProductChangeListener {
        void onQuantityChanged(int position, int newQuantity);
        void onProductClicked(int position);
        void onStatusChanged(int position, boolean status);
    }

    private final Context context;
    private final List<Product> cartList;
    private final OnProductChangeListener listener;

    public CartAdapter(Context context, List<Product> cartList, OnProductChangeListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartList.get(position);

        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(Utils.formatPrice(product.getPrice()));
        holder.tvQuantity.setText(String.valueOf(product.getStock()));

        Bitmap bitmap = Utils.convertToBitmapFromAssets(context, product.getImage());
        if (bitmap != null) {
            holder.imgProduct.setImageBitmap(bitmap);
        } else {
            holder.imgProduct.setImageResource(R.drawable.person);
        }

        // tránh listener bị gọi khi setChecked lại
        holder.cbBuy.setOnCheckedChangeListener(null);
        holder.cbBuy.setChecked(product.isStatus());

        // Set lại listener sau khi setChecked
        holder.cbBuy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.setStatus(isChecked);
            if (listener != null) {
                listener.onStatusChanged(position, isChecked);
            }
        });

        holder.btnIncrease.setOnClickListener(v -> {
            int qty = product.getStock() + 1;
            product.setStock(qty);
            holder.tvQuantity.setText(String.valueOf(qty));
            if (listener != null) listener.onQuantityChanged(position, qty);
        });

        holder.btnDecrease.setOnClickListener(v -> {
            int qty = product.getStock();
            if (qty > 1) {
                qty--;
                product.setStock(qty);
                holder.tvQuantity.setText(String.valueOf(qty));
                if (listener != null) listener.onQuantityChanged(position, qty);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onProductClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnIncrease, btnDecrease;
        TextView tvProductName, tvProductPrice, tvQuantity;
        CheckBox cbBuy;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            cbBuy = itemView.findViewById(R.id.cbBuy);
        }
    }
}
