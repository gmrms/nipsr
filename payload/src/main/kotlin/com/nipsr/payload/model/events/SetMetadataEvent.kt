package com.nipsr.payload.model.events

import com.nipsr.payload.content.SetMetadata
import com.nipsr.payload.nips.NIP_01

@NIP_01
class SetMetadataEvent : Event<SetMetadata>(){
    override fun isReplaceable() = true
}