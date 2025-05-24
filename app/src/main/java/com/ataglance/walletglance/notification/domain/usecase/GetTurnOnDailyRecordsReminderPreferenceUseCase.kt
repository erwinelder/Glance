package com.ataglance.walletglance.notification.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetTurnOnDailyRecordsReminderPreferenceUseCase {

    fun getAsFlow(): Flow<Boolean>

    suspend fun get(): Boolean

}