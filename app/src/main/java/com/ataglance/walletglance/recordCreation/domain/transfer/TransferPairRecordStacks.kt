package com.ataglance.walletglance.recordCreation.domain.transfer

import com.ataglance.walletglance.record.domain.model.RecordStack

data class TransferPairRecordStacks(
    val sender: RecordStack,
    val receiver: RecordStack
)
