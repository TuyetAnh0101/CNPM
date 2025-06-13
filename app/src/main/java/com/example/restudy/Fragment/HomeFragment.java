package com.example.restudy.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restudy.Manager.CategoryManagerActivity;
import com.example.restudy.Manager.PackagesManagerActivity;
import com.example.restudy.Manager.ProductManagerActivity;
import com.example.restudy.Manager.TransactionsManagerActivity;
import com.example.restudy.Manager.UserManagerActivity;
import com.example.restudy.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ các button
        View btnPackagesManager = view.findViewById(R.id.btnPackagesManager);
        View btnProductManager = view.findViewById(R.id.btnProductManager);
        View btnCategoryManager = view.findViewById(R.id.btnCategoryManager);
        View btnTransactionsManager = view.findViewById(R.id.btnTransactionsManager);


        // Mở các Activity khi click
        btnPackagesManager.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PackagesManagerActivity.class));
        });

        btnProductManager.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProductManagerActivity.class));
        });

        btnCategoryManager.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CategoryManagerActivity.class));
        });

        btnTransactionsManager.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), TransactionsManagerActivity.class));
        });



        // Nếu bạn có thêm nút như: btnExerciseManager, btnReviewManager thì thêm ở đây

        return view;
    }
}
