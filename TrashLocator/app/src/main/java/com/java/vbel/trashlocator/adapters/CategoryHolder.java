package com.java.vbel.trashlocator.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.vbel.trashlocator.R;

public class CategoryHolder extends RecyclerView.ViewHolder{

    ImageView categoryImage;
    TextView categoryTitle;
    TextView categoryDescription;

//    private View.OnClickListener onItemClickListener;
//
//    public void setItemClickListener(View.OnClickListener clickListener) {
//        onItemClickListener = clickListener;
//    }

    public CategoryHolder(@NonNull View itemView) {
        super(itemView);
        //itemView.setTag(this);

        categoryImage = itemView.findViewById(R.id.categoryImage);
        categoryTitle = itemView.findViewById(R.id.categoryTitle);
        categoryDescription = itemView.findViewById(R.id.categoryDescription);
    }

}
