package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

sealed class ItemAction {
    data class LoadItems(val page: Int) : ItemAction()
    data object LoadProfile : ItemAction()
}