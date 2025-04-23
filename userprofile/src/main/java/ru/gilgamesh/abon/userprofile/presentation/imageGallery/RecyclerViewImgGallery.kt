package ru.gilgamesh.abon.userprofile.presentation.imageGallery

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.gilgamesh.abon.userprofile.R

class RecyclerViewImgGallery(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var image: ImageView = itemView.findViewById(R.id.imageView)
}