package com.ataglance.walletglance.settings.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetLanguagePreferenceUseCase {

    fun getFlow(): Flow<String>

    suspend fun get(): String

}