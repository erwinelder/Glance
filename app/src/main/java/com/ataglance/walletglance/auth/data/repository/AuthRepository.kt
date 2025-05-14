package com.ataglance.walletglance.auth.data.repository

import com.ataglance.walletglance.auth.data.model.SignUpFormDto
import com.ataglance.walletglance.auth.data.model.UserCredentialsDto
import com.ataglance.walletglance.auth.data.model.UserDto
import com.ataglance.walletglance.auth.data.model.UserWithTokenDto
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData

interface AuthRepository {

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

    suspend fun checkTokenValidity(): ResultData<UserDto, AuthError>

}