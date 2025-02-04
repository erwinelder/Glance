package com.ataglance.walletglance.core.data.utils

import com.ataglance.walletglance.core.data.model.EntitiesToSync


fun needToSynchroniseData(localTimestamp: Long?, remoteTimestamp: Long?): Pair<Long?, Long>? {
    if (remoteTimestamp == null) return null
    if (localTimestamp != null && localTimestamp >= remoteTimestamp) return null

    return Pair(localTimestamp, remoteTimestamp)
}

suspend fun <LE, RE> synchroniseData(
    localUpdateTimeGetter: suspend () -> Long?,
    remoteUpdateTimeGetter: suspend () -> Long?,
    remoteDataGetter: suspend (timestamp: Long) -> EntitiesToSync<RE>,
    remoteDataToLocalDataMapper: (RE) -> LE,
    localSynchroniser: suspend (EntitiesToSync<LE>, timestamp: Long) -> Unit
) {
    val (localTimestamp, remoteTimestamp) = needToSynchroniseData(
        localTimestamp = localUpdateTimeGetter(),
        remoteTimestamp = remoteUpdateTimeGetter()
    ) ?: return

    val dataToSync = remoteDataGetter(localTimestamp ?: 0).map(remoteDataToLocalDataMapper)
    localSynchroniser(dataToSync, remoteTimestamp)
}