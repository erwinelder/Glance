package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.auth.mapper.toDomainModel
import com.ataglance.walletglance.core.data.local.preferences.SecureStorage
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.ataglance.walletglance.settings.domain.usecase.GetUserProfileLocalTimestampUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.GetLanguagePreferenceUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageLocallyUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageRemotelyUseCase

class CheckTokenValidityUseCaseImpl(
    private val secureStorage: SecureStorage,
    private val authRepository: AuthRepository,
    private val userContext: UserContext,
    private val getUserProfileLocalTimestampUseCase: GetUserProfileLocalTimestampUseCase,
    private val getLanguagePreferenceUseCase: GetLanguagePreferenceUseCase,
    private val saveLanguageLocallyUseCase: SaveLanguageLocallyUseCase,
    private val saveLanguagePreferenceRemotelyUseCase: SaveLanguageRemotelyUseCase
) : CheckTokenValidityUseCase {

    override suspend fun execute(): ResultData<Unit, AuthError> {
        val token = secureStorage.getAuthToken()
            ?: return ResultData.Error(AuthError.UserNotSignedIn)
        val result = authRepository.checkTokenValidity(token = token)

        result.getDataIfSuccess()?.let { data ->
            val user = data.toDomainModel()
                ?: return ResultData.Error(AuthError.RequestDataNotValid)

            userContext.saveUser(user = user)
            syncDataIfRequired(
                remoteTimestamp = data.timestamp,
                remoteLangCode = user.language.languageCode
            )
        }

        return result.mapDataToUnit()
    }

    private suspend fun syncDataIfRequired(
        remoteTimestamp: Long,
        remoteLangCode: String
    ) {
        val localTimestamp = getUserProfileLocalTimestampUseCase.get()
        if (remoteTimestamp == localTimestamp) return

        if (remoteTimestamp > localTimestamp) {
            saveLanguageLocallyUseCase.execute(
                langCode = remoteLangCode, timestamp = remoteTimestamp
            )
        } else {
            val localLangCode = getLanguagePreferenceUseCase.get()
            saveLanguagePreferenceRemotelyUseCase.execute(
                langCode = localLangCode, timestamp = localTimestamp
            )
        }
    }

}