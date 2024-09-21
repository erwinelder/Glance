package com.ataglance.walletglance.recordCreation.domain.record

import androidx.compose.runtime.Stable

@Stable
data class RecordDraftPreferences(
    val includeInBudgets: Boolean = true
)
