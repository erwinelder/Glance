package com.ataglance.walletglance.account.data.remote.source

import android.util.Log
import com.ataglance.walletglance.account.data.remote.model.AccountCommandDto
import com.ataglance.walletglance.account.data.remote.model.AccountQueryDto
import com.glanci.account.shared.service.AccountService
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class AccountRemoteDataSourceImpl(
    private val service: AccountService
) : AccountRemoteDataSource {

    constructor(client: KtorRpcClient) : this(service = client.withService<AccountService>())


    override suspend fun getUpdateTime(token: String): Long? {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrNull().also { timestamp ->
            if (timestamp != null) {
                Log.d("AccountRemoteDataSourceImpl", "getUpdateTime:" +
                        "received update time $timestamp")
            } else {
                Log.w("AccountRemoteDataSourceImpl", "getUpdateTime:" +
                        "no update time received")
            }
        }
    }

    override suspend fun synchronizeAccounts(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        token: String
    ): Boolean {
        return runCatching {
            service.saveAccounts(accounts = accounts, timestamp = timestamp, token = token)
        }.isSuccess.also { success ->
            if (success) {
                Log.d("AccountRemoteDataSourceImpl", "synchronizeAccounts: " +
                        "synchronized ${accounts.size} accounts at timestamp $timestamp")
            } else {
                Log.e("AccountRemoteDataSourceImpl", "synchronizeAccounts: " +
                        "failed to synchronize ${accounts.size} accounts at timestamp $timestamp")
            }
        }
    }

    override suspend fun getAccountsAfterTimestamp(
        timestamp: Long,
        token: String
    ): List<AccountQueryDto>? {
        return runCatching {
            service.getAccountsAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrNull().also { accounts ->
            accounts?.forEach {
                Log.d("AccountRemoteDataSourceImpl", "getAccountsAfterTimestamp:" +
                        "received account: id = ${it.id}, name = ${it.name}")
            }
        }
    }

    override suspend fun synchronizeAccountsAndGetAfterTimestamp(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<AccountQueryDto>? {
        return runCatching {
            service.saveAccountsAndGetAfterTimestamp(
                accounts = accounts,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrNull().also { accounts ->
            Log.d("AccountRemoteDataSourceImpl", "synchronizeAccountsAndGetAfterTimestamp:" +
                    "synchronized ${accounts?.size ?: 0} accounts at timestamp $timestamp" +
                    " with local timestamp $localTimestamp")
            accounts?.forEach {
                Log.d("AccountRemoteDataSourceImpl", "synchronizeAccountsAndGetAfterTimestamp:" +
                        "received account: id = ${it.id}, name = ${it.name}")
            }
        }
    }

}