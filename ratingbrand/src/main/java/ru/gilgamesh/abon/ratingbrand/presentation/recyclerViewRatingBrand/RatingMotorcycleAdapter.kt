package ru.gilgamesh.abon.ratingbrand.presentation.recyclerViewRatingBrand

import android.graphics.LinearGradient
import android.graphics.Shader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.gilgamesh.abon.ratingbrand.R

class RatingMotorcycleAdapter(private var items: MutableList<RatingMotorcycleItem>) :
    RecyclerView.Adapter<RatingMotorcycleAdapter.ItemViewHolder>() {
    private var maxCount: Int = 0

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeText: TextView = view.findViewById(R.id.place_text)
        val brandText: TextView = view.findViewById(R.id.brand_text)
        val modelText: TextView = view.findViewById(R.id.model_text)
        val countText: TextView = view.findViewById(R.id.count_text)
        val imageBrand: ImageView = view.findViewById(R.id.imageBrand)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val mainLayout: ConstraintLayout = itemView.findViewById(R.id.mainLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rating_motorcycle, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val place: Int = position + 1
        if (place in 1..3) {
            val shader: Shader = LinearGradient(
                0f,
                holder.placeText.textSize,
                holder.placeText.paint.measureText(holder.placeText.getText() as String),
                holder.placeText.textSize,
                "#FF7919".toColorInt(),
                "#FFCF53".toColorInt(),
                Shader.TileMode.REPEAT
            )
            holder.placeText.paint.setShader(shader)
        } else {
            val shader: Shader = LinearGradient(
                0f,
                holder.placeText.textSize,
                holder.placeText.paint.measureText(holder.placeText.getText() as String),
                holder.placeText.textSize,
                "#FCFCFC".toColorInt(),
                "#FCFCFC".toColorInt(),
                Shader.TileMode.REPEAT
            )
            holder.placeText.paint.setShader(shader)
        }
        holder.placeText.text = place.toString()
        holder.brandText.text = items[position].brand
        calcBrandImage(items[position].brand, holder.imageBrand)
        holder.countText.text = items[position].id.toString()

        if (items[position].model != null) {
            holder.modelText.text = items[position].model
            holder.modelText.isVisible = true
        } else {
            holder.modelText.isVisible = false
        }
        if (maxCount > 0) {
            holder.progressBar.max = maxCount
            holder.progressBar.progress = items[position].id
            holder.progressBar.isVisible = true
        } else {
            holder.progressBar.isVisible = false
        }

        var isDrawBg: Boolean = false
        /*if (App.contactInfo == null || App.contactInfo.motoBrand.isNullOrEmpty()) isDrawBg = false
        else {
            if (items[position].brand.isNotEmpty() && !items[position].model.isNullOrEmpty()) {
                isDrawBg =
                    (items[position].brand == App.contactInfo.motoBrand && items[position].model == App.contactInfo.motoModel)
            } else if (items[position].brand.isNotEmpty()) {
                isDrawBg = (items[position].brand == App.contactInfo.motoBrand)
            } else isDrawBg = false
        }*/


        if (isDrawBg) {
            holder.mainLayout.background = AppCompatResources.getDrawable(
                holder.itemView.context, R.drawable.background_radius_8_rating_me
            )
        } else {
            holder.mainLayout.background = AppCompatResources.getDrawable(
                holder.itemView.context, R.drawable.background_radius_8_rating
            )
        }
    }

    private fun calcBrandImage(brand: String, imageBrand: ImageView) {
        if (brand.isEmpty()) {
            imageBrand.isVisible = false
        } else {
            when (brand.lowercase()) {
                "bmw" -> {
                    imageBrand.setImageResource(ru.gilgamesh.abon.core.R.drawable.icon_bmw)
                    imageBrand.isVisible = true
                }
                "honda" -> {
                    imageBrand.setImageResource(ru.gilgamesh.abon.core.R.drawable.icon_honda)
                    imageBrand.isVisible = true
                }
                "suzuki" -> {
                    imageBrand.setImageResource(ru.gilgamesh.abon.core.R.drawable.icon_suzuki)
                    imageBrand.isVisible = true
                }
                "bajaj" -> {
                    imageBrand.setImageResource(ru.gilgamesh.abon.core.R.drawable.icon_bajaj)
                    imageBrand.isVisible = true
                }
                "yamaha" -> {
                    imageBrand.setImageResource(ru.gilgamesh.abon.core.R.drawable.icon_yamaha)
                    imageBrand.isVisible = true
                }
                "kawasaki" -> {
                    imageBrand.setImageResource(ru.gilgamesh.abon.core.R.drawable.icon_kawasaki)
                    imageBrand.isVisible = true
                }
                "harley-davidson" -> {
                    imageBrand.setImageResource(ru.gilgamesh.abon.core.R.drawable.icon_harley)
                    imageBrand.isVisible = true
                }
                "brp" -> {
                    imageBrand.setImageResource(ru.gilgamesh.abon.core.R.drawable.icon_brp)
                    imageBrand.isVisible = true
                }
                "husqvarna" -> {
                    imageBrand.setImageResource(ru.gilgamesh.abon.core.R.drawable.icon_husqvarna)
                    imageBrand.isVisible = true
                }
                "ducati" -> {
                    imageBrand.setImageResource(ru.gilgamesh.abon.core.R.drawable.icon_ducati)
                    imageBrand.isVisible = true
                }
                else -> {
                    imageBrand.isVisible = false
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun clearItems() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun addItems(newItems: List<RatingMotorcycleItem>) {
        if (newItems.isEmpty()) return
        val start = items.size
        items.addAll(newItems)
        maxCount = items[0].id
        notifyItemRangeInserted(start, newItems.size)
    }
}