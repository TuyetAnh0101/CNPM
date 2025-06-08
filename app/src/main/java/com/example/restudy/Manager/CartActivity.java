package com.example.restudy.Manager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restudy.R;
import com.example.restudy.adt.CartAdapter;
import com.example.restudy.model.CartManager;
import com.example.restudy.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnProductChangeListener {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<Product> cartProducts;
    private Button buttonCheckout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerViewCart);
        buttonCheckout = findViewById(R.id.buttonCheckout);

        cartProducts = getCartProductsFromCartManager();

        adapter = new CartAdapter(this, cartProducts, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonCheckout.setOnClickListener(v -> {
            double totalPrice = 0;
            for (Product p : cartProducts) {
                totalPrice += p.getPrice() * p.getStock(); // stock là số lượng
            }
            Toast.makeText(this, "Tổng tiền: " + String.format("%,.2f đ", totalPrice), Toast.LENGTH_LONG).show();
            // TODO: Xử lý thanh toán ở đây
        });
    }

    private List<Product> getCartProductsFromCartManager() {
        List<CartManager.CartItem> cartItems = CartManager.getInstance().getCartItems();
        List<Product> products = new ArrayList<>();
        for (CartManager.CartItem item : cartItems) {
            Product p = item.getProduct();
            p.setStock(item.getQuantity());  // Cập nhật số lượng sản phẩm
            products.add(p);
        }
        return products;
    }

    @Override
    public void onQuantityChanged(int position, int newQuantity) {
        Log.d("CartActivity", "Sản phẩm " + position + " thay đổi số lượng thành: " + newQuantity);
        // Cập nhật lại số lượng trong CartManager luôn
        Product product = cartProducts.get(position);
        product.setStock(newQuantity);
        CartManager.getInstance().removeProductCompletely(product);  // Xóa sản phẩm hiện tại
        for (int i = 0; i < newQuantity; i++) {
            CartManager.getInstance().addProduct(product);         // Thêm lại đúng số lượng mới
        }

        adapter.notifyItemChanged(position);
    }

    @Override
    public void onProductClicked(int position) {
        Toast.makeText(this, "Bạn chọn sản phẩm: " + cartProducts.get(position).getName(), Toast.LENGTH_SHORT).show();
        // TODO: Mở chi tiết sản phẩm nếu cần
    }

    @Override
    public void onStatusChanged(int position, boolean status) {
        // Có thể xử lý khi người dùng check/uncheck sản phẩm trong giỏ hàng
        Product product = cartProducts.get(position);
        product.setStatus(status);
        // TODO: Xử lý thêm nếu cần
    }
}
