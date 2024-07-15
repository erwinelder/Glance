package com.ataglance.walletglance.ui.viewmodels.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithIds
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionsWithIds
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.ui.utils.filterByCollection
import com.ataglance.walletglance.ui.utils.filterByCollectionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class RecordsViewModel(
    passedCategoryCollections: CategoryCollectionsWithIds,
    recordsFilteredByDateAndAccount: List<RecordStack>,
) : ViewModel() {

    private val _categoryCollections = MutableStateFlow(passedCategoryCollections)
    val categoryCollections = _categoryCollections.asStateFlow()

    fun setCategoryCollections(collections: CategoryCollectionsWithIds) {
        _categoryCollections.update { collections }
    }


    private val _collectionType = MutableStateFlow(CategoryCollectionType.Mixed)
    val collectionType = _collectionType.asStateFlow()

    fun setCollectionType(type: CategoryCollectionType) {
        _collectionType.update { type }
        resetSelectedCollection()
    }


    private val _recordsFilteredByDateAndAccount = MutableStateFlow(recordsFilteredByDateAndAccount)

    fun setRecordsFilteredByDateAndAccount(recordList: List<RecordStack>) {
        _recordsFilteredByDateAndAccount.update { recordList }
    }


    val currentCollectionList = combine(
        _categoryCollections,
        _collectionType
    ) { collections, collectionType ->
        collections.getByType(collectionType)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _selectedCollection = MutableStateFlow(
        categoryCollections.value.getByType(collectionType.value).firstOrNull()
            ?: CategoryCollectionWithIds()
    )
    val selectedCollection = _selectedCollection.asStateFlow()

    fun selectCollection(collection: CategoryCollectionWithIds) {
        _selectedCollection.update { collection }
    }

    private fun resetSelectedCollection() {
        _selectedCollection.update {
            categoryCollections.value.getByType(collectionType.value).firstOrNull()
                ?: CategoryCollectionWithIds()
        }
    }


    val recordsFilteredByDateAccountAndCollection = combine(
        _recordsFilteredByDateAndAccount,
        _collectionType,
        _selectedCollection
    ) { recordsFilteredByDateAndAccount, collectionType, selectedCollection ->
        recordsFilteredByDateAndAccount.filterByCollectionType(collectionType).let { recordList ->
            selectedCollection
                .takeIf { it.categoriesIds?.isNotEmpty() == true }
                ?.let { recordList.filterByCollection(selectedCollection) }
                ?: recordList
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

}

class RecordsViewModelFactory(
    private val categoryCollections: CategoryCollectionsWithIds,
    private val recordsFilteredByDateAndAccount: List<RecordStack>,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecordsViewModel(
            passedCategoryCollections = categoryCollections,
            recordsFilteredByDateAndAccount = recordsFilteredByDateAndAccount
        ) as T
    }
}