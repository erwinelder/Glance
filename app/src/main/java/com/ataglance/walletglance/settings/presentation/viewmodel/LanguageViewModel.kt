package com.ataglance.walletglance.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.usecase.TranslateCategoriesUseCase
import com.ataglance.walletglance.settings.domain.usecase.ApplyLanguageToSystemUseCase
import com.ataglance.walletglance.settings.domain.usecase.SaveLanguagePreferenceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LanguageViewModel(
    currentLangCode: String,
    private val applyLanguageToSystemUseCase: ApplyLanguageToSystemUseCase,
    private val saveLanguagePreferenceUseCase: SaveLanguagePreferenceUseCase,
    private val translateCategoriesUseCase: TranslateCategoriesUseCase
): ViewModel() {

    private val _langCode = MutableStateFlow<String?>(currentLangCode)
    val langCode = _langCode.asStateFlow()

    fun selectNewLanguage(langCode: String) {
        _langCode.update { langCode }
    }

    suspend fun applyLanguage() {
        val langCode = langCode.value ?: return

        saveLanguagePreferenceUseCase.save(langCode)
        applyLanguageToSystemUseCase.execute(langCode)
    }


    suspend fun translatedCategories(
        defaultInCurrLocale: List<Category>,
        defaultInNewLocale: List<Category>
    ) {
        translateCategoriesUseCase.execute(
            defaultCategoriesInCurrLocale = defaultInCurrLocale,
            defaultCategoriesInNewLocale = defaultInNewLocale
        )
    }

}