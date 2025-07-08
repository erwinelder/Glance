package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.data.model.SignUpFormDto
import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.settings.domain.usecase.language.GetLanguagePreferenceUseCase

class SignUpUseCaseImpl(
    private val authRepository: AuthRepository,
    private val getLanguagePreferenceUseCase: GetLanguagePreferenceUseCase
) : SignUpUseCase {
    override suspend fun execute(
        name: String,
        email: String,
        password: String
    ): Result<AuthSuccess, AuthError> {
        val langCode = getLanguagePreferenceUseCase.get()
        val form = SignUpFormDto(
            name = name,
            email = email,
            password = password,
            langCode = langCode
        )
        return authRepository.signUpWithEmailAndPassword(signUpForm = form)
    }
}