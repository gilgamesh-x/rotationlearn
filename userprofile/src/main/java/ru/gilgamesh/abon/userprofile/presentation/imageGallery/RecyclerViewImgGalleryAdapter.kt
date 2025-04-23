package ru.gilgamesh.abon.userprofile.presentation.imageGallery

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.gilgamesh.abon.core.glide.CustomGlideMethod
import ru.gilgamesh.abon.userprofile.R

class RecyclerViewImgGalleryAdapter(
    private val listenerItem: OnItemClickListener,
    private val listenerItemMenu: OnItemClickDeleteListener
) : ListAdapter<ItemImg, RecyclerViewImgGallery>(ItemDiffCallback()) {
    fun interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun interface OnItemClickDeleteListener {
        fun onItemDelete(itemId: Long)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerViewImgGallery {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_imggallery_view, viewGroup, false)
        val holder = RecyclerViewImgGallery(view)
        holder.image.setOnClickListener {
            listenerItem.onItemClick(
                holder.absoluteAdapterPosition
            )
        }
        holder.image.setOnLongClickListener {
            showPopup(it, getItem(holder.absoluteAdapterPosition).id)
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerViewImgGallery, position: Int) {
        CustomGlideMethod.getContactImageByIdByte(
            holder.itemView.context, getItem(position).id, holder.image
        )
    }

    private fun showPopup(_v: View, id: Long) {
        val popup = PopupMenu(_v.context, _v)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { v: MenuItem ->
            listenerItemMenu.onItemDelete(id)
            return@OnMenuItemClickListener true
        })
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_delete, popup.menu)
        popup.show()
    }
}

class ItemDiffCallback : DiffUtil.ItemCallback<ItemImg>() {
    override fun areItemsTheSame(oldItem: ItemImg, newItem: ItemImg): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ItemImg, newItem: ItemImg): Boolean {
        return oldItem.id == newItem.id
    }
}

data class ItemImg(var id: Long)