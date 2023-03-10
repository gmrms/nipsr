package com.nipsr.relay.filters.global

import com.nipsr.payload.Constants.NONCE_TAG
import com.nipsr.payload.model.events.Event
import com.nipsr.payload.nips.NIP_13
import com.nipsr.relay.filters.EventFilter
import com.nipsr.relay.filters.EventFilter.Companion.ok
import com.nipsr.relay.filters.EventFilter.Companion.pow
import com.nipsr.relay.filters.FilterType
import java.util.HexFormat
import javax.enterprise.context.ApplicationScoped
import kotlin.experimental.and

@NIP_13
@ApplicationScoped
class PoWEventFilter : EventFilter {
    override fun filter(event: Event<*>) : Pair<Boolean, String?> {
        val nonceTag = event.tags.find { it.tag == NONCE_TAG } ?: return ok()
        val difficultyTarget = nonceTag.options.first().toInt()
        val difficultyAchieved = getDifficulty(event)
        if(difficultyAchieved != difficultyTarget){
            return false pow "The provided difficulty is $difficultyAchieved but the target is $difficultyTarget"
        }
        return ok()
    }

    private fun getDifficulty(event: Event<*>) = countLeadingZeroBits(
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