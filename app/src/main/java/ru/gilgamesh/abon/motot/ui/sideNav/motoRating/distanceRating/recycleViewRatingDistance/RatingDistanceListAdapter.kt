package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating.recycleViewRatingDistance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.gilgamesh.abon.motot.R

class RatingDistanceListAdapter(
    private val listener: OnItemClickListener
) : ListAdapter<RatingItem, RatingHolder>(ItemDiffCallback()) {
    interface OnItemClickListener {
        fun onItemClick(item: RatingItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rating_km, parent, false)
        val holder = RatingHolder(view)
        holder.itemView.setOnClickListener {
            listener.onItemClick(
                getItem(holder.absoluteAdapterPosition)
            )
        }
        return holder
    }

    override fun onBindViewHolder(holder: RatingHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    fun addItems(newItems: List<RatingItem>) {
        val currentList = currentList.toMutableList()
        currentList.addAll(newItems)
        submitList(currentList)
    }

    fun clearItems() {
        submitList(emptyList())
    }
}

class ItemDiffCallback : DiffUtil.ItemCallback<RatingItem>() {
    override fun areItemsTheSame(oldItem: RatingItem, newItem: RatingItem): Boolean {
        return oldItem.contactId == newItem.contactId
    }

    override fun areContentsTheSame(oldItem: RatingItem, newItem: RatingItem): Boolean {
        return oldItem.contactId == newItem.contactId
    }
}
