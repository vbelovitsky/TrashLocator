package com.java.vbel.trashlocator.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.vbel.trashlocator.R;
import com.java.vbel.trashlocator.adapters.CategoryAdapter;
import com.java.vbel.trashlocator.dto.CategoryItem;

import java.util.ArrayList;

public class CategoryFragment extends DialogFragment{

    ArrayList<CategoryItem> categoryItems = new ArrayList<>();
    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        categoryItems = getArguments().getParcelableArrayList("categoryItems");

        View rootView = inflater.inflate(R.layout.category_fragment, container);

        recyclerView = rootView.findViewById(R.id.categoryRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        categoryAdapter = new CategoryAdapter(this.getActivity(), categoryItems, this);
        recyclerView.setAdapter(categoryAdapter);


        //this.getDialog().setTitle("Выберите категорию мусора:");

        return rootView;
    }

}
