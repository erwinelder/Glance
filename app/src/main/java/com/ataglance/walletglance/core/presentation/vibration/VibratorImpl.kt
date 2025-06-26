package com.ataglance.walletglance.core.presentation.vibration

import android.content.Context
import android.os.Build
import android.os.VibratorManager

class VibratorImpl(private val context: Context) : Vibrator {

    override fun vibrateLightSingleTap() {
        /*val vibrator = getVibrator()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100)
        }*/
    }

    override fun vibrateSuccessTaps() {
        /*val vibrator = getVibrator()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pattern = longArrayOf(0, 50, 200, 30)
            val amplitudes = intArrayOf(180, 255)
            val effect = VibrationEffect.createWaveform(pattern, amplitudes, -1)
            vibrator.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(150)
        }*/
    }

    override fun vibrateErrorTaps() {
        /*val vibrator = getVibrator()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
            vibrator.vibrate(effect)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100)
        }*/
    }

    override fun vibrateSequence(count: Int) {
        /*val vibrator = getVibrator()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val (timings, amplitudes) = when (count) {
                4 -> Pair(
                    longArrayOf(450, 50, 150, 50, 150, 50, 150, 50),
                    intArrayOf(0, 255, 0, 220, 0, 200, 0, 180)
                )
                3 -> Pair(
                    longArrayOf(450, 50, 150, 50, 150, 50),
                    intArrayOf(0, 255, 0, 220, 0, 200)
                )
                2 -> Pair(
                    longArrayOf(450, 50, 150, 50),
                    intArrayOf(0, 255, 0, 220)
                )
                else -> {
                    val baseDelay = 150L
                    val baseVibration = 50L
                    val timings = mutableListOf<Long>()
                    val amplitudes = mutableListOf<Int>()
                    var delay = 300L
                    repeat(count) {
                        timings.add(delay)
                        timings.add(baseVibration)
                        amplitudes.add(0)
                        amplitudes.add(200)
                        delay += baseDelay
                    }
                    Pair(timings.toLongArray(), amplitudes.toIntArray())
                }
            }
            val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
            vibrator.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(300)
        }*/
    }


    private fun getVibrator(): android.os.Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager)
                .defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
        }
    }

}