package ru.gilgamesh.abon.motot.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import ru.gilgamesh.abon.motot.domain.repositories.AuthRepository
import ru.gilgamesh.abon.motot.payload.response.auth.RefreshTokenResponse
import ru.gilgamesh.abon.motot.payload.response.auth.SigninResponse
import java.util.Date

class AuthRepositoryImpl (private val sharedPreferences: SharedPreferences): AuthRepository{
    override fun setLogin(login: String) {
        sharedPreferences
            .edit {
                putString(USER_INFO_LOGIN, login)
            }
    }

    override fun setPassword(password: String) {
        sharedPreferences
            .edit {
                putString(USER_INFO_PASSWORD, password)
            }
    }

    override fun getRemember(): Boolean {
        return sharedPreferences.getBoolean(USER_INFO_REMEMBER, false)
    }

    override fun setRemember(remember: Boolean) {
        sharedPreferences
            .edit {
                putBoolean(USER_INFO_REMEMBER, remember)
            }
    }

    override fun setId(id: Long) {
        sharedPreferences
            .edit {
                putLong(USER_INFO_ID, id)
            }
    }

    override fun setType(type: String) {
        sharedPreferences
            .edit {
                putString(USER_INFO_TYPE, type)
            }
    }

    override fun getType(): String {
        return sharedPreferences.getString(USER_INFO_TYPE, "").orEmpty()
    }

    override fun setToken(token: String) {
        sharedPreferences
            .edit {
                putString(USER_INFO_TOKEN, token)
            }
    }

    override fun getToken(): String {
        return sharedPreferences.getString(USER_INFO_TOKEN, "").orEmpty()
    }

    override fun setRefreshToken(refreshToken: String) {
        sharedPreferences
            .edit {
                putString(USER_INFO_REFRESH_TOKEN, refreshToken)
            }
    }

    override fun getRefreshToken(): String {
        return sharedPreferences.getString(USER_INFO_REFRESH_TOKEN, "").orEmpty()
    }

    override fun getRoles(): Set<String>? {
        return sharedPreferences.getStringSet(USER_INFO_ROLES, null)
    }

    override fun setRoles(roles: Set<String>) {
        sharedPreferences
            .edit {
                putStringSet(USER_INFO_ROLES, roles)
            }
    }

    override fun getDuration(): Long {
        return sharedPreferences.getLong(USER_INFO_DURATION, 0L).or(0L)
    }

    override fun getLoginDt(): Long {
        return sharedPreferences.getLong(USER_INFO_LOGIN_DT, 0L).or(0L)
    }

    override fun saveAll(refreshResponse: RefreshTokenResponse) {
        sharedPreferences.edit {
            putString(USER_INFO_TYPE, refreshResponse.type)
            putString(USER_INFO_TOKEN, refreshResponse.token)
            putString(USER_INFO_REFRESH_TOKEN, refreshResponse.refreshToken)
            refreshResponse.tokenDurationMs?.let { putLong(USER_INFO_DURATION, it) }
            putLong(USER_INFO_LOGIN_DT, Date().time)
        }
    }

    override fun saveAll(signingResponse: SigninResponse) {
        sharedPreferences.edit {
            signingResponse.id?.let { putLong(USER_INFO_ID, it) }
            putString(USER_INFO_TYPE, signingResponse.type)
            putString(USER_INFO_TOKEN, signingResponse.token)
            putString(USER_INFO_REFRESH_TOKEN, signingResponse.refreshToken)
            putStringSet(USER_INFO_ROLES, signingResponse.roles)
            signingResponse.tokenDurationMs?.let { putLong(USER_INFO_DURATION, it) }
            putLong(USER_INFO_LOGIN_DT, Date().time)
            signingResponse.contactId?.let { putLong(USER_INFO_CONTACT_ID, it) }
        }
    }

    companion object {
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