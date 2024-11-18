package com.ataglance.walletglance.auth.domain.model

import android.util.Log
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.data.model.UserRemotePreferences
import com.ataglance.walletglance.core.mapper.toMap
import com.ataglance.walletglance.core.mapper.toUserRemotePreferences
import com.ataglance.walletglance.core.utils.takeIfNoneIsNull
import com.ataglance.walletglance.errorHandling.domain.model.TaskResult
import com.ataglance.walletglance.errorHandling.domain.model.TaskResultData
import com.ataglance.walletglance.errorHandling.domain.utils.getTaskErrorRunning
import com.ataglance.walletglance.errorHandling.domain.utils.getTaskResultRunning
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
    ): TaskResultData<String> {
        return runCatching {
            val firebaseUser = auth.createUserWithEmailAndPassword(email, password).await().user
                ?: return TaskResultData.error(R.string.user_not_created_error)

            setUser(firebaseUser)

            val userPreferences = UserRemotePreferences(
                language = lang,
                subscription = user.subscription
            )

            userFirestoreRef?.set(userPreferences.toMap(), SetOptions.merge())?.await()
            TaskResultData.success(firebaseUser.uid)
        }.getOrElse { exception ->
            Log.e("FirebaseOperation", "Error occurred: ", exception)
            when (exception) {
                is FirebaseAuthUserCollisionException -> TaskResultData.error(R.string.user_already_exists_error)
                is FirebaseAuthEmailException -> TaskResultData.error(R.string.invalid_email_error)
                is FirebaseFirestoreException -> TaskResultData.error(R.string.user_data_not_saved_error)
                else -> TaskResultData.error(R.string.user_not_created_error)
            }
        }
    }

    suspend fun signIn(email: String, password: String): TaskResultData<UserRemotePreferences> {
        return runCatching {
            auth.signInWithEmailAndPassword(email, password).await().user
                ?.let(::setUser)
                ?: return TaskResultData.error(R.string.wrong_credentials_error)

            userFirestoreRef?.get()?.await()?.data?.toUserRemotePreferences()
                ?.let { return TaskResultData.success(it) }
                ?: return TaskResultData.error(R.string.user_not_found_error)
        }.getOrElse { exception ->
            Log.e("FirebaseOperation", "Error occurred: ", exception)
            when (exception) {
                is FirebaseAuthEmailException -> TaskResultData.error(R.string.invalid_email_error)
                is FirebaseAuthInvalidCredentialsException -> TaskResultData.error(R.string.wrong_credentials_error)
                is FirebaseAuthInvalidUserException -> TaskResultData.error(R.string.user_not_found_error)
                else -> TaskResultData.error(R.string.sign_in_error)
            }
        }
    }

    suspend fun updatePassword(currentPassword: String, newPassword: String): TaskResult {
        val (currentUser, email) = auth.currentUser.let { it to it?.email }.takeIfNoneIsNull()
            ?: return TaskResult.error(R.string.user_not_logged_in_error)

        getTaskErrorRunning(R.string.wrong_credentials_error) {
            val credential = EmailAuthProvider.getCredential(email, currentPassword)
            currentUser.reauthenticate(credential).await()
        }?.let { return it }

        return getTaskResultRunning(errorMessageRes = R.string.update_password_error) {
            currentUser.updatePassword(newPassword).await()
        }
    }

    fun signOut() {
        auth.signOut()
        user = User()
    }

}