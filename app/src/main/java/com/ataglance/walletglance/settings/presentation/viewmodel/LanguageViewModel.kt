package com.ataglance.walletglance.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.category.domain.usecase.TranslateCategoriesUseCase
import com.ataglance.walletglance.category.presentation.model.DefaultCategoriesPackage
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.ataglance.walletglance.errorHandling.presentation.model.RequestState
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState.MessageState
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageLocallyUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageRemotelyUseCase
import com.ataglance.walletglance.settings.mapper.toResultWithMessageState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.parametersOf

class LanguageViewModel(
    currentLangCode: String,
    private val translateCategoriesUseCase: TranslateCategoriesUseCase,
    private val saveLanguageLocallyUseCase: SaveLanguageLocallyUseCase,
    private val saveLanguageRemotelyUseCase: SaveLanguageRemotelyUseCase,
    private val userContext: UserContext
): ViewModel() {

    private val _langCode = MutableStateFlow<String?>(currentLangCode)
    val langCode = _langCode.asStateFlow()

    fun selectLanguage(langCode: String) {
        _langCode.update { langCode }
    }

    fun applyLanguage() {
        val langCode = _langCode.value ?: return

        val koin = GlobalContext.get()
        val categoriesPackageCurr = DefaultCategoriesPackage(
            resourceManager = koin.get<ResourceManager>()
        )
        val categoriesPackageNew = DefaultCategoriesPackage(
            resourceManager = koin.get<ResourceManager> { parametersOf(langCode) }
        )

        viewModelScope.launch {
            translateCategoriesUseCase.execute(
                defaultCategoriesInCurrLocale = categoriesPackageCurr.getAsList(),
                defaultCategoriesInNewLocale = categoriesPackageNew.getAsList()
            )

            val timestamp = getCurrentTimestamp()

            saveLanguageLocallyUseCase.execute(langCode = langCode, timestamp = timestamp)

            if (userContext.isSignedIn()) {
                setRequestLoadingState()
                val result = saveLanguageRemotelyUseCase.execute(
                    langCode = langCode, timestamp = timestamp
                )

                (result as? ResultData.Error)?.toResultWithMessageState()?.let {
                    setRequestResultState(result = it)
                    delay(1000)
                }

                resetRequestState()
            }
        }
    }


    private val _requestState = MutableStateFlow<RequestState<MessageState>?>(null)
    val requestState = _requestState.asStateFlow()

    private fun setRequestLoadingState() {
        _requestState.update {
            RequestState.Loading(messageRes = R.string.saving_language_loader)
        }
    }

    private fun setRequestResultState(result: MessageState) {
        _requestState.update { RequestState.Result(resultState = result) }
    }

    fun resetRequestState() {
        _requestState.update { null }
    }

}