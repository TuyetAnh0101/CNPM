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
import androidx.viewpager2.widget.ViewPager2;

import com.example.restudy.R;
import com.example.restudy.Utils;
import com.example.restudy.adt.ProductImageAdapter;
import com.example.restudy.model.CartManager;
import com.example.restudy.model.Product;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private static final String TAG = "ProductDetailActivity";

    private ViewPager2 productImageSlider;
    private TextView productName, productPrice, productDescription;
    private Button buttonAddToCart, buttonBuyNow;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.product_details), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productImageSlider = findViewById(R.id.product_images_viewpager);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productDescription = findViewById(R.id.product_description);
        buttonAddToCart = findViewById(R.id.button_add_to_cart);
        buttonBuyNow = findViewById(R.id.button_buy_now);

        currentProduct = (Product) getIntent().getSerializableExtra("product");

        if (currentProduct != null) {
            showProductDetail(currentProduct);
        } else {
            productName.setText("Không có dữ liệu sản phẩm");
        }

        buttonAddToCart.setOnClickListener(v -> {
            if (currentProduct != null) {
                CartManager.getInstance().addProduct(currentProduct);
                Toast.makeText(this, currentProduct.getName() + " đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBuyNow.setOnClickListener(v -> {
            if (currentProduct != null) {
                Toast.makeText(this, "Mua ngay: " + currentProduct.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProductDetail(Product product) {
        productName.setText(product.getName());
        productPrice.setText("₫" + String.format("%.0f", product.getPrice()));
        productDescription.setText("Mô tả: " + product.getDescription());

        // Load ảnh (nếu là nhiều ảnh thì sửa Product để chứa danh sách ảnh)
        List<String> imageList = List.of(product.getImage()); // hoặc product.getImages() nếu có danh sách
        ProductImageAdapter adapter = new ProductImageAdapter(this, imageList);
        productImageSlider.setAdapter(adapter);
    }
}
