package com.paligot.confily.backend.quiz.application

import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import java.util.HexFormat
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Turns a raw device identifier into a deterministic, keyed hash before it is persisted.
 *
 * The raw device id never touches the database, so anyone inspecting the storage cannot
 * trace a row back to a physical device. Hashing is keyed (HMAC) and salted so the device
 * id space cannot be brute-forced from a database dump alone, and deterministic so that the
 * unique index and device lookups keep working on the stored value.
 */
object DeviceIdHasher {
    private const val ALGORITHM = "HmacSHA256"

    fun hash(deviceId: String): String {
        val mac = Mac.getInstance(ALGORITHM)
        mac.init(SecretKeySpec(SystemEnv.Crypto.key.toByteArray(), ALGORITHM))
        return HexFormat.of().formatHex(mac.doFinal("${SystemEnv.Crypto.salt}$deviceId".toByteArray()))
    }
}
