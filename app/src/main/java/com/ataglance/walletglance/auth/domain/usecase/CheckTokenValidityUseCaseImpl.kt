package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.domain.model.UserContext
import com.ataglance.walletglance.auth.mapper.toDomainModel
import com.ataglance.walletglance.core.data.local.preferences.SecureStorage
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.ataglance.walletglance.settings.domain.usecase.GetUserProfileLocalTimestampUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageLocallyUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageRemotelyUseCaseImpl

class CheckTokenValidityUseCaseImpl(
    private val secureStorage: SecureStorage,
    private val authRepository: AuthRepository,
    private val userContext: UserContext,
    private val getUserProfileLocalTimestampUseCase: GetUserProfileLocalTimestampUseCase,
    private val saveLanguageLocallyUseCase: SaveLanguageLocallyUseCase,
    private val saveLanguagePreferenceRemotelyUseCaseImpl: SaveLanguageRemotelyUseCaseImpl
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
                langCode = user.language.languageCode
            )
        }

        return result.mapDataToUnit()
    }

    private suspend fun syncDataIfRequired(
        remoteTimestamp: Long,
        langCode: String
    ) {
        val localTimestamp = getUserProfileLocalTimestampUseCase.get()
        if (remoteTimestamp == localTimestamp) return

        if (remoteTimestamp > localTimestamp) {
            saveLanguageLocallyUseCase.execute(langCode = langCode, timestamp = remoteTimestamp)
        } else {
            saveLanguagePreferenceRemotelyUseCaseImpl.execute(
                langCode = langCode, timestamp = localTimestamp
            )
        }
    }

}