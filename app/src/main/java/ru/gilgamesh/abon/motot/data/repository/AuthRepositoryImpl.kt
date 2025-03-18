package ru.gilgamesh.abon.motot.data.repository

import android.content.Context
import ru.gilgamesh.abon.motot.domain.repositories.AuthRepository
import ru.gilgamesh.abon.motot.payload.response.auth.RefreshTokenResponse
import ru.gilgamesh.abon.motot.payload.response.auth.SigninResponse
import java.util.Date

class AuthRepositoryImpl (val context: Context): AuthRepository{
    override fun setLogin(login: String) {
        context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
            .edit()
            .putString(USER_INFO_LOGIN, login)
            .apply()
    }

    override fun setPassword(password: String) {
        context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
            .edit()
            .putString(USER_INFO_PASSWORD, password)
            .apply()
    }

    override fun getRemember(): Boolean {
        return context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).getBoolean(USER_INFO_REMEMBER, false)
    }

    override fun setRemember(remember: Boolean) {
        context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(USER_INFO_REMEMBER, remember)
            .apply()
    }

    override fun setId(id: Long) {
        context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
            .edit()
            .putLong(USER_INFO_ID, id)
            .apply()
    }

    override fun setType(type: String) {
        context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
            .edit()
            .putString(USER_INFO_TYPE, type)
            .apply()
    }

    override fun getType(): String {
        return context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).getString(USER_INFO_TYPE, "").orEmpty()
    }

    override fun setToken(token: String) {
        context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
            .edit()
            .putString(USER_INFO_TOKEN, token)
            .apply()
    }

    override fun getToken(): String {
        return context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).getString(USER_INFO_TOKEN, "").orEmpty()
    }

    override fun setRefreshToken(refreshToken: String) {
        context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
            .edit()
            .putString(USER_INFO_REFRESH_TOKEN, refreshToken)
            .apply()
    }

    override fun getRefreshToken(): String {
        return context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).getString(USER_INFO_REFRESH_TOKEN, "").orEmpty()
    }

    override fun getRoles(): Set<String>? {
        return context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).getStringSet(USER_INFO_ROLES, null)
    }

    override fun setRoles(roles: Set<String>) {
        context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(USER_INFO_ROLES, roles)
            .apply()
    }

    override fun getDuration(): Long {
        return context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).getLong(USER_INFO_DURATION, 0L).or(0L)
    }

    override fun getLoginDt(): Long {
        return context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).getLong(USER_INFO_LOGIN_DT, 0L).or(0L)
    }

    override fun saveAll(refreshResponse: RefreshTokenResponse) {
        val editor = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit()
        editor.putString(USER_INFO_TYPE, refreshResponse.type)
        editor.putString(USER_INFO_TOKEN, refreshResponse.token)
        editor.putString(USER_INFO_REFRESH_TOKEN, refreshResponse.refreshToken)
        refreshResponse.tokenDurationMs?.let { editor.putLong(USER_INFO_DURATION, it) }
        editor.putLong(USER_INFO_LOGIN_DT, Date().time)
        editor.apply()
    }

    override fun saveAll(signingResponse: SigninResponse) {
        val editor = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit()
        signingResponse.id?.let { editor.putLong(USER_INFO_ID, it) }
        editor.putString(USER_INFO_TYPE, signingResponse.type)
        editor.putString(USER_INFO_TOKEN, signingResponse.token)
        editor.putString(USER_INFO_REFRESH_TOKEN, signingResponse.refreshToken)
        editor.putStringSet(USER_INFO_ROLES, signingResponse.roles)
        signingResponse.tokenDurationMs?.let { editor.putLong(USER_INFO_DURATION, it) }
        editor.putLong(USER_INFO_LOGIN_DT, Date().time)
        signingResponse.contactId?.let { editor.putLong(USER_INFO_CONTACT_ID, it) }
        editor.apply()
    }

    companion object {
        private const val USER_INFO = "UserInfo"
        private const val USER_INFO_LOGIN = "Username"
        private const val USER_INFO_PASSWORD = "Password"
        private const val USER_INFO_REMEMBER = "Remember"
        private const val USER_INFO_ID = "id"
        private const val USER_INFO_TYPE = "type"
        private const val USER_INFO_TOKEN = "token"
        private const val USER_INFO_REFRESH_TOKEN = "refreshToken"
        private const val USER_INFO_ROLES = "roles"
        private const val USER_INFO_DURATION = "tokenDurationMs"
        private const val USER_INFO_LOGIN_DT = "dateLoginLong"
        private const val USER_INFO_CONTACT_ID = "contactId"
    }
}