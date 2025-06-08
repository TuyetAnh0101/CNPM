package com.example.restudy.Detail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restudy.R;
import com.example.restudy.Utils;
import com.example.restudy.model.CartManager;
import com.example.restudy.model.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private static final String TAG = "ProductDetailActivity";

    private ImageView productImage;
    private TextView productName, productPrice, productDescription,
            productStock, productStatus, productCategory,
            productCreatedAt, productUpdatedAt;

    private Button buttonAddToCart, buttonBuyNow;

    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_product_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productDescription = findViewById(R.id.product_description);
        productStock = findViewById(R.id.product_stock);
        productStatus = findViewById(R.id.product_status);
        productCategory = findViewById(R.id.product_category);
        productCreatedAt = findViewById(R.id.product_createdAt);
        productUpdatedAt = findViewById(R.id.product_updatedAt);

        buttonAddToCart = findViewById(R.id.button_add_to_cart);
        buttonBuyNow = findViewById(R.id.button_buy_now);

        currentProduct = (Product) getIntent().getSerializableExtra("product");

        if (currentProduct != null) {
            showProductDetail(currentProduct);
        } else {
            productName.setText(R.string.hello_blank_fragment);
        }

        // Thêm sản phẩm vào giỏ hàng
        buttonAddToCart.setOnClickListener(v -> {
            if (currentProduct != null) {
                CartManager.getInstance().addProduct(currentProduct);
                Toast.makeText(this, currentProduct.getName() + " đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Add to cart clicked: " + currentProduct.getName());
            }
        });

        // Xử lý nút Mua ngay
        buttonBuyNow.setOnClickListener(v -> {
            if (currentProduct != null) {
                // TODO: chuyển sang trang thanh toán hoặc xử lý mua ngay
                Toast.makeText(this, "Mua ngay: " + currentProduct.getName(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Buy now clicked: " + currentProduct.getName());
            }
        });
    }

    private void showProductDetail(Product product) {
        productName.setText(product.getName());
        productPrice.setText(String.format("Price: $%.2f", product.getPrice()));
        productDescription.setText("Description: " + product.getDescription());
        productStock.setText("Stock: " + product.getStock());
        productStatus.setText("Status: " + (product.isStatus() ? "Available" : "Out of stock"));
        productCategory.setText("Category ID: " + product.getCategoryId());
        productCreatedAt.setText("Created At: " + product.getCreatedAt());
        productUpdatedAt.setText("Updated At: " + product.getUpdatedAt());

        Bitmap bitmap = Utils.convertToBitmapFromAssets(this, product.getImage());
        if (bitmap != null) {
            productImage.setImageBitmap(bitmap);
        } else {
            productImage.setImageResource(R.drawable.person);
        }
    }
}
