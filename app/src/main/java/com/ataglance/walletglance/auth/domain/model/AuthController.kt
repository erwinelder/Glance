package com.ataglance.walletglance.auth.domain.model

import com.ataglance.walletglance.auth.data.model.UserRemotePreferences
import com.ataglance.walletglance.auth.domain.usecase.ApplyOobCodeUseCase
import com.ataglance.walletglance.auth.domain.usecase.CreateNewUserUseCase
import com.ataglance.walletglance.auth.domain.usecase.DeleteUserUseCase
import com.ataglance.walletglance.auth.domain.usecase.GetUserEmailUseCase
import com.ataglance.walletglance.auth.domain.usecase.GetUserRemotePreferencesUseCase
import com.ataglance.walletglance.auth.domain.usecase.RequestEmailUpdateUseCase
import com.ataglance.walletglance.auth.domain.usecase.RequestPasswordResetUseCase
import com.ataglance.walletglance.auth.domain.usecase.SendEmailVerificationEmailUseCase
import com.ataglance.walletglance.auth.domain.usecase.SetNewPasswordUseCase
import com.ataglance.walletglance.auth.domain.usecase.SignInUseCase
import com.ataglance.walletglance.auth.domain.usecase.SignOutUseCase
import com.ataglance.walletglance.auth.domain.usecase.UpdatePasswordUseCase
import com.ataglance.walletglance.auth.domain.usecase.UserEmailIsVerifiedUseCase
import com.ataglance.walletglance.billing.domain.model.AppSubscription
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthController(
    private val getUserEmailUseCase: GetUserEmailUseCase,
    private val userEmailIsVerifiedUseCase: UserEmailIsVerifiedUseCase,
    private val applyOobCodeUseCase: ApplyOobCodeUseCase,
    private val createNewUserUseCase: CreateNewUserUseCase,
    private val signInUseCase: SignInUseCase,
    private val getUserRemotePreferencesUseCase: GetUserRemotePreferencesUseCase,
    private val sendEmailVerificationEmailUseCase: SendEmailVerificationEmailUseCase,
    private val requestEmailUpdateUseCase: RequestEmailUpdateUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val requestPasswordResetUseCase: RequestPasswordResetUseCase,
    private val setNewPasswordUseCase: SetNewPasswordUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val signOutUseCase: SignOutUseCase
) {

    private val _userState: MutableStateFlow<User> = MutableStateFlow(User())
    val userState = _userState.asStateFlow()

    fun getUser(): User = userState.value

    fun setUser(firebaseUser: FirebaseUser) {
        _userState.update { User(uid = firebaseUser.uid) }
    }

    fun setUserId(userId: String) {
        _userState.update { it.copy(uid = userId) }
    }

    fun setUserSubscription(subscription: AppSubscription) {
        _userState.update { it.copy(subscription = subscription) }
    }

    private fun resetUser() {
        _userState.update { User() }
    }

    fun isSignedIn(): Boolean = getUser().isSignedIn()

    fun getUserId(): String? = getUser().uid

    fun getEmail(): String = getUserEmailUseCase.execute() ?: ""

    fun emailIsVerified(): Boolean = userEmailIsVerifiedUseCase.execute()


    suspend fun applyOobCode(obbCode: String): Boolean {
        return applyOobCodeUseCase.execute(obbCode)
    }

    suspend fun createNewUser(
        email: String,
        password: String,
        appLanguageCode: String
    ): ResultData<String, AuthError> {
        return when (val result = createNewUserUseCase.execute(email, password, appLanguageCode)) {
            is ResultData.Success -> {
                setUser(result.data)
                ResultData.Success(result.data.uid)
            }
            is ResultData.Error -> ResultData.Error(result.error)
        }
    }

    suspend fun signIn(
        email: String,
        password: String
    ): ResultData<UserRemotePreferences?, AuthError> {
        val userInstanceResult = signInUseCase.execute(email, password)
        val userInstance = userInstanceResult.getDataIfSuccess()
            ?: return ResultData.Error((userInstanceResult as ResultData.Error).error)

        setUser(userInstance)

        return if (userInstance.isEmailVerified) {
            getUserRemotePreferencesUseCase.execute(userInstance.uid)
        } else {
            ResultData.Success(null)
        }
    }

    suspend fun sendEmailVerificationEmail(): AuthResult {
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

    fun signOut() {
        signOutUseCase.execute()
        resetUser()
    }

    suspend fun deleteAccount(password: String): AuthResult {
        val result = deleteUserUseCase.execute(password)

        if (result is Result.Success) {
            resetUser()
        }
        return result
    }

}