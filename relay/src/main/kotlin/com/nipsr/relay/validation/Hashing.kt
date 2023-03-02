package com.nipsr.relay.validation

import java.security.MessageDigest

/**
 * Helper class for hashing.
 */
object Hashing {

    val sha = MessageDigest.getInstance("SHA-256")

    fun sha256(bytes: ByteArray): ByteArray {
        return sha.digest(bytes)
    }

}