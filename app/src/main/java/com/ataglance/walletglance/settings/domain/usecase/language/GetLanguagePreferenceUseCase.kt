package com.ataglance.walletglance.settings.domain.usecase.language

import kotlinx.coroutines.flow.Flow

interface GetLanguagePreferenceUseCase {

    fun getFlow(): Flow<String>

    suspend fun get(): String

}