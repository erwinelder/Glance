package com.ataglance.walletglance.ui.viewmodels.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LanguageViewModel: ViewModel() {
    private val _langCode: MutableStateFlow<String?> = MutableStateFlow(null)
    val langCode: StateFlow<String?> = _langCode.asStateFlow()

    fun chooseNewLanguage(newLangCode: String) {
        _langCode.update { newLangCode }
    }
}