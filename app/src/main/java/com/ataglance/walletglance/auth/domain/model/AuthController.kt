package com.ataglance.walletglance.auth.domain.model

import com.ataglance.walletglance.core.data.model.UserRemotePreferences
import com.ataglance.walletglance.core.mapper.toMap
import com.ataglance.walletglance.core.mapper.toUserRemotePreferences
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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

typealias AuthResult = Result<AuthSuccess, AuthError>

class AuthController(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    var user = User()

    private fun setUser(user: FirebaseUser) {
        this.user = User(uid = user.uid)
    }

    fun getUserId(): String? {
        return user.uid
    }

    fun getEmail(): String {
        return auth.currentUser?.email ?: ""
    }

    private val userFirestoreRef: DocumentReference?
        get() = user.uid?.let { firestore.collection("usersPreferences").document(it) }


    fun emailIsVerified(): Boolean {
        return auth.currentUser?.isEmailVerified ?: false
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
                userId = firebaseUser.uid, language = lang, subscription = user.subscription
            )

            userFirestoreRef?.set(userPreferences.toMap())?.await()
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

            if (firebaseUser.isEmailVerified) {
                userFirestoreRef?.get()?.await()
                    ?.data?.toUserRemotePreferences(userId = firebaseUser.uid)
                    ?.let { return ResultData.Success(it) }
                    ?: return ResultData.Error(AuthError.UserNotFound)
            } else {
                return ResultData.Success(null)
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

    suspend fun sendEmailVerificationEmail(): AuthResult {
        val currentUser = auth.currentUser ?: return Result.Error(AuthError.UserNotSignedIn)

        return try {
            currentUser.sendEmailVerification().await()
            Result.Success(AuthSuccess.SignUpEmailVerificationSent)
        } catch (e: Exception) {
            Result.Error(AuthError.SignUpEmailVerificationError)
        }
    }

    suspend fun updateEmail(password: String, newEmail: String): AuthResult {
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
        val (currentUser, email) = auth.currentUser.let { it to it?.email }.takeIfNoneIsNull()
            ?: return Result.Error(AuthError.UserNotSignedIn)

        try {
            val credential = EmailAuthProvider.getCredential(email, currentPassword)
            currentUser.reauthenticate(credential).await()
        } catch (e: Exception) {
            return Result.Error(AuthError.WrongCredentials)
        }

        return try {
            currentUser.updatePassword(newPassword).await()
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

    suspend fun reloadUser(): ResultData<String, AuthError> {
        try {
            auth.currentUser?.reload()?.await() ?: return ResultData.Error(AuthError.UserNotSignedIn)
            val firebaseUser = auth.currentUser ?: return ResultData.Error(AuthError.UserNotSignedIn)
            firebaseUser.let(::setUser)

            if (!firebaseUser.isEmailVerified) {
                return ResultData.Error(AuthError.EmailVerificationError)
            }

            val userId = user.uid ?: return ResultData.Error(AuthError.UserNotSignedIn)
            return ResultData.Success(userId)
        } catch (e: Exception) {
            return ResultData.Error(AuthError.EmailVerificationError)
        }
    }

    fun signOut() {
        auth.signOut()
        user = User()
    }

}