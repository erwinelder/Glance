package com.ataglance.walletglance.auth.data.repository

import com.ataglance.walletglance.auth.data.model.UserRemotePreferences
import com.ataglance.walletglance.core.mapper.toMap
import com.ataglance.walletglance.core.mapper.toUserRemotePreferences
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(val firestore: FirebaseFirestore) {

    private fun getUserPreferencesFirestoreRef(userId: String): DocumentReference {
        return firestore.collection("usersPreferences").document(userId)
    }

    private fun getUserDataFirestoreRef(userId: String): DocumentReference {
        return firestore.collection("usersData").document(userId)
    }

    suspend fun saveUserPreferences(userPreferences: UserRemotePreferences) {
        getUserPreferencesFirestoreRef(userPreferences.userId).set(userPreferences.toMap()).await()
    }

    suspend fun updateUserSubscription(userId: String, subscription: String) {

    }

    suspend fun getUserPreferences(userId: String): ResultData<UserRemotePreferences, AuthError> {
        getUserPreferencesFirestoreRef(userId).get().await()
            ?.data?.toUserRemotePreferences(userId = userId)
            ?.let { return ResultData.Success(it) }
            ?: return ResultData.Error(AuthError.UserNotFound)
    }

    suspend fun deleteAllUserData(userId: String) {
        firestore.runBatch { batch ->
            batch.delete(getUserPreferencesFirestoreRef(userId))
            batch.delete(getUserDataFirestoreRef(userId))
        }.await()
    }

}