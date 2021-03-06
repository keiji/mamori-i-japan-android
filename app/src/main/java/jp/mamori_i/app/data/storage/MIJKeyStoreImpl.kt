package jp.mamori_i.app.data.storage

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.*


class MIJKeyStoreImpl: MIJKeyStore {

    private val PROVIDER = "AndroidKeyStore"
    private val ALIAS = "jp.mamori_i.app.keystore"
    private val keyStore: KeyStore

    init {
        try {
            keyStore = KeyStore.getInstance(PROVIDER)
            keyStore.load(null)
            if (!keyStore.containsAlias(ALIAS)) {
                createNewKey(ALIAS)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private fun createNewKey(alias: String) {
        try {
            val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA, PROVIDER
            )
            keyPairGenerator.initialize(
                KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_DECRYPT
                )
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                    .build()
            )
            keyPairGenerator.generateKeyPair()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun publicKey(): PublicKey {
        return keyStore.getCertificate(ALIAS).publicKey
    }

    override fun privateKey(): Key {
        return keyStore.getKey(ALIAS, null)
    }
}