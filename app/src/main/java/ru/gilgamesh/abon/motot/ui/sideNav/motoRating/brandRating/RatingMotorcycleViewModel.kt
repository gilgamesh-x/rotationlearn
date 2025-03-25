package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.brandRating

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.brandRating.recyclerViewRatingBrand.RatingMotorcycleItem
import javax.inject.Inject

@HiltViewModel
class RatingMotorcycleViewModel @Inject constructor(
    private val ratingMotorcycleRepository: RatingMotorcycleRepository
) : ViewModel() {
    private val _items = ratingMotorcycleRepository.observeBrandRating()
    val items: LiveData<List<RatingMotorcycleItem>> get() = _items

    private var mode: Int = 1
    private var isLoading: Boolean = false

    fun setModeBrand() {
        mode = 1
    }

    fun setModeBrandModel() {
        mode = 2
    }

    fun getFirstPage() {
        if (isLoading) return
        isLoading = true
        if (mode == 1) {
            viewModelScope.launch {
                ratingMotorcycleRepository.getBrandRatingFirst()
                isLoading = false
            }
        } else {
            viewModelScope.launch {
                ratingMotorcycleRepository.getBrandModelRatingFirst()
                isLoading = false
            }
        }
    }

    fun getNextPage() {
        if (isLoading) return
        isLoading = true
        if (mode == 1) {
            viewModelScope.launch {
                ratingMotorcycleRepository.getBrandRatingNext()
                isLoading = false
            }
        } else {
            viewModelScope.launch {
                ratingMotorcycleRepository.getBrandModelRatingNext()
                isLoading = false
            }
        }
    }

    fun clear() {
        ratingMotorcycleRepository.clear()
    }
}