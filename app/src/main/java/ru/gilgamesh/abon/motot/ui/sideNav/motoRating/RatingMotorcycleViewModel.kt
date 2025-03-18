package ru.gilgamesh.abon.motot.ui.sideNav.motoRating

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.RVRatingMotorcycler.RatingMotorcycleItem
import javax.inject.Inject

@HiltViewModel
class RatingMotorcycleViewModel @Inject constructor(private val ratingMotorcycleRepository: RatingMotorcycleRepository) :
    ViewModel() {
    private val _items = ratingMotorcycleRepository.observeBrandRating()
    val items: LiveData<List<RatingMotorcycleItem>> get() = _items

    var mode: Int = 1
    private var isLoading: Boolean = false


    fun getFirstPage() {
        if (isLoading) return
        isLoading = true
        if (mode == 1) {
            viewModelScope.launch(Dispatchers.IO) {
                ratingMotorcycleRepository.getBrandRatingFirst()
                isLoading = false
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                ratingMotorcycleRepository.getBrandModelRatingFirst()
                isLoading = false
            }
        }
    }

    fun getNextPage() {
        if (isLoading) return
        isLoading = true
        if (mode == 1) {
            viewModelScope.launch(Dispatchers.IO) {
                ratingMotorcycleRepository.getBrandRatingNext()
                isLoading = false
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                ratingMotorcycleRepository.getBrandModelRatingNext()
                isLoading = false
            }
        }
    }
}