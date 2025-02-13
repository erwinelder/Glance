package com.ataglance.walletglance.di

import com.ataglance.walletglance.categoryCollection.data.local.source.CategoryCollectionLocalDataSource
import com.ataglance.walletglance.categoryCollection.data.local.source.getCategoryCollectionLocalDataSource
import com.ataglance.walletglance.categoryCollection.data.remote.dao.CategoryCollectionRemoteDao
import com.ataglance.walletglance.categoryCollection.data.remote.source.CategoryCollectionRemoteDataSource
import com.ataglance.walletglance.categoryCollection.data.remote.source.CategoryCollectionRemoteDataSourceImpl
import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepository
import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepositoryImpl
import com.ataglance.walletglance.categoryCollection.domain.usecase.GetCategoryCollectionsUseCase
import com.ataglance.walletglance.categoryCollection.domain.usecase.GetCategoryCollectionsUseCaseImpl
import com.ataglance.walletglance.categoryCollection.domain.usecase.SaveCategoryCollectionsUseCase
import com.ataglance.walletglance.categoryCollection.domain.usecase.SaveCategoryCollectionsUseCaseImpl
import com.ataglance.walletglance.categoryCollection.presentation.viewmodel.EditCategoryCollectionViewModel
import com.ataglance.walletglance.categoryCollection.presentation.viewmodel.EditCategoryCollectionsViewModel
import com.ataglance.walletglance.core.data.remote.FirestoreAdapterFactory
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val categoryCollectionModule = module {

    /* ---------- DAOs ---------- */

    single {
        CategoryCollectionRemoteDao(
            categoryCollectionFirestoreAdapter = FirestoreAdapterFactory(firestore = get())
                .getCategoryCollectionFirestoreAdapter(),
            associationFirestoreAdapter = FirestoreAdapterFactory(firestore = get())
                .getCollectionCategoryAssociationFirestoreAdapter()
        )
    }

    /* ---------- Data Sources ---------- */

    single<CategoryCollectionLocalDataSource> {
        getCategoryCollectionLocalDataSource(appDatabase = get())
    }

    single<CategoryCollectionRemoteDataSource> {
        CategoryCollectionRemoteDataSourceImpl(
            categoryCollectionDao = get(),
            updateTimeDao = get()
        )
    }

    /* ---------- Repositories ---------- */

    single<CategoryCollectionRepository> {
        CategoryCollectionRepositoryImpl(
            localSource = get(),
            remoteSource = get(),
            userContext = get()
        )
    }

    /* ---------- Use Cases ---------- */

    single<SaveCategoryCollectionsUseCase> {
        SaveCategoryCollectionsUseCaseImpl(categoryCollectionRepository = get())
    }

    single<GetCategoryCollectionsUseCase> {
        GetCategoryCollectionsUseCaseImpl(categoryCollectionRepository = get())
    }

    /* ---------- View Models ---------- */

    viewModel {
        EditCategoryCollectionsViewModel(
            saveCategoryCollectionsUseCase = get(),
            getCategoryCollectionsUseCase = get(),
            getCategoriesUseCase = get()
        )
    }

    viewModel {
        EditCategoryCollectionViewModel(getCategoriesUseCase = get())
    }

}