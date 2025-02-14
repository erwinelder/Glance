package com.ataglance.walletglance.core.presentation.model

import android.content.Context

class ResourceManagerImpl(
    private val context: Context
) : ResourceManager {
    override fun getString(id: Int): String {
        return context.getString(id)
    }
}