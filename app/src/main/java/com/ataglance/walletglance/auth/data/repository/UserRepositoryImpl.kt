package com.ataglance.walletglance.auth.data.repository

import com.ataglance.walletglance.auth.data.model.UserData
import com.ataglance.walletglance.core.mapper.toMap
import com.ataglance.walletglance.core.mapper.toUserData
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(val firestore: FirebaseFirestore) : UserRepository {

    private fun getUserPreferencesFirestoreRef(userId: String): DocumentReference {
        return firestore.collection("usersPreferences").document(userId)
    }

    private fun getUserDataFirestoreRef(userId: String): DocumentReference {
        return firestore.collection("usersData").document(userId)
    }


    override suspend fun getUserData(userId: String): ResultData<UserData, AuthError> {
        getUserPreferencesFirestoreRef(userId).get().await()
            ?.data?.toUserData(userId = userId)
            ?.let { return ResultData.Success(it) }
            ?: return ResultData.Error(AuthError.UserNotFound)
    }

    override suspend fun saveUserPreferences(userPreferences: UserData) {
        getUserPreferencesFirestoreRef(userPreferences.userId).set(userPreferences.toMap()).await()
    }

    override suspend fun updateUserSubscription(userId: String, subscription: String) {

    }

    override suspend fun deleteAllUserData(userId: String) {
        firestore.runBatch { batch ->
            batch.delete(getUserDataFirestoreRef(userId))
            batch.delete(getUserPreferencesFirestoreRef(userId))
        }.await()
    }

}