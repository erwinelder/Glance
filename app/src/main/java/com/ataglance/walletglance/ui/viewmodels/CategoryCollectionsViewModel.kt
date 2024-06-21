package com.ataglance.walletglance.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithCategories
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionsWithCategories
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionsWithIds
import com.ataglance.walletglance.domain.entities.Category
import com.ataglance.walletglance.ui.utils.toggle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CategoryCollectionsViewModel(
    private val categoryList: List<Category>,
    categoryCollectionsWithIds: CategoryCollectionsWithIds
) : ViewModel() {

    private val _categoryCollectionType: MutableStateFlow<CategoryCollectionType> =
        MutableStateFlow(CategoryCollectionType.Mixed)
    val categoryCollectionType: StateFlow<CategoryCollectionType> =
        _categoryCollectionType.asStateFlow()

    private val _collectionsWithCategories: MutableStateFlow<CategoryCollectionsWithCategories> =
        MutableStateFlow(categoryCollectionsWithIds.toCollectionsWithCategories(categoryList))
    val collectionsWithCategories: StateFlow<CategoryCollectionsWithCategories> =
        _collectionsWithCategories.asStateFlow()

    val collectionsWithCategoriesByType: StateFlow<List<CategoryCollectionWithCategories>> =
        combine(
            _collectionsWithCategories,
            _categoryCollectionType
        ) { collectionsWithCategories, categoryType ->
            collectionsWithCategories.getListByType(categoryType)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )

    private val _collectionToEdit: MutableStateFlow<CategoryCollectionWithCategories?> =
        MutableStateFlow(null)
    val collectionToEdit: StateFlow<CategoryCollectionWithCategories?> =
        _collectionToEdit.asStateFlow()


    fun changeCategoryType(type: CategoryCollectionType? = null) {
        _categoryCollectionType.update { type ?: it.toggle() }
    }

    fun addNewCollection(context: Context) {
        _collectionsWithCategories.update {
            it.cloneAndAddNewCollection(
                type = categoryCollectionType.value,
                name = context.getString(R.string.new_category_collection_name)
            )
        }
    }

    fun deleteCollection(collectionOrderNum: Int) {
        _collectionsWithCategories.update {
            it.cloneAndDeleteCollectionByOrderNum(collectionOrderNum, categoryCollectionType.value)
        }
    }

    fun applyCollectionToEdit(collectionOrderNum: Int) {
        _collectionToEdit.update {
            collectionsWithCategories.value.getListByType(categoryCollectionType.value)
                .find { it.orderNum == collectionOrderNum }
        }
    }

}

data class CategoryCollectionsViewModelFactory(
    private val categoryList: List<Category>,
    private val collectionsWithIds: CategoryCollectionsWithIds
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryCollectionsViewModel(categoryList, collectionsWithIds) as T
    }
}
