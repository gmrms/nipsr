package com.nipsr.payload.codec

import com.nipsr.payload.model.events.Event
import com.nipsr.payload.nips.NIP_01
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry

@NIP_01
class EventCodecProvider : CodecProvider {
    override fun <T : Any?> get(clazz: Class<T>, registry: CodecRegistry): Codec<T>? {
        if(clazz == Event::class.java){
            return EventCodec() as Codec<T>
        }
        return null
    }
}