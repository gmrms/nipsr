package com.nipsr.payload.model.events

import com.nipsr.payload.nips.NIP_01
import org.bson.codecs.pojo.annotations.BsonIgnore

@NIP_01
class TextNoteEvent : Event<String>(){
    @BsonIgnore
    override fun isRegular() = true
}