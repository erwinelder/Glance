package com.ataglance.walletglance.core.data.model

data class EntitiesToSynchronise<T>(
    val toDelete: List<T> = emptyList(),
    val toUpsert: List<T> = emptyList()
) {

    companion object {

        fun <T, R> fromEntities(
            entities: List<T>,
            deletedPredicate: (T) -> Boolean,
            mapper: (T) -> R
        ): EntitiesToSynchronise<R> {
            return entities.partition(deletedPredicate).let { (deleted, updated) ->
                EntitiesToSynchronise(
                    toDelete = deleted.map(mapper), toUpsert = updated.map(mapper)
                )
            }
        }

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
