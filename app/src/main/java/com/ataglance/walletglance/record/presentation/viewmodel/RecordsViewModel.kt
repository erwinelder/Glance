package com.ataglance.walletglance.record.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.domain.usecase.GetCategoryCollectionsUseCase
import com.ataglance.walletglance.categoryCollection.presentation.model.CategoryCollectionsUiState
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.usecase.GetRecordStacksInDateRangeUseCase
import com.ataglance.walletglance.record.domain.utils.filterByAccount
import com.ataglance.walletglance.record.domain.utils.filterByCollection
import com.ataglance.walletglance.record.domain.utils.shrinkForCompactView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecordsViewModel(
    activeAccount: Account?,
    activeDateRange: LongDateRange,
    private val defaultCollectionName: String,
    private val getCategoryCollectionsUseCase: GetCategoryCollectionsUseCase,
    private val getRecordStacksInDateRangeUseCase: GetRecordStacksInDateRangeUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {

            getCategoryCollectionsUseCase.getFlow().collect { collections ->
                collectionsByType = collections
                setCategoryCollections(collections = collections)
            }

        }
    }


    private val _activeAccountId = MutableStateFlow(activeAccount?.id)

    fun setActiveAccountId(id: Int) {
        if (_activeAccountId.value == id) return
        _activeAccountId.update { id }
    }


    private val _activeDateRange = MutableStateFlow(activeDateRange)

    fun setActiveDateRange(dateRange: LongDateRange) {
        if (_activeDateRange.value.equalsTo(dateRange)) return
        _activeDateRange.update { dateRange }
    }


    private var collectionsByType = CategoryCollectionsWithIdsByType()

    private val _categoryCollectionsUiState = MutableStateFlow(CategoryCollectionsUiState())
    val categoryCollectionsUiState = _categoryCollectionsUiState.asStateFlow()

    private fun setCategoryCollections(collections: CategoryCollectionsWithIdsByType) {
        _categoryCollectionsUiState.update {
            it.setCollections(
                collections = collections, defaultCollectionName = defaultCollectionName
            )
        }
    }

    fun toggleCollectionType() {
        _categoryCollectionsUiState.update {
            it.toggleCollectionType(
                collectionsByType = collectionsByType, defaultCollectionName = defaultCollectionName
            )
        }
    }

    fun selectCollection(collectionId: Int) {
        _categoryCollectionsUiState.update {
            it.selectCollection(collectionId = collectionId)
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private val _recordsInDateRange = _activeDateRange.flatMapLatest { dateRange ->
        getRecordStacksInDateRangeUseCase.getFlow(range = dateRange)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val filteredRecordStacks: StateFlow<List<RecordStack>> = combine(
        _recordsInDateRange,
        _activeAccountId,
        _categoryCollectionsUiState
    ) { stacks, accountId, collectionsState ->
        stacks
            .filterByAccount(accountId)
            .filterByCollection(collectionsState.activeCollection)
            .shrinkForCompactView()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

}