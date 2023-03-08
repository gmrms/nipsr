package com.nipsr.payload.model.events

import com.nipsr.payload.content.SetMetadata
import com.nipsr.payload.nips.NIP_01
import org.bson.codecs.pojo.annotations.BsonIgnore

@NIP_01
class SetMetadataEvent : Event<SetMetadata>(){
    @BsonIgnore
    override fun isReplaceable() = true
}