package com.ataglance.walletglance.settings.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetUserIdPreferenceUseCase {

    fun getAsFlow(): Flow<String?>

    suspend fun get(): String?

}