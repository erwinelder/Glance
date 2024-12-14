package com.ataglance.walletglance.auth.domain.model

import com.ataglance.walletglance.auth.data.model.UserRemotePreferences
import com.ataglance.walletglance.auth.data.repository.UserRepository
import com.ataglance.walletglance.billing.domain.model.AppSubscription
import com.ataglance.walletglance.core.utils.takeIfNoneIsNull
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await

typealias AuthResult = Result<AuthSuccess, AuthError>

class AuthController(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) {

    private val _userState: MutableStateFlow<User> = MutableStateFlow(User())
    val userState = _userState.asStateFlow()

    fun getUser(): User = userState.value

    private fun setUser(firebaseUser: FirebaseUser) {
        _userState.update { User(uid = firebaseUser.uid) }
    }

    fun setUserSubscription(subscription: AppSubscription) {
        _userState.update { it.copy(subscription = subscription) }
    }

    private fun resetUser() {
        _userState.update { User() }
    }

    fun isSignedIn(): Boolean = getUser().isSignedIn()

    fun getUserId(): String? = getUser().uid

    fun getEmail(): String = auth.currentUser?.email ?: ""

    fun emailIsVerified(): Boolean = auth.currentUser?.isEmailVerified ?: false


    suspend fun applyObbCode(obbCode: String): Boolean {
        return try {
            auth.applyActionCode(obbCode).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun createNewUser(
        email: String,
        password: String,
        lang: String
    ): ResultData<String, AuthError> {
        return try {
            val firebaseUser = auth.createUserWithEmailAndPassword(email, password).await()?.user
                ?: return ResultData.Error(AuthError.UserNotCreated)

            setUser(firebaseUser)

            val userPreferences = UserRemotePreferences(
                userId = firebaseUser.uid,
                language = lang,
                subscription = getUser().subscription
            )

            userRepository.saveUserPreferences(userPreferences)
            ResultData.Success(firebaseUser.uid)
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthUserCollisionException -> ResultData.Error(AuthError.UserAlreadyExists)
                is FirebaseAuthEmailException -> ResultData.Error(AuthError.InvalidEmail)
                is FirebaseFirestoreException -> ResultData.Error(AuthError.UserDataNotSaved)
                else -> ResultData.Error(AuthError.UserNotCreated)
            }
        }
    }

    suspend fun signIn(
        email: String,
        password: String
    ): ResultData<UserRemotePreferences?, AuthError> {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
                .user?.let(::setUser)
                ?: return ResultData.Error(AuthError.WrongCredentials)
            val firebaseUser = auth.currentUser ?: return ResultData.Error(AuthError.SignInError)

            return if (firebaseUser.isEmailVerified) {
                userRepository.getUserPreferences(firebaseUser.uid)
            } else {
                ResultData.Success(null)
            }
        } catch (e: Exception) {
            return when (e) {
                is FirebaseAuthEmailException -> ResultData.Error(AuthError.InvalidEmail)
                is FirebaseAuthInvalidCredentialsException -> ResultData.Error(AuthError.WrongCredentials)
                is FirebaseAuthInvalidUserException -> ResultData.Error(AuthError.UserNotFound)
                else -> ResultData.Error(AuthError.SignInError)
            }
        }
    }

    private suspend fun reauthenticate(password: String): ResultData<FirebaseUser, AuthError> {
        val (currentUser, email) = auth.currentUser.let { it to it?.email }.takeIfNoneIsNull()
            ?: return ResultData.Error(AuthError.UserNotSignedIn)

        return try {
            val credential = EmailAuthProvider.getCredential(email, password)
            currentUser.reauthenticate(credential).await()
            auth.currentUser?.reload()?.await()

            auth.currentUser
                ?.let { ResultData.Success(it) }
                ?: ResultData.Error(AuthError.UserNotSignedIn)
        } catch (e: Exception) {
            ResultData.Error(AuthError.WrongCredentials)
        }
    }

