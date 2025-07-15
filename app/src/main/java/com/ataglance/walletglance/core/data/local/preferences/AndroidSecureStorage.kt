package com.ataglance.walletglance.core.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.core.content.edit
import com.russhwolf.settings.Settings
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class SecureStorageWithKeyGenerator(context: Context) : Settings {

    private val keyAlias = "glanci_secure_key"
    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        "glanci_secure_storage",
        Context.MODE_PRIVATE
    )

    private fun generateOrGetKey(): SecretKey {
        return if (keyStore.containsAlias(keyAlias)) {
            keyStore.getKey(keyAlias, null) as SecretKey
        } else {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()

            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }

    private fun encrypt(data: String): String {
        val secretKey = keyStore.getKey(keyAlias, null) as SecretKey
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encryptedData = cipher.doFinal(data.toByteArray())

        // Combine IV + encrypted data
        val combined = cipher.iv + encryptedData
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    private fun decrypt(encryptedData: String): String {
        val secretKey = keyStore.getKey(keyAlias, null) as SecretKey
        val combined = Base64.decode(encryptedData, Base64.DEFAULT)

        // Extract IV (first 12 bytes for GCM)
        val iv = combined.sliceArray(0..11)
        val encrypted = combined.sliceArray(12 until combined.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val gcmParameterSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec)

        val decryptedData = cipher.doFinal(encrypted)
        return String(decryptedData)
    }

    private fun getDecryptedValue(key: String): String? {
        return runCatching {
            sharedPrefs.getString(key, null)?.let { decrypt(it) }
        }.getOrNull()
    }


    override val keys: Set<String>
        get() = sharedPrefs.all.keys

    override val size: Int
        get() = sharedPrefs.all.size


    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        getDecryptedValue(key)?.toBoolean() ?: defaultValue

    override fun getBooleanOrNull(key: String): Boolean? = getDecryptedValue(key)?.toBoolean()

    override fun getFloat(key: String, defaultValue: Float): Float =
        getDecryptedValue(key)?.toFloat() ?: defaultValue

    override fun getFloatOrNull(key: String): Float? = getDecryptedValue(key)?.toFloat()

    override fun getDouble(key: String, defaultValue: Double): Double =
        getDecryptedValue(key)?.toDouble() ?: defaultValue

    override fun getDoubleOrNull(key: String): Double? = getDecryptedValue(key)?.toDouble()

    override fun getInt(key: String, defaultValue: Int): Int =
        getDecryptedValue(key)?.toInt() ?: defaultValue

    override fun getIntOrNull(key: String): Int? = getDecryptedValue(key)?.toInt()

    override fun getLong(key: String, defaultValue: Long): Long =
        getDecryptedValue(key)?.toLong() ?: defaultValue

    override fun getLongOrNull(key: String): Long? = getDecryptedValue(key)?.toLong()

    override fun getString(key: String, defaultValue: String): String =
        getDecryptedValue(key) ?: defaultValue

    override fun getStringOrNull(key: String): String? = getDecryptedValue(key)


    override fun putBoolean(key: String, value: Boolean) {
        val encryptedValue = encrypt(value.toString())
        sharedPrefs.edit { putString(key, encryptedValue) }
    }

    override fun putFloat(key: String, value: Float) {
        val encryptedValue = encrypt(value.toString())
        sharedPrefs.edit { putString(key, encryptedValue) }
    }

    override fun putDouble(key: String, value: Double) {
        val encryptedValue = encrypt(value.toString())
        sharedPrefs.edit { putString(key, encryptedValue) }
    }

    override fun putInt(key: String, value: Int) {
        val encryptedValue = encrypt(value.toString())
        sharedPrefs.edit { putString(key, encryptedValue) }
    }

    override fun putLong(key: String, value: Long) {
        val encryptedValue = encrypt(value.toString())
        sharedPrefs.edit { putString(key, encryptedValue) }
    }

    override fun putString(key: String, value: String) {
        val encryptedValue = encrypt(value)
        sharedPrefs.edit { putString(key, encryptedValue) }
    }

    override fun remove(key: String) {
        sharedPrefs.edit { remove(key) }
    }

    override fun clear() {
        sharedPrefs.edit { clear() }
    }

    override fun hasKey(key: String): Boolean = sharedPrefs.contains(key)


    init {
        keyStore.load(null)
        generateOrGetKey()
    }

}