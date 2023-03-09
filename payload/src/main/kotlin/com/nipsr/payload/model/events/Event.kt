package com.nipsr.payload.model.events

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.nipsr.payload.ObjectMapperUtils.objectMapper
import com.nipsr.payload.model.EventType
import com.nipsr.payload.model.KnownKinds
import com.nipsr.payload.model.inputs.EventInput
import com.nipsr.payload.nips.NIP_01
import com.nipsr.payload.nips.NIP_16
import org.bson.codecs.pojo.annotations.BsonId
import kotlin.properties.Delegates

@NIP_01
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY,
    property = "kind", visible = true,
    defaultImpl = UnknownEvent::class
)
@JsonSubTypes(
    JsonSubTypes.Type(value = SetMetadataEvent::class, name = "0"),
    JsonSubTypes.Type(value = TextNoteEvent::class, name = "1"),
    JsonSubTypes.Type(value = RecommendServerEvent::class, name = "2"),
    JsonSubTypes.Type(value = ContactListEvent::class, name = "3"),
    JsonSubTypes.Type(value = DeletionEvent::class, name = "5"),
)
sealed class Event<T> {

    @BsonId
    lateinit var id: String
    lateinit var pubkey: String
    var created_at by Delegates.notNull<Long>()
    var kind by Delegates.notNull<Int>()
    var tags: ArrayList<Tag> = arrayListOf()
    var content: String = ""
    lateinit var sig: String

    @NIP_16
    fun readEventType() = EventType.fromKind(kind)

    fun readContent() : T = objectMapper.readValue(content, KnownKinds.fromCode(kind).contentType.java) as T

    fun fromDTO(dto: EventInput) : Event<T> {
        id = dto.id
        pubkey = dto.pubkey
        created_at = dto.created_at
        kind = dto.kind
        tags = dto.tags.map { Tag.fromList(it) } as ArrayList<Tag>
        content = dto.content
        sig = dto.sig
        return this
    }

}