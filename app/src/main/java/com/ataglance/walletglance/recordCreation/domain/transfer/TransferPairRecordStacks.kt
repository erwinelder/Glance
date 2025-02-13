package com.ataglance.walletglance.recordCreation.domain.transfer

import com.ataglance.walletglance.record.domain.model.RecordStack

data class TransferPairRecordStacks(
    val sender: RecordStack,
    val receiver: RecordStack
) {

    companion object {

        fun fromRecordStacks(stacks: Pair<RecordStack, RecordStack>): TransferPairRecordStacks? {
            if (stacks.first.isOutTransfer() == stacks.second.isOutTransfer()) return null

            return TransferPairRecordStacks(
                sender = if (stacks.first.isOutTransfer()) stacks.first else stacks.second,
                receiver = if (stacks.first.isOutTransfer()) stacks.second else stacks.first
            )
        }

    }

}
