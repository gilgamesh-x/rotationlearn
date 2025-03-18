package ru.gilgamesh.abon.motot.payload.response.auth

import android.content.Context
import com.google.gson.Gson
import ru.gilgamesh.abon.motot.model.App
import java.util.Date

class SigninResponse {
    var token: String? = null
    var type: String? = null
    var refreshToken: String? = null
    var id: Long? = null
    var roles: Set<String>? = null
    var dateLoginLong: Long? = null
    var tokenDurationMs: Long? = null
    var contactId: Long? = null


    /*fun setAllData(response: RefreshTokenResponse?) {
        if (response == null) return
        if (response.type != null) {
            this.type = response.type
        }
        if (response.token != null) {
            this.token = response.token
        }
        if (response.refreshToken != null) {
            this.refreshToken = response.refreshToken
        }
        if (response.tokenDurationMs != null) {
            this.tokenDurationMs = response.tokenDurationMs
        }
        App.loginInfo?.dateLoginLong = Date().time
        saveToPref()
    }*/

    /*private fun saveToPref() {
        val preferences = App.contextApp.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("type", this.type)
        editor.putString("token", this.token)
        editor.putString("refreshToken", this.refreshToken)
        editor.putLong("tokenDurationMs", tokenDurationMs!!)
        App.loginInfo.dateLoginLong?.let { editor.putLong("dateLoginLong", it) }
        editor.putString("jsonLogin", this.toString())
        editor.commit()
    }*/

    override fun toString(): String {
        return Gson().toJson(this)
    }
}
