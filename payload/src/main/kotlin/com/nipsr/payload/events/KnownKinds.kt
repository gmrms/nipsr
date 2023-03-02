package com.nipsr.payload.events

import com.nipsr.payload.Constants
import com.nipsr.payload.content.SetMetadata
import com.nipsr.payload.nips.NIP_01
import kotlin.reflect.KClass

@NIP_01
enum class KnownKinds(val code: Int, val description: String, val type: KClass<*>, val contentType: KClass<*>) {
    SET_METADATA(0, Constants.SET_METADATA, SetMetadataEvent::class, SetMetadata::class),
    TEXT_NOTE(1, Constants.TEXT_NOTE, TextNoteEvent::class, String::class),
    RECOMMEND_SERVER(2, Constants.RECOMMEND_SERVER, RecommendServerEvent::class, String::class);

    companion object {
        fun fromCode(code: Int): KnownKinds? {
            return values().find { it.code == code }
        }
        fun fromDescription(description: String): KnownKinds? {
            return values().find { it.description == description }
        }
    }
}