package com.ataglance.walletglance.record.presentation.model

import androidx.compose.runtime.Stable

@Stable
data class RecordDraftPreferences(
    val includeInBudgets: Boolean = true
)
