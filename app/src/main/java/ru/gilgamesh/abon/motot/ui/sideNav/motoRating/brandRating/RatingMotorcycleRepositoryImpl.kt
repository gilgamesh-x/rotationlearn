package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.brandRating

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gilgamesh.abon.motot.data.api.RatingApi
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.brandRating.recyclerViewRatingBrand.RatingMotorcycleItem
import java.util.stream.Collectors
import javax.inject.Inject

private const val PAGE_SIZE = 15
class RatingMotorcycleRepositoryImpl @Inject constructor(private val ratingApi: RatingApi) :
    RatingMotorcycleRepository {

    private val _items = MutableLiveData<List<RatingMotorcycleItem>>()
    private var currentPage: Int = 0
    private var isLast: Boolean = false

    override suspend fun getBrandRatingFirst() {
        withContext(Dispatchers.IO) {
            isLast = false
            currentPage = 0
            getBrandRating()
        }
    }

    override suspend fun getBrandRatingNext() {
        withContext(Dispatchers.IO) {
            currentPage++
            getBrandRating()
        }
    }

    override suspend fun getBrandModelRatingFirst() {
        withContext(Dispatchers.IO) {
            isLast = false
            currentPage = 0
            getBrandModelRating()
        }
    }

    override suspend fun getBrandModelRatingNext() {
        withContext(Dispatchers.IO) {
            currentPage++
            getBrandModelRating()
        }
    }

    private suspend fun getBrandRating() {
        if (isLast) return
        runCatching {
            val response = ratingApi.getBrandRating(currentPage, PAGE_SIZE)
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
        }.onFailure {
            Log.e("RatingMotorcycleRepository", it.toString())
        }
    }


    private suspend fun getBrandModelRating() {
        if (isLast) return

        runCatching {
            val response = ratingApi.getBrandModelRating(currentPage, PAGE_SIZE)
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
        }.onFailure {
            Log.e("RatingMotorcycleRepository", it.toString())
        }
    }

    override fun observeBrandRating(): LiveData<List<RatingMotorcycleItem>> = _items
    override fun clear() {
        _items.postValue(emptyList())
    }
}