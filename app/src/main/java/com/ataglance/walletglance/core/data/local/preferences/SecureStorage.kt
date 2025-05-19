package com.ataglance.walletglance.core.data.local.preferences

import com.russhwolf.settings.Settings

class SecureStorage(
    private val settings: Settings
) {

    companion object {

        private const val AUTH_TOKEN_KEY = "auth_token"

    }


    fun getAuthToken(): String? = settings.getStringOrNull(key = AUTH_TOKEN_KEY)
    fun saveAuthToken(token: String) = settings.putString(key = AUTH_TOKEN_KEY, value = token)
    fun deleteAuthToken() = settings.remove(AUTH_TOKEN_KEY)


    fun clear() {
        settings.remove(AUTH_TOKEN_KEY)
    }

}