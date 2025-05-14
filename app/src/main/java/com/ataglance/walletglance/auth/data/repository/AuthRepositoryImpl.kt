package com.ataglance.walletglance.auth.data.repository

import com.ataglance.walletglance.auth.data.model.SignUpFormDto
import com.ataglance.walletglance.auth.data.model.UserCredentialsDto
import com.ataglance.walletglance.auth.data.model.UserDto
import com.ataglance.walletglance.auth.data.model.UserWithTokenDto
import com.ataglance.walletglance.auth.domain.model.UserContext
import com.ataglance.walletglance.core.data.remote.glanciBackendUrl
import com.ataglance.walletglance.core.data.remote.httpClient
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

class AuthRepositoryImpl(
    private val userContext: UserContext
) : AuthRepository {

    override suspend fun signInWithEmailAndPassword(
        userCredentials: UserCredentialsDto
    ): ResultData<UserWithTokenDto, AuthError> {
        val response = try {
            httpClient.post(
                urlString = "$glanciBackendUrl/auth/sign-in"
            ) {
                contentType(ContentType.Application.Json)
                setBody(userCredentials)
            }
        } catch (_: Exception) {
            return ResultData.Error(AuthError.SignInError)
        }

        return when (response.status) {
            HttpStatusCode.OK -> ResultData.Success(
                data = Json.decodeFromString<UserWithTokenDto>(string = response.bodyAsText())
            )
            HttpStatusCode.BadRequest -> ResultData.Error(AuthError.DataNotValid)
            HttpStatusCode.Unauthorized -> ResultData.Error(AuthError.InvalidCredentials)
            HttpStatusCode.ExpectationFailed -> ResultData.Error(AuthError.EmailNotVerified)
            HttpStatusCode.NotFound -> ResultData.Error(AuthError.UserNotFound)
            HttpStatusCode.InternalServerError -> ResultData.Error(AuthError.SignInError)
            else -> ResultData.Error(AuthError.SignInError)
        }
    }

    override suspend fun signUpWithEmailAndPassword(
        signUpForm: SignUpFormDto
    ): Result<AuthSuccess, AuthError> {
        val response = try {
            httpClient.post(
                urlString = "$glanciBackendUrl/auth/sign-up"
            ) {
                contentType(ContentType.Application.Json)
                setBody(signUpForm)
            }
        } catch (_: Exception) {
            return Result.Error(AuthError.SignUpError)
        }

        return when (response.status) {
            HttpStatusCode.Accepted -> Result.Success(AuthSuccess.SignUpEmailVerificationSent)
            HttpStatusCode.BadRequest -> Result.Error(AuthError.DataNotValid)
            HttpStatusCode.Conflict -> Result.Error(AuthError.UserAlreadyExists)
            HttpStatusCode.ServiceUnavailable -> Result.Error(AuthError.SignUpEmailVerificationError)
            HttpStatusCode.InternalServerError -> Result.Error(AuthError.SignUpError)
            else -> Result.Error(AuthError.SignUpError)
        }
    }

    override suspend fun checkEmailVerification(
        signUpForm: SignUpFormDto
    ): ResultData<UserWithTokenDto, AuthError> {
        val response = try {
            httpClient.post(
                urlString = "$glanciBackendUrl/auth/check-email-verification"
            ) {
                contentType(ContentType.Application.Json)
                setBody(signUpForm)
            }
        } catch (_: Exception) {
            return ResultData.Error(AuthError.CheckEmailVerificationError)
        }

        return when (response.status) {
            HttpStatusCode.OK -> ResultData.Success(
                data = Json.decodeFromString<UserWithTokenDto>(string = response.bodyAsText())
            )
            HttpStatusCode.BadRequest -> ResultData.Error(AuthError.DataNotValid)
            HttpStatusCode.Unauthorized -> ResultData.Error(AuthError.InvalidCredentials)
            HttpStatusCode.ExpectationFailed -> ResultData.Error(AuthError.EmailNotVerified)
            HttpStatusCode.NotFound -> ResultData.Error(AuthError.UserNotFound)
            HttpStatusCode.InternalServerError -> ResultData.Error(AuthError.CheckEmailVerificationError)
            else -> ResultData.Error(AuthError.CheckEmailVerificationError)
        }
    }

    override suspend fun finishSignUpWithEmailAndPassword(
        oobCode: String
    ): ResultData<UserWithTokenDto, AuthError> {
        val response = try {
            httpClient.get(
                urlString = "$glanciBackendUrl/auth/finish-sign-up/$oobCode"
            )
        } catch (_: Exception) {
            return ResultData.Error(AuthError.FinishSignUpError)
        }

        return when (response.status) {
            HttpStatusCode.OK -> ResultData.Success(
                data = Json.decodeFromString<UserWithTokenDto>(string = response.bodyAsText())
            )
            HttpStatusCode.BadRequest -> ResultData.Error(AuthError.OobCodeIsInvalid)
            HttpStatusCode.InternalServerError -> ResultData.Error(AuthError.FinishSignUpError)
            else -> ResultData.Error(AuthError.FinishSignUpError)
        }
    }

    override suspend fun checkTokenValidity(): ResultData<UserDto, AuthError> {
        val token = userContext.getAuthToken() ?: return ResultData.Error(AuthError.UserNotSignedIn)

        val response = try {
            httpClient.get(
                urlString = "$glanciBackendUrl/auth/check-token-validity"
            ) {
                header("Authorization", "Bearer $token")
            }
        } catch (_: Exception) {
            return ResultData.Error(AuthError.ReauthenticationError)
        }

        return when (response.status) {
            HttpStatusCode.OK -> ResultData.Success(
                data = Json.decodeFromString<UserDto>(string = response.bodyAsText())
            )
            HttpStatusCode.Unauthorized -> ResultData.Error(AuthError.InvalidCredentials)
            HttpStatusCode.Forbidden -> ResultData.Error(AuthError.ReauthenticationError)
            HttpStatusCode.NotFound -> ResultData.Error(AuthError.UserNotFound)
            HttpStatusCode.InternalServerError -> ResultData.Error(AuthError.ReauthenticationError)
            else -> ResultData.Error(AuthError.ReauthenticationError)
        }
    }

}