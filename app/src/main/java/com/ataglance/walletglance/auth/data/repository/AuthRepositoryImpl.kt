package com.ataglance.walletglance.auth.data.repository

import com.ataglance.walletglance.auth.data.model.CheckAppVersionRequestDto
import com.ataglance.walletglance.auth.data.model.EmailUpdateRequestDto
import com.ataglance.walletglance.auth.data.model.ResetPasswordRequestDto
import com.ataglance.walletglance.auth.data.model.SaveLanguageRequestDto
import com.ataglance.walletglance.auth.data.model.SignUpFormDto
import com.ataglance.walletglance.auth.data.model.UpdatePasswordRequestDto
import com.ataglance.walletglance.auth.data.model.UserCredentialsDto
import com.ataglance.walletglance.auth.data.model.UserDto
import com.ataglance.walletglance.auth.data.model.UserWithTokenDto
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.core.data.remote.glanciBackendUrl
import com.ataglance.walletglance.core.data.remote.httpClient
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
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

    override suspend fun checkTokenValidity(token: String): ResultData<UserDto, AuthError> {
        val appVersion = CheckAppVersionRequestDto(4, 2, 0)

        val response = try {
            httpClient.post(
                urlString = "$glanciBackendUrl/auth/check-token-validity"
            ) {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(appVersion)
            }
        } catch (_: Exception) {
            return ResultData.Error(AuthError.ReauthenticationError)
        }

        return when (response.status) {
            HttpStatusCode.OK -> ResultData.Success(
                data = Json.decodeFromString<UserDto>(string = response.bodyAsText())
            )
            HttpStatusCode.BadRequest -> ResultData.Error(AuthError.RequestDataNotValid)
            HttpStatusCode.Unauthorized -> ResultData.Error(AuthError.SessionExpired)
            HttpStatusCode.Forbidden -> ResultData.Error(AuthError.InvalidCredentials)
            HttpStatusCode.UpgradeRequired -> ResultData.Error(AuthError.AppUpdateRequired)
            HttpStatusCode.NotFound -> ResultData.Error(AuthError.UserNotFound)
            HttpStatusCode.InternalServerError -> ResultData.Error(AuthError.ReauthenticationError)
            else -> ResultData.Error(AuthError.ReauthenticationError)
        }
    }

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
            HttpStatusCode.BadRequest -> ResultData.Error(AuthError.RequestDataNotValid)
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
            HttpStatusCode.BadRequest -> Result.Error(AuthError.RequestDataNotValid)
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
            HttpStatusCode.BadRequest -> ResultData.Error(AuthError.RequestDataNotValid)
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
            ) {
                contentType(ContentType.Application.Json)
            }
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

    override suspend fun requestEmailUpdate(
        emailUpdateRequest: EmailUpdateRequestDto
    ): Result<AuthSuccess, AuthError> {
        val token = userContext.getAuthToken() ?: return Result.Error(AuthError.UserNotSignedIn)

        val response = try {
            httpClient.post(
                urlString = "$glanciBackendUrl/auth/request-email-update"
            ) {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(emailUpdateRequest)
            }
        } catch (_: Exception) {
            return Result.Error(AuthError.RequestEmailUpdateError)
        }

        return when (response.status) {
            HttpStatusCode.Accepted -> Result.Success(AuthSuccess.UpdateEmailEmailVerificationSent)
            HttpStatusCode.Unauthorized -> Result.Error(AuthError.SessionExpired)
            HttpStatusCode.BadRequest -> Result.Error(AuthError.RequestDataNotValid)
            HttpStatusCode.NotFound -> Result.Error(AuthError.UserNotFound)
            HttpStatusCode.ExpectationFailed -> Result.Error(AuthError.EmailNotVerified)
            HttpStatusCode.InternalServerError -> Result.Error(AuthError.RequestEmailUpdateError)
            else -> Result.Error(AuthError.RequestEmailUpdateError)
        }
    }

    override suspend fun verifyEmailUpdate(
        oobCode: String
    ): ResultData<UserWithTokenDto, AuthError> {
        val token = userContext.getAuthToken() ?: return ResultData.Error(AuthError.UserNotSignedIn)

        val response = try {
            httpClient.get(
                urlString = "$glanciBackendUrl/auth/verify-email-update/$oobCode"
            ) {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }
        } catch (_: Exception) {
            return ResultData.Error(AuthError.EmailUpdateError)
        }

        return when (response.status) {
            HttpStatusCode.OK -> ResultData.Success(
                data = Json.decodeFromString<UserWithTokenDto>(string = response.bodyAsText())
            )
            HttpStatusCode.Unauthorized -> ResultData.Error(AuthError.SessionExpired)
            HttpStatusCode.BadRequest -> ResultData.Error(AuthError.OobCodeIsInvalid)
            HttpStatusCode.NotFound -> ResultData.Error(AuthError.UserNotFound)
            HttpStatusCode.InternalServerError -> ResultData.Error(AuthError.EmailUpdateError)
            else -> ResultData.Error(AuthError.EmailUpdateError)
        }
    }

    override suspend fun updatePassword(
        updatePasswordRequest: UpdatePasswordRequestDto
    ): Result<AuthSuccess, AuthError> {
        val token = userContext.getAuthToken() ?: return Result.Error(AuthError.UserNotSignedIn)

        val response = try {
            httpClient.post(
                urlString = "$glanciBackendUrl/auth/update-password"
            ) {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(updatePasswordRequest)
            }
        } catch (_: Exception) {
            return Result.Error(AuthError.PasswordUpdateError)
        }

        return when (response.status) {
            HttpStatusCode.OK -> Result.Success(AuthSuccess.PasswordUpdated)
            HttpStatusCode.BadRequest -> Result.Error(AuthError.RequestDataNotValid)
            HttpStatusCode.NotFound -> Result.Error(AuthError.UserNotFound)
            HttpStatusCode.Unauthorized -> Result.Error(AuthError.InvalidCredentials)
            HttpStatusCode.InternalServerError -> Result.Error(AuthError.PasswordUpdateError)
            else -> Result.Error(AuthError.PasswordUpdateError)
        }
    }

    override suspend fun requestPasswordReset(email: String): Result<AuthSuccess, AuthError> {
        val response = try {
            httpClient.get(
                urlString = "$glanciBackendUrl/auth/request-password-reset/$email"
            )
        } catch (_: Exception) {
            return Result.Error(AuthError.ResetPasswordRequestError)
        }

        return when (response.status) {
            HttpStatusCode.Accepted -> Result.Success(AuthSuccess.ResetPasswordEmailSent)
            HttpStatusCode.BadRequest -> Result.Error(AuthError.RequestDataNotValid)
            HttpStatusCode.InternalServerError -> Result.Error(AuthError.ResetPasswordRequestError)
            else -> Result.Error(AuthError.ResetPasswordRequestError)
        }
    }

    override suspend fun verifyPasswordReset(
        resetPasswordRequest: ResetPasswordRequestDto
    ): Result<AuthSuccess, AuthError> {
        val response = try {
            httpClient.post(
                urlString = "$glanciBackendUrl/auth/verify-password-reset"
            ) {
                contentType(ContentType.Application.Json)
                setBody(resetPasswordRequest)
            }
        } catch (_: Exception) {
            return Result.Error(AuthError.PasswordResetError)
        }

        return when (response.status) {
            HttpStatusCode.OK -> Result.Success(AuthSuccess.PasswordUpdated)
            HttpStatusCode.BadRequest -> Result.Error(AuthError.RequestDataNotValid)
            HttpStatusCode.InternalServerError -> Result.Error(AuthError.PasswordResetError)
            else -> Result.Error(AuthError.PasswordResetError)
        }
    }

    override suspend fun deleteAccount(
        userCredentials: UserCredentialsDto
    ): Result<AuthSuccess, AuthError> {
        val token = userContext.getAuthToken() ?: return Result.Error(AuthError.UserNotSignedIn)

        val response = try {
            httpClient.post(
                urlString = "$glanciBackendUrl/auth/delete-account"
            ) {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(userCredentials)
            }
        } catch (_: Exception) {
            return Result.Error(AuthError.AccountNotDeleted)
        }

        return when (response.status) {
            HttpStatusCode.NoContent -> Result.Success(AuthSuccess.AccountDeleted)
            HttpStatusCode.BadRequest -> Result.Error(AuthError.RequestDataNotValid)
            HttpStatusCode.Unauthorized -> Result.Error(AuthError.InvalidCredentials)
            HttpStatusCode.InternalServerError -> Result.Error(AuthError.AccountNotDeleted)
            else -> Result.Error(AuthError.AccountNotDeleted)
        }
    }


    override suspend fun saveLanguage(
        saveLanguageRequest: SaveLanguageRequestDto
    ): ResultData<Unit, AuthError> {
        val token = userContext.getAuthToken() ?: return ResultData.Error(AuthError.UserNotSignedIn)

        val response = try {
            httpClient.post(
                urlString = "$glanciBackendUrl/auth/save-language"
            ) {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(saveLanguageRequest)
            }
        } catch (_: Exception) {
            return ResultData.Error(AuthError.LanguageNotSaved)
        }

        return when (response.status) {
            HttpStatusCode.OK -> ResultData.Success(Unit)
            HttpStatusCode.BadRequest -> ResultData.Error(AuthError.RequestDataNotValid)
            HttpStatusCode.Unauthorized -> ResultData.Error(AuthError.InvalidCredentials)
            HttpStatusCode.InternalServerError -> ResultData.Error(AuthError.LanguageNotSaved)
            else -> ResultData.Error(AuthError.LanguageNotSaved)
        }
    }

}