package com.ataglance.walletglance.core.data.model

data class EntitiesToSync<T>(
    val toDelete: List<T> = emptyList(),
    val toUpsert: List<T> = emptyList()
) {

    companion object {

        fun <T> fromEntities(
            entities: List<T>,
            deletedPredicate: (T) -> Boolean,
        ): EntitiesToSync<T> {
            return entities.partition(deletedPredicate).let { (deleted, updated) ->
                EntitiesToSync(toDelete = deleted, toUpsert = updated)
            }
        }

    }


    fun <R> map(transform: T.() -> R): EntitiesToSync<R> {
        return EntitiesToSync(
            toDelete = toDelete.map(transform),
            toUpsert = toUpsert.map(transform)
        )
    }

    fun <R> map(transform: T.(deleted: Boolean) -> R): EntitiesToSync<R> {
        return EntitiesToSync(
            toDelete = toDelete.map { it.transform(true) },
            toUpsert = toUpsert.map { it.transform(false) }
        )
    }

    fun isNotEmpty(): Boolean {
        return toUpsert.isNotEmpty() || toDelete.isNotEmpty()
    }

}
