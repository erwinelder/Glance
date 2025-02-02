package com.ataglance.walletglance.core.data.model

data class EntitiesToSynchronise<T>(
    val toDelete: List<T> = emptyList(),
    val toUpsert: List<T> = emptyList()
) {

    fun <R> map(transform: T.() -> R): EntitiesToSynchronise<R> {
        return EntitiesToSynchronise(
            toDelete = toDelete.map(transform),
            toUpsert = toUpsert.map(transform)
        )
    }

    fun <R> map(transform: T.(deleted: Boolean) -> R): EntitiesToSynchronise<R> {
        return EntitiesToSynchronise(
            toDelete = toDelete.map { it.transform(true) },
            toUpsert = toUpsert.map { it.transform(false) }
        )
    }

    fun isNotEmpty(): Boolean {
        return toUpsert.isNotEmpty() || toDelete.isNotEmpty()
    }

}
