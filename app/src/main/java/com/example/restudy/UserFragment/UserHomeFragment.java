package com.example.restudy.UserFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.restudy.Detail.ProductDetailActivity;
import com.example.restudy.Manager.CartActivity;
import com.example.restudy.R;
import com.example.restudy.adt.UserProductAdapter;
import com.example.restudy.data.ProductDataQuery;
import com.example.restudy.model.Product;

import java.util.ArrayList;
import java.util.List;

public class UserHomeFragment extends Fragment implements UserProductAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private UserProductAdapter adapter;
    private List<Product> productList;

    private EditText searchEditText;
    private ImageView searchIcon;

    private String userRole = "user"; // Mặc định là user

    public UserHomeFragment() {
    }

    public static UserHomeFragment newInstance(String role) {
        UserHomeFragment fragment = new UserHomeFragment();
        Bundle args = new Bundle();
        args.putString("role", role);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            userRole = getArguments().getString("role", "user");
        } else {
            SharedPreferences prefs = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            userRole = prefs.getString("userRole", "user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchEditText = view.findViewById(R.id.search_edit_text);
        searchIcon = view.findViewById(R.id.searchIcon);

        ImageView cartIcon = view.findViewById(R.id.cartIcon);
        cartIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CartActivity.class);
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 sản phẩm mỗi hàng
        loadProducts();

        searchIcon.setOnClickListener(v -> {
            String keyword = searchEditText.getText().toString().trim();
            filterProducts(keyword);
        });

        return view;
    }

    private void loadProducts() {
        productList = ProductDataQuery.getAll(getContext());
        adapter = new UserProductAdapter(getContext(), productList, this);
        recyclerView.setAdapter(adapter);
    }

    private void filterProducts(String keyword) {
        if (keyword.isEmpty()) {
            adapter.updateData(productList);
            return;
        }
        List<Product> filtered = new ArrayList<>();
        for (Product p : productList) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(p);
            }
        }
        adapter.updateData(filtered);
    }

    @Override
    public void onItemClick(Product product) {
        openProductDetail(product);
    }

    private void openProductDetail(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        if ("admin".equals(userRole)) {
            inflater.inflate(R.menu.bottom_menu, menu);
        } else {
            inflater.inflate(R.menu.menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
}
