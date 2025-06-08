package com.example.restudy.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restudy.R;
import com.example.restudy.Manager.ProductManagerActivity;

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
        View btnUserManager = view.findViewById(R.id.btnUserManager);
        View btnTopicManager = view.findViewById(R.id.btnTopicManager);
        View btnSentenceManager = view.findViewById(R.id.btnSentenceManager);
        View btnExerciseManager = view.findViewById(R.id.btnExerciseManager);

        // Xử lý sự kiện click
       // btnUserManager.setOnClickListener(v -> {
        //    Intent intent = new Intent(getActivity(), UserManagerActivity.class);
       //     startActivity(intent);
       // });

        btnTopicManager.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductManagerActivity.class);
            startActivity(intent);
        });

      //  btnSentenceManager.setOnClickListener(v -> {
        //    Intent intent = new Intent(getActivity(), ContactManagerActivity.class);
     //       startActivity(intent);
     //   });

       // btnExerciseManager.setOnClickListener(v -> {
        //    Intent intent = new Intent(getActivity(), ReviewManagerActivity.class);
          //  startActivity(intent);
        //});

        return view;
    }
}
