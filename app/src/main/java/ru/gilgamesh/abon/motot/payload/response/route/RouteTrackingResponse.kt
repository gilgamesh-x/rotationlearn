package ru.gilgamesh.abon.motot.payload.response.route

import com.google.gson.Gson
import lombok.Getter
import lombok.Setter

class RouteTrackingResponse {
    var id: Long? = null
    var distance: Int? = null
    var pointList: String? = null
    var averageSpeed: Int? = null
    var activeTime: Long? = null
    var totalTime: Long? = null

    override fun toString(): String {
        return Gson().toJson(this)
    }
}
