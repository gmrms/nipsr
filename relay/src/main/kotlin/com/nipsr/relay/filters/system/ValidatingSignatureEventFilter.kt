package com.nipsr.relay.filters.system

import com.nipsr.payload.ObjectMapperUtils.toJsonByteArray
import com.nipsr.payload.model.events.Event
import com.nipsr.payload.nips.NIP_01
import com.nipsr.relay.filters.EventFilter
import com.nipsr.relay.filters.EventFilter.Companion.invalid
import com.nipsr.relay.filters.EventFilter.Companion.ok
import com.nipsr.relay.filters.FilterType
import com.nipsr.relay.validation.Hashing
import com.nipsr.relay.validation.Schnorr
import io.quarkus.arc.profile.UnlessBuildProfile
import javax.enterprise.context.ApplicationScoped

/**
 * An event filter that validates the signature of an event.
 */
@NIP_01
@ApplicationScoped
@UnlessBuildProfile("dev")
class ValidatingSignatureEventFilter : EventFilter {

    override fun filter(event: Event<*>) = if(event.isValid()){
        ok()
    } else {
        false invalid "Invalid signature"
    }

    override fun type() = FilterType.GLOBAL

    fun Event<*>.generateId() : String {
        return Hashing.sha.digest(
            serializeEvent(this)
        ).toHex()
    }

    fun serializeEvent(event: Event<*>) : ByteArray {
        return event.getFieldsToSerialize().toJsonByteArray()
    }

    fun Event<*>.getFieldsToSerialize() : List<Any?> {
        return listOf(
            0,
            this.pubkey,
            this.created_at,
            this.kind,
            this.tags.map { it.toList() },
            this.content
        )
    }

    fun ByteArray.toHex() : String {
        return this.joinToString("") { "%02x".format(it) }
    }

    fun Event<*>.isValid() : Boolean {
        return this.id == this.generateId() &&
                Schnorr.verify(
                    hexStringToByteArray(this.id),
                    hexStringToByteArray(this.pubkey),
                    hexStringToByteArray(this.sig)
                )
    }

    private fun hexStringToByteArray(string: String): ByteArray {
        val len = string.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(string[i], 16) shl 4)
                    + Character.digit(string[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

}