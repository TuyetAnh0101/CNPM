package com.example.restudy.model;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    // Mẫu CartItem lưu Product và quantity
    public static class CartItem {
        private Product product;
        private int quantity;

        public CartItem(Product product) {
            this.product = product;
            this.quantity = 1;
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void incrementQuantity() {
            this.quantity++;
        }

        public void decrementQuantity() {
            if (quantity > 1) {
                this.quantity--;
            }
        }
    }

    private static CartManager instance;
    private final List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Thêm sản phẩm vào giỏ, nếu đã có thì tăng số lượng
    public void addProduct(Product product) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                item.incrementQuantity();
                return;
            }
        }
        cartItems.add(new CartItem(product));
    }

    // Giảm số lượng sản phẩm, nếu số lượng = 1 thì xóa khỏi giỏ
    public void removeProduct(Product product) {
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            if (item.getProduct().getId() == product.getId()) {
                if (item.getQuantity() > 1) {
                    item.decrementQuantity();
                } else {
                    cartItems.remove(i);
                }
                return;
            }
        }
    }

    // Xóa hẳn sản phẩm khỏi giỏ
    public void removeProductCompletely(Product product) {
        cartItems.removeIf(item -> item.getProduct().getId() == product.getId());
    }

    // Lấy danh sách CartItem (product + số lượng)
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    // Xóa sạch giỏ hàng
    public void clearCart() {
        cartItems.clear();
    }

    // Lấy tổng số sản phẩm (đếm quantity)
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getQuantity();
        }
        return total;
    }

    // Lấy tổng tiền trong giỏ
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    // Kiểm tra giỏ hàng có rỗng không
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}
