package com.ataglance.walletglance.auth.domain.model

import android.util.Log
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
import com.google.firebase.firestore.SetOptions
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


    suspend fun createNewUser(
        email: String,
        password: String,
        lang: String
    ): ResultData<String, AuthError> {
        return try {
            val firebaseUser = auth.createUserWithEmailAndPassword(email, password).await().user
                ?: return ResultData.Error(AuthError.UserNotCreated)

            setUser(firebaseUser)

            val userPreferences = UserRemotePreferences(
                language = lang, subscription = user.subscription
            )

            userFirestoreRef?.set(userPreferences.toMap(), SetOptions.merge())?.await()
            ResultData.Success(firebaseUser.uid)
        } catch (e: Exception) {
            Log.e("Create new user Firebase operation", "Error occurred: ", e)
            when (e) {
                is FirebaseAuthUserCollisionException -> ResultData.Error(AuthError.UserAlreadyExists)
                is FirebaseAuthEmailException -> ResultData.Error(AuthError.InvalidEmail)
                is FirebaseFirestoreException -> ResultData.Error(AuthError.UserDataNotSaved)
                else -> ResultData.Error(AuthError.UserNotCreated)
            }
        }
    }

    suspend fun sendSignUpVerificationEmail(): AuthResult {
        val currentUser = auth.currentUser ?: return Result.Error(AuthError.UserNotSignedIn)

        return try {
            currentUser.sendEmailVerification().await()
            Result.Success(AuthSuccess.SignUpEmailVerificationSent)
        } catch (e: Exception) {
            Result.Error(AuthError.SignUpEmailVerificationError)
        }
    }

    suspend fun signIn(
        email: String,
        password: String
    ): ResultData<UserRemotePreferences, AuthError> {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
                .user?.let(::setUser)
                ?: return ResultData.Error(AuthError.WrongCredentials)

            userFirestoreRef?.get()?.await()
                ?.data?.toUserRemotePreferences()
                ?.let { return ResultData.Success(it) }
                ?: return ResultData.Error(AuthError.UserNotFound)
        } catch (e: Exception) {
            Log.e("Sign in Firebase operation", "Error occurred: ", e)
            return when (e) {
                is FirebaseAuthEmailException -> ResultData.Error(AuthError.InvalidEmail)
                is FirebaseAuthInvalidCredentialsException -> ResultData.Error(AuthError.WrongCredentials)
                is FirebaseAuthInvalidUserException -> ResultData.Error(AuthError.UserNotFound)
                else -> ResultData.Error(AuthError.SignInError)
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
        auth.currentUser ?: return Result.Error(AuthError.UserNotSignedIn)

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
        user = User()
    }

}