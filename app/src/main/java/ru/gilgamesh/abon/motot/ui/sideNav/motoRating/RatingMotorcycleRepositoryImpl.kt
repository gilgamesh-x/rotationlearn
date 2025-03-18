package ru.gilgamesh.abon.motot.ui.sideNav.motoRating

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.gilgamesh.abon.motot.data.api.RatingApi
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.RVRatingMotorcycler.RatingMotorcycleItem
import java.util.stream.Collectors
import javax.inject.Inject

class RatingMotorcycleRepositoryImpl @Inject constructor(private val ratingApi: RatingApi) : RatingMotorcycleRepository {
    private val _items = MutableLiveData<List<RatingMotorcycleItem>>()
    private var currentPage: Int = 0
    private var isLast: Boolean = false

    override fun getBrandRatingFirst() {
        isLast = false
        currentPage = 0
        getBrandRating()
    }

    override fun getBrandRatingNext() {
        currentPage++
        getBrandRating()
    }

    override fun getBrandModelRatingFirst() {
        isLast = false
        currentPage = 0
        getBrandModelRating()
    }

    override fun getBrandModelRatingNext() {
        currentPage++
        getBrandModelRating()
    }

    private fun getBrandRating() {
        try {
            if (isLast) return
            val response = ratingApi.getBrandRating(currentPage, 10).execute()
            if (response.isSuccessful) {
                _items.postValue(
                    response.body()?.content?.stream()?.map {
                        RatingMotorcycleItem(
                            id = it.rating, brand = it.brand, model = null
                        )
                    }?.collect(
                        Collectors.toList()
                    )
                )
                isLast = response.body()?.last ?: true
            } else {
                isLast = true
            }
        } catch (e: Exception) {
            Log.e("RatingMotorcycleRepository", e.toString())
        }
    }


    private fun getBrandModelRating() {
        try {
            if (isLast) return
            val response = ratingApi.getBrandModelRating(currentPage, 10).execute()
            if (response.isSuccessful) {
                _items.postValue(
                    response.body()?.content?.stream()?.map {
                        RatingMotorcycleItem(
                            id = it.rating, brand = it.brand, model = it.model
                        )
                    }?.collect(
                        Collectors.toList()
                    )
                )
                isLast = response.body()?.last ?: true
            } else {
                isLast = true
            }
        } catch (e: Exception) {
            Log.e("RatingMotorcycleRepository", e.toString())
        }
    }

    override fun observeBrandRating(): LiveData<List<RatingMotorcycleItem>> = _items

}