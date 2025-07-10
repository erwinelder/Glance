package com.ataglance.walletglance.auth.data.repository

import com.ataglance.walletglance.auth.data.model.EmailUpdateRequestDto
import com.ataglance.walletglance.auth.data.model.ResetPasswordRequestDto
import com.ataglance.walletglance.auth.data.model.SaveLanguageRequestDto
import com.ataglance.walletglance.auth.data.model.SignUpFormDto
import com.ataglance.walletglance.auth.data.model.UpdatePasswordRequestDto
import com.ataglance.walletglance.auth.data.model.UserCredentialsDto
import com.ataglance.walletglance.auth.data.model.UserDto
import com.ataglance.walletglance.auth.data.model.UserWithTokenDto
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.request.domain.model.result.Error
import com.ataglance.walletglance.request.domain.model.result.Result
import com.ataglance.walletglance.request.domain.model.result.ResultData

interface AuthRepository {

    suspend fun checkTokenValidity(token: String): ResultData<UserDto, AuthError>

    suspend fun signInWithEmailAndPassword(
        userCredentials: UserCredentialsDto
    ): ResultData<UserWithTokenDto, AuthError>

    suspend fun signUpWithEmailAndPassword(
        signUpForm: SignUpFormDto
    ): Result<AuthSuccess, AuthError>

    suspend fun checkEmailVerification(
        signUpForm: SignUpFormDto
    ): ResultData<UserWithTokenDto, AuthError>

    suspend fun finishSignUpWithEmailAndPassword(
        oobCode: String
    ): ResultData<UserWithTokenDto, AuthError>

    suspend fun requestEmailUpdate(
        emailUpdateRequest: EmailUpdateRequestDto
    ): Result<AuthSuccess, AuthError>

    suspend fun verifyEmailUpdate(oobCode: String): ResultData<UserWithTokenDto, AuthError>

    suspend fun updatePassword(
        updatePasswordRequest: UpdatePasswordRequestDto
    ): Result<AuthSuccess, AuthError>

    suspend fun requestPasswordReset(email: String): Result<AuthSuccess, AuthError>

    suspend fun verifyPasswordReset(
        resetPasswordRequest: ResetPasswordRequestDto
    ): Result<AuthSuccess, AuthError>

    suspend fun deleteAccount(userCredentials: UserCredentialsDto): Result<AuthSuccess, AuthError>


    suspend fun saveLanguage(
        saveLanguageRequest: SaveLanguageRequestDto
    ): ResultData<Unit, Error>

}