package com.ataglance.walletglance.auth.data.repository

import com.ataglance.walletglance.auth.data.model.UserData
import com.ataglance.walletglance.core.data.mapper.toUserData
import com.ataglance.walletglance.core.data.mapper.toUserDataMap
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserFirestoreRepository(val firestore: FirebaseFirestore) : UserRepository {

    private fun getUsersPreferencesFirestoreRef(userId: String): DocumentReference {
        return firestore.collection("UsersPreferences").document(userId)
    }

    private fun getUsersDataFirestoreRef(userId: String): DocumentReference {
        return firestore.collection("UsersData").document(userId)
    }


    override suspend fun getUserData(userId: String): ResultData<UserData, AuthError> {
        getUsersPreferencesFirestoreRef(userId).get().await()
            ?.data?.toUserData(userId = userId)
            ?.let { return ResultData.Success(it) }
            ?: return ResultData.Error(AuthError.UserNotFound)
    }

    override suspend fun saveUserData(userData: UserData) {
        getUsersPreferencesFirestoreRef(userData.userId).set(userData.toUserDataMap()).await()
    }

    override suspend fun updateUserSubscription(userId: String, subscription: String) {
        getUsersPreferencesFirestoreRef(userId).update("subscription", subscription).await()
    }

    override suspend fun deleteAllUserData(userId: String) {
        firestore.runBatch { batch ->
            batch.delete(getUsersDataFirestoreRef(userId))
            batch.delete(getUsersPreferencesFirestoreRef(userId))
        }.await()
    }

}