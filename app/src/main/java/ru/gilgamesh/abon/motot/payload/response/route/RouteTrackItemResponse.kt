package ru.gilgamesh.abon.motot.payload.response.route

import com.google.gson.Gson
import lombok.Getter
import lombok.Setter

class RouteTrackItemResponse {
    val latitude: Double? = null
    val longitude: Double? = null
    val dateTime: String? = null

    override fun toString(): String {
        return Gson().toJson(this)
    }
}
