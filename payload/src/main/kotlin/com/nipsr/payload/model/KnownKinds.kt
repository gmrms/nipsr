package com.nipsr.payload.model

import com.nipsr.payload.Constants
import com.nipsr.payload.content.SetMetadata
import com.nipsr.payload.model.events.RecommendServerEvent
import com.nipsr.payload.model.events.SetMetadataEvent
import com.nipsr.payload.model.events.TextNoteEvent
import com.nipsr.payload.model.events.UnknownEvent
import com.nipsr.payload.nips.NIP_01
import kotlin.reflect.KClass

@NIP_01
enum class KnownKinds(val kind: Int, val description: String, val type: KClass<*>, val contentType: KClass<*>) {
    UNKNOWN(-1, Constants.RECOMMEND_SERVER, UnknownEvent::class, String::class),
    @NIP_01
    SET_METADATA(0, Constants.SET_METADATA, SetMetadataEvent::class, SetMetadata::class),
    TEXT_NOTE(1, Constants.TEXT_NOTE, TextNoteEvent::class, String::class),
    RECOMMEND_SERVER(2, Constants.RECOMMEND_SERVER, RecommendServerEvent::class, String::class);

    companion object {
        fun fromCode(code: Int): KnownKinds {
            return values().find { it.kind == code } ?: UNKNOWN
        }
        fun fromDescription(description: String): KnownKinds? {
            return values().find { it.description == description }
        }
    }
}