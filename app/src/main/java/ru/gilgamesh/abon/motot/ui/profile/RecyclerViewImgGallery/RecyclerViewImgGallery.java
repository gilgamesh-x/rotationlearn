package ru.gilgamesh.abon.motot.ui.profile.RecyclerViewImgGallery;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.gilgamesh.abon.motot.R;

public class RecyclerViewImgGallery extends RecyclerView.ViewHolder {
    ImageView image;

    public RecyclerViewImgGallery(@NonNull View itemView) {
        super(itemView);
        this.image = itemView.findViewById(R.id.imageView);
    }
}