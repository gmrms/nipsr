package com.nipsr.payload.model

import com.nipsr.payload.Constants
import com.nipsr.payload.content.SetMetadata
import com.nipsr.payload.model.events.ContactListEvent
import com.nipsr.payload.model.events.RecommendServerEvent
import com.nipsr.payload.model.events.SetMetadataEvent
import com.nipsr.payload.model.events.TextNoteEvent
import com.nipsr.payload.model.events.UnknownEvent
import com.nipsr.payload.nips.NIP_01
import com.nipsr.payload.nips.NIP_02
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@NIP_01
enum class KnownKinds(
    val kind: Int,
    val description: String,
    val type: KClass<*>,
    val contentType: KClass<*> = String::class
) {
    UNKNOWN(-1, Constants.RECOMMEND_SERVER, UnknownEvent::class),
    @NIP_01
    SET_METADATA(0, Constants.SET_METADATA, SetMetadataEvent::class, SetMetadata::class),
    TEXT_NOTE(1, Constants.TEXT_NOTE, TextNoteEvent::class),
    RECOMMEND_SERVER(2, Constants.RECOMMEND_SERVER, RecommendServerEvent::class),
    @NIP_02
    CONTACT_LIST(3, Constants.CONTACT_LIST, ContactListEvent::class);

    companion object {
        fun fromCode(code: Int): KnownKinds {
            return values().find { it.kind == code } ?: UNKNOWN
        }
    }
}