    suspend fun sendEmailVerificationEmail(): AuthResult {
        val currentUser = auth.currentUser ?: return Result.Error(AuthError.UserNotSignedIn)

        return try {
            currentUser.sendEmailVerification().await()
            Result.Success(AuthSuccess.SignUpEmailVerificationSent)
        } catch (e: Exception) {
            Result.Error(AuthError.SignUpEmailVerificationError)
        }
    }

    suspend fun requestEmailUpdate(password: String, newEmail: String): AuthResult {
        val (currentUser, currentEmail) = auth.currentUser.let { it to it?.email }.takeIfNoneIsNull()
            ?: return Result.Error(AuthError.UserNotSignedIn)

        try {
            val credential = EmailAuthProvider.getCredential(currentEmail, password)
            currentUser.reauthenticate(credential).await()
        } catch (e: Exception) {
            return when (e) {
                is FirebaseAuthInvalidCredentialsException -> Result.Error(AuthError.WrongCredentials)
                is FirebaseAuthInvalidUserException -> Result.Error(AuthError.UserNotFound)
                else -> Result.Error(AuthError.ReauthenticationError)
            }
        }

        return try {
            currentUser.verifyBeforeUpdateEmail(newEmail).await()
            Result.Success(AuthSuccess.UpdateEmailEmailVerificationSent)
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthEmailException, is FirebaseAuthInvalidCredentialsException ->
                    Result.Error(AuthError.InvalidEmail)
                is FirebaseAuthUserCollisionException -> Result.Error(AuthError.UserAlreadyExists)
                else -> Result.Error(AuthError.EmailVerificationError)
            }
        }
    }

    suspend fun updatePassword(currentPassword: String, newPassword: String): AuthResult {
        val reauthenticationResult = reauthenticate(currentPassword)
        if (reauthenticationResult is ResultData.Error) {
            return Result.Error(reauthenticationResult.error)
        }
        val firebaseUser = (reauthenticationResult as ResultData.Success).data
        val email = firebaseUser.email ?: return Result.Error(AuthError.UserNotFound)

        try {
            val credential = EmailAuthProvider.getCredential(email, currentPassword)
            firebaseUser.reauthenticate(credential).await()
        } catch (e: Exception) {
            return Result.Error(AuthError.WrongCredentials)
        }

        return try {
            firebaseUser.updatePassword(newPassword).await()
            Result.Success(AuthSuccess.PasswordUpdated)
        } catch (e: Exception) {
            Result.Error(AuthError.UpdatePasswordError)
        }
    }

    suspend fun requestPasswordReset(email: String): AuthResult {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.Success(AuthSuccess.ResetPasswordEmailSent)
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidUserException -> Result.Error(AuthError.UserNotFound)
                is FirebaseAuthInvalidCredentialsException -> Result.Error(AuthError.InvalidEmail)
                else -> Result.Error(AuthError.EmailForPasswordResetError)
            }
        }
    }

    suspend fun setNewPassword(obbCode: String, newPassword: String): AuthResult {
        return try {
            auth.confirmPasswordReset(obbCode, newPassword).await()
            Result.Success(AuthSuccess.PasswordUpdated)
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> Result.Error(AuthError.InvalidCode)
                else -> Result.Error(AuthError.PasswordResetError)
            }
        }
    }

    fun signOut() {
        auth.signOut()
        resetUser()
    }

    suspend fun deleteAccount(password: String): AuthResult {
        val reauthenticationResult = reauthenticate(password)
        if (reauthenticationResult is ResultData.Error) {
            return Result.Error(reauthenticationResult.error)
        }
        val firebaseUser = (reauthenticationResult as ResultData.Success).data

        return try {
            userRepository.deleteAllUserData(firebaseUser.uid)

            firebaseUser.delete().await()
            resetUser()

            Result.Success(AuthSuccess.AccountDeleted)
        } catch (e: Exception) {
            when (e) {
                is FirebaseFirestoreException -> Result.Error(AuthError.DataDeletionError)
                else -> Result.Error(AuthError.AccountDeletionError)
            }
        }
    }

}