package com.ataglance.walletglance.record.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionsWithIds
import com.ataglance.walletglance.categoryCollection.utils.toggle
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.utils.filterByCollection
import com.ataglance.walletglance.record.utils.filterByCollectionType
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

    fun setCategoryCollections(collections: CategoryCollectionsWithIds) {
        _categoryCollections.update { collections }
    }


    private val _collectionType = MutableStateFlow(CategoryCollectionType.Mixed)
    val collectionType = _collectionType.asStateFlow()

    fun toggleCollectionType() {
        _collectionType.update { it.toggle() }
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
        _categoryCollections.value.getByType(collectionType.value).firstOrNull()
            ?: CategoryCollectionWithIds()
    )
    val selectedCollection = _selectedCollection.asStateFlow()

    fun selectCollection(collection: CategoryCollectionWithIds) {
        _selectedCollection.update { collection }
    }

    private fun resetSelectedCollection() {
        _selectedCollection.update {
            _categoryCollections.value.getByType(collectionType.value).firstOrNull()
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