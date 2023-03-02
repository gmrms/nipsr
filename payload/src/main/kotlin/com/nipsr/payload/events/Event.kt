package com.nipsr.payload.events

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.nipsr.payload.nips.NIP_01
import org.bson.codecs.pojo.annotations.BsonId
import kotlin.properties.Delegates

@NIP_01
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "kind",
    visible = true,
)
@JsonSubTypes(
    JsonSubTypes.Type(value = SetMetadataEvent::class, name = "0"),
    JsonSubTypes.Type(value = TextNoteEvent::class, name = "1"),
    JsonSubTypes.Type(value = RecommendServerEvent::class, name = "2"),
)
sealed class Event<T> {

    @BsonId
    lateinit var id: String
    lateinit var pubkey: String

    var created_at by Delegates.notNull<Long>()
    var kind by Delegates.notNull<Int>()

    var tags: ArrayList<ArrayList<String>>? = null

    open var content: T? = null

    lateinit var sig: String
}