package com.ataglance.walletglance.di

import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.local.preferences.SecureStorage
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.remote.glanciBackendUrl
import com.ataglance.walletglance.core.data.remote.glanciBackendWebSocketPort
import com.ataglance.walletglance.core.domain.usecase.DeleteAllDataLocallyUseCase
import com.ataglance.walletglance.core.domain.usecase.DeleteAllDataLocallyUseCaseImpl
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.ExpensesIncomeWidgetViewModel
import com.ataglance.walletglance.settings.domain.usecase.language.ApplyLanguageToSystemUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.ApplyLanguageToSystemUseCaseImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.krpc.ktor.client.installKrpc
import kotlinx.rpc.krpc.ktor.client.rpc
import kotlinx.rpc.krpc.ktor.client.rpcConfig
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val coreModule = module {

    /* ---------- Other ---------- */

    single {
        AppDatabase.getDatabase(context = get())
    }

    single {
        SecureStorage(settings = get())
    }

    single {
        DataSyncHelper(userContext = get())
    }

    single<HttpClient> {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    single<KtorRpcClient> { params ->
        val path = params.get<String>()

        HttpClient(OkHttp) {
            installKrpc()
        }.rpc {
            url {
                protocol = URLProtocol.WSS
                host = glanciBackendUrl.removePrefix("https://")
                port = glanciBackendWebSocketPort.toInt()
                encodedPath = path
            }

            rpcConfig {
                serialization {
                    json()
                }
            }
        }
    }

    /* ---------- Use Cases ---------- */

    single<ApplyLanguageToSystemUseCase> {
        ApplyLanguageToSystemUseCaseImpl()
    }

    single<DeleteAllDataLocallyUseCase> {
        DeleteAllDataLocallyUseCaseImpl(
            settingsRepository = get(),
            accountRepository = get(),
            categoryRepository = get(),
            categoryCollectionRepository = get(),
            widgetRepository = get(),
            navigationButtonRepository = get()
        )
    }

    /* ---------- ViewModels ---------- */

    viewModel {
        AppViewModel(
            getAppThemeConfigurationUseCase = get(),
            applyLanguageToSystemUseCase = get(),
            getLanguagePreferenceUseCase = get(),
            changeAppSetupStageUseCase = get(),
            getStartDestinationsBySetupStageUseCase = get(),

            getAccountsUseCase = get(),
            getWidgetsUseCase = get()
        )
    }

    viewModel { parameters ->
        ExpensesIncomeWidgetViewModel(
            activeAccount = parameters.getOrNull(),
            activeDateRange = parameters.get(),
            getTransactionsInDateRangeUseCase = get()
        )
    }

}