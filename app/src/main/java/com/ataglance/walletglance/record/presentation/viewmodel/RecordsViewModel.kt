package com.ataglance.walletglance.record.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.domain.utils.toggle
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.utils.filterByCollection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class RecordsViewModel(
    passedCategoryCollections: CategoryCollectionsWithIdsByType,
    recordsFilteredByDateAndAccount: List<RecordStack>,
) : ViewModel() {

    private val _categoryCollections = MutableStateFlow(passedCategoryCollections)

    fun setCategoryCollections(collections: CategoryCollectionsWithIdsByType) {
        _categoryCollections.update { collections }
    }


    private val _collectionType = MutableStateFlow(CategoryCollectionType.Mixed)
    val collectionType = _collectionType.asStateFlow()

    fun toggleCollectionType() {
        _collectionType.update { it.toggle() }
        resetSelectedCollection()
    }


    private val _recordsFilteredByDateAndAccount = MutableStateFlow(recordsFilteredByDateAndAccount)

    fun setRecordsByDateAndAccount(recordList: List<RecordStack>) {
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


    val recordsByDateAccountAndCollection = combine(
        _recordsFilteredByDateAndAccount,
        _selectedCollection
    ) { recordsFilteredByDateAndAccount, selectedCollection ->
        recordsFilteredByDateAndAccount.filterByCollection(selectedCollection)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

}

class RecordsViewModelFactory(
    private val categoryCollections: CategoryCollectionsWithIdsByType,
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