package com.ataglance.walletglance.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LanguageViewModel(
    currentLangCode: String
): ViewModel() {
    private val _langCode: MutableStateFlow<String?> = MutableStateFlow(currentLangCode)
    val langCode: StateFlow<String?> = _langCode.asStateFlow()

    fun selectNewLanguage(newLangCode: String) {
        _langCode.update { newLangCode }
    }
}

data class LanguageViewModelFactory(
    val currentLangCode: String
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LanguageViewModel(currentLangCode) as T
    }
}
