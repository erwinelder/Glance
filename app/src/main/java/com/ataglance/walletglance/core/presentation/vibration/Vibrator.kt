package com.ataglance.walletglance.core.presentation.vibration

interface Vibrator {

    fun vibrateLightSingleTap()

    fun vibrateSuccessTaps()

    fun vibrateErrorTaps()

    fun vibrateSequence(count: Int)

}