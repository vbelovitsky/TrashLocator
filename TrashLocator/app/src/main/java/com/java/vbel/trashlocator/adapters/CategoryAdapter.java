package com.java.vbel.trashlocator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.java.vbel.trashlocator.R;
import com.java.vbel.trashlocator.dto.CategoryItem;
import com.java.vbel.trashlocator.fragments.CategoryFragment;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

    private Context context;
    private ArrayList<CategoryItem> categoryItems;
    private CategoryFragment categoryFragment;


    public CategoryAdapter(Context context, ArrayList<CategoryItem> categoryItems, CategoryFragment categoryFragment) {
        this.context = context;
        this.categoryItems = categoryItems;
        this.categoryFragment = categoryFragment;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

        CategoryHolder categoryHolder = new CategoryHolder(view);

        return categoryHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.itemView.setOnClickListener(categoryOnClickListener);

        //Set Id to holder
        holder.itemView.setTag(categoryItems.get(position));

        //Set UI
        holder.categoryTitle.setText(categoryItems.get(position).getTitle());
        holder.categoryDescription.setText(categoryItems.get(position).getDescription());
        holder.categoryImage.setImageResource((int)categoryItems.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return categoryItems.size();
    }

    private View.OnClickListener categoryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CategoryItem categoryItem = (CategoryItem) v.getTag();
            GetCategoryFromDialog activity = (GetCategoryFromDialog) categoryFragment.getActivity();
            activity.getCategoryFromDialog(categoryItem.getId(), categoryItem.getTitle());
            categoryFragment.dismiss();
        }
    };

//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//    }

    public interface GetCategoryFromDialog{
        void getCategoryFromDialog(Long id, String title);
    }
}
