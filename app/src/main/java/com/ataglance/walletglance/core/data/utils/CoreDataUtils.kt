package com.ataglance.walletglance.core.data.utils

import com.ataglance.walletglance.core.data.model.EntitiesToSync


fun needToSynchroniseDataFromRemote(localTimestamp: Long?, remoteTimestamp: Long?): Pair<Long?, Long>? {
    if (remoteTimestamp == null) return null
    if (localTimestamp != null && localTimestamp >= remoteTimestamp) return null

    return Pair(localTimestamp, remoteTimestamp)
}

suspend fun <LE, RE> synchroniseDataFromRemote(
    localUpdateTimeGetter: suspend () -> Long?,
    remoteUpdateTimeGetter: suspend () -> Long?,
    remoteDataGetter: suspend (timestamp: Long) -> EntitiesToSync<RE>,
    remoteDataToLocalDataMapper: (RE) -> LE,
    localSynchroniser: suspend (EntitiesToSync<LE>, timestamp: Long) -> Unit
) {
    val (localTimestamp, remoteTimestamp) = needToSynchroniseDataFromRemote(
        localTimestamp = localUpdateTimeGetter(),
        remoteTimestamp = remoteUpdateTimeGetter()
    ) ?: return

    val dataToSync = remoteDataGetter(localTimestamp ?: 0).map(remoteDataToLocalDataMapper)
    localSynchroniser(dataToSync, remoteTimestamp)
}


fun needToSynchroniseDataToRemote(localTimestamp: Long?, remoteTimestamp: Long?): Long? {
    if (localTimestamp == null) return null
    if (remoteTimestamp != null && localTimestamp <= remoteTimestamp) return null

    return localTimestamp
}

suspend fun <LE, RE> synchroniseDataToRemote(
    localUpdateTimeGetter: suspend () -> Long?,
    remoteUpdateTimeGetter: suspend () -> Long?,
    localDataGetter: suspend () -> List<LE>,
    localDataToRemoteDataMapper: LE.(timestamp: Long) -> RE,
    remoteSynchroniser: suspend (List<RE>, timestamp: Long) -> Unit
) {
    val localTimestamp = needToSynchroniseDataToRemote(
        localTimestamp = localUpdateTimeGetter(),
        remoteTimestamp = remoteUpdateTimeGetter()
    ) ?: return

    val dataToSync = localDataGetter().map {
        it.localDataToRemoteDataMapper(localTimestamp)
    }
    remoteSynchroniser(dataToSync, localTimestamp)
}