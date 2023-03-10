package com.nipsr.relay.filters.global

import com.nipsr.payload.Constants.NONCE_TAG
import com.nipsr.payload.model.events.Event
import com.nipsr.payload.nips.NIP_13
import com.nipsr.relay.config.NipsrRelaySettings
import com.nipsr.relay.filters.EventFilter
import com.nipsr.relay.filters.EventFilter.Companion.ok
import com.nipsr.relay.filters.EventFilter.Companion.pow
import com.nipsr.relay.filters.FilterType
import java.util.HexFormat
import javax.enterprise.context.ApplicationScoped
import kotlin.experimental.and

@NIP_13
@ApplicationScoped
class PoWEventFilter(
    private val settings: NipsrRelaySettings
) : EventFilter {

    override fun filter(event: Event<*>) : Pair<Boolean, String?> {
        val target = getTargetDifficulty(event)
        if(settings.minPowRequired() > target){
            return false pow "The minimum difficulty required is ${settings.minPowRequired()} but the target was $target"
        }
        val achieved = getAchievedDifficulty(event)
        if(achieved != target){
            return false pow "The provided difficulty is $achieved but the target was $target"
        }
        return ok()
    }

    private fun getTargetDifficulty(event: Event<*>) : Int {
        val nonceTag = event.tags.find { it.tag == NONCE_TAG } ?: return 0
        return nonceTag.options.first().toInt()
    }

    private fun getAchievedDifficulty(event: Event<*>) = countLeadingZeroBits(
        HexFormat.of().parseHex(event.id)
    )

    /**
     * Counts the number of leading zero bits in a [ByteArray]
     */
    private fun countLeadingZeroBits(byteArray: ByteArray) : Int {
        var count = 0
        for(byte in byteArray){
            val leadingZeroBits = countLeadingZeroBits(byte)
            count += leadingZeroBits
            if(leadingZeroBits != 8){
                break
            }
        }
        return count
    }

    /**
     * Counts the number of leading zero bits in a [Byte]
     */
    private fun countLeadingZeroBits(byte: Byte) : Int {
        var count = 0
        var currentByte = byte
        for(i in 0..7){
            if(currentByte and 0x80.toByte() == 0.toByte()){
                count++
            } else {
                break
            }
            currentByte = (currentByte.toInt() shl 1).toByte()
        }
        return count
    }

    override fun type() = FilterType.GLOBAL

}