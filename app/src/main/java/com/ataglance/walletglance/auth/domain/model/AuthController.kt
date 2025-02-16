package com.ataglance.walletglance.auth.domain.model

import com.ataglance.walletglance.auth.data.model.UserContext
import com.ataglance.walletglance.auth.data.model.UserData
import com.ataglance.walletglance.auth.domain.usecase.ApplyOobCodeUseCase
import com.ataglance.walletglance.auth.domain.usecase.CreateNewUserUseCase
import com.ataglance.walletglance.auth.domain.usecase.DeleteUserUseCase
import com.ataglance.walletglance.auth.domain.usecase.GetUserDataUseCase
import com.ataglance.walletglance.auth.domain.usecase.GetUserEmailUseCase
import com.ataglance.walletglance.auth.domain.usecase.RequestEmailUpdateUseCase
import com.ataglance.walletglance.auth.domain.usecase.RequestPasswordResetUseCase
import com.ataglance.walletglance.auth.domain.usecase.SendEmailVerificationEmailUseCase
import com.ataglance.walletglance.auth.domain.usecase.SetNewPasswordUseCase
import com.ataglance.walletglance.auth.domain.usecase.SignInUseCase
import com.ataglance.walletglance.auth.domain.usecase.SignOutUseCase
import com.ataglance.walletglance.auth.domain.usecase.UpdatePasswordUseCase
import com.ataglance.walletglance.billing.domain.model.AppSubscription
import com.ataglance.walletglance.core.domain.usecase.DeleteAllDataLocallyUseCase
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.ataglance.walletglance.settings.domain.usecase.ApplyLanguageToSystemUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetUserIdPreferenceUseCase
import com.ataglance.walletglance.settings.domain.usecase.SaveLanguagePreferenceUseCase
import com.ataglance.walletglance.settings.domain.usecase.SaveUserIdPreferenceUseCase

class AuthController(
    private val userContext: UserContext,

    private val getUserEmailUseCase: GetUserEmailUseCase,
    private val applyOobCodeUseCase: ApplyOobCodeUseCase,
    private val createNewUserUseCase: CreateNewUserUseCase,
    private val signInUseCase: SignInUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val sendEmailVerificationEmailUseCase: SendEmailVerificationEmailUseCase,
    private val requestEmailUpdateUseCase: RequestEmailUpdateUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val requestPasswordResetUseCase: RequestPasswordResetUseCase,
    private val setNewPasswordUseCase: SetNewPasswordUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val signOutUseCase: SignOutUseCase,

    private val saveUserIdPreferenceUseCase: SaveUserIdPreferenceUseCase,
    private val getUserIdPreferenceUseCase: GetUserIdPreferenceUseCase,
    private val applyLanguageToSystemUseCase: ApplyLanguageToSystemUseCase,
    private val saveLanguagePreferenceUseCase: SaveLanguagePreferenceUseCase,
    private val deleteAllDataLocallyUseCase: DeleteAllDataLocallyUseCase
) {

    private suspend fun saveUserId(userId: String) {
        userContext.setUserId(userId)
        saveUserIdPreferenceUseCase.save(userId)
    }

    fun setUserSubscription(subscription: AppSubscription) {
        userContext.setSubscription(subscription)
    }

    private fun setUserData(userData: UserData) {
        userContext.setUserId(userData.userId)
        userContext.setSubscription(userData.subscription)
    }

    suspend fun fetchUserDataAndUpdateUser() {
        val userId = getUserIdPreferenceUseCase.get() ?: return
        fetchUserDataAndUpdateUser(userId)
    }

    suspend fun fetchUserDataAndUpdateUser(userId: String) {
        val userData = getUserDataUseCase.get(userId).getDataIfSuccess() ?: return
        setUserData(userData)
    }

    fun resetUser() {
        userContext.resetUser()
    }

    fun isSignedIn(): Boolean = userContext.isSignedIn()

    fun getUserId(): String? = userContext.getUserId()

    fun getEmail(): String = getUserEmailUseCase.execute() ?: ""


    suspend fun applyOobCode(obbCode: String): Boolean {
        return applyOobCodeUseCase.execute(obbCode)
    }

    suspend fun createNewUser(
        email: String,
        password: String,
        appLanguageCode: String
    ): Result<AuthSuccess, AuthError> {
        return when (val result = createNewUserUseCase.execute(email, password, appLanguageCode)) {
            is ResultData.Success -> sendEmailVerificationEmail()
            is ResultData.Error -> Result.Error(result.error)
        }
    }

    suspend fun signIn(email: String, password: String): Result<AuthSuccess?, AuthError> {
        val userInstance = when (val result = signInUseCase.execute(email, password)) {
            is ResultData.Success -> result.data
            is ResultData.Error -> return Result.Error(result.error)
        }

        return when (val result = getUserDataUseCase.get(userInstance.uid)) {
            is ResultData.Success -> {
                saveLanguagePreferenceUseCase.save(result.data.language)
                applyLanguageToSystemUseCase.execute(result.data.language)
                if (userInstance.isEmailVerified) {
                    saveUserId(result.data.userId)
                    Result.Success(null)
                } else {
                    sendEmailVerificationEmail()
                }
            }
            is ResultData.Error -> Result.Error(result.error)
        }
    }

    private suspend fun sendEmailVerificationEmail(): AuthResult {
        return sendEmailVerificationEmailUseCase.execute()
    }

    suspend fun requestEmailUpdate(password: String, newEmail: String): AuthResult {
        return requestEmailUpdateUseCase.execute(password, newEmail)
    }

    suspend fun updatePassword(currentPassword: String, newPassword: String): AuthResult {
        return updatePasswordUseCase.execute(currentPassword, newPassword)
    }

    suspend fun requestPasswordReset(email: String): AuthResult {
        return requestPasswordResetUseCase.execute(email)
    }

    suspend fun setNewPassword(obbCode: String, newPassword: String): AuthResult {
        return setNewPasswordUseCase.execute(obbCode, newPassword)
    }

    suspend fun signOut() {
        saveUserIdPreferenceUseCase.save("")
        signOutUseCase.execute()
        resetUser()
    }

    suspend fun deleteAccount(password: String): AuthResult {
        val result = deleteUserUseCase.execute(password)

//        if (result is Result.Success) {
//            resetUser()
//        }
        return result
    }

    suspend fun deleteAllLocalData() {
        deleteAllDataLocallyUseCase.execute()
    }

}