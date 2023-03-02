package com.nipsr.payload.codec

import com.fasterxml.jackson.core.type.TypeReference
import com.mongodb.MongoClientSettings
import com.nipsr.payload.ObjectMapperUtils.mapTo
import com.nipsr.payload.content.SetMetadata
import com.nipsr.payload.events.Event
import com.nipsr.payload.events.KnownKinds
import com.nipsr.payload.events.RecommendServerEvent
import com.nipsr.payload.events.SetMetadataEvent
import com.nipsr.payload.events.TextNoteEvent
import com.nipsr.payload.nips.NIP_01
import org.bson.BsonReader
import org.bson.BsonString
import org.bson.BsonValue
import org.bson.BsonWriter
import org.bson.Document
import org.bson.codecs.CollectibleCodec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

@NIP_01
class EventCodec : CollectibleCodec<Event<*>> {

    private val documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document::class.java)

    override fun encode(writer: BsonWriter, value: Event<*>, encoderContext: EncoderContext) {
        TODO("Not yet implemented")
    }

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): Event<*> {
        val document = documentCodec.decode(reader, decoderContext)
        val kind = KnownKinds.fromCode(document["kind"] as Int)
        val event = buildEventOfKind(kind, document)
        setEventData(event, document)
        return event
    }

    private fun buildEventOfKind(kind: KnownKinds?, document: Document): Event<*> {
        val content = document["content"]
        return when(kind){
            KnownKinds.TEXT_NOTE -> TextNoteEvent().apply {
                this.content = convertDocument<String>(content, kind)
            }
            KnownKinds.SET_METADATA -> SetMetadataEvent().apply {
                this.content = convertDocument<SetMetadata>(content, kind)
            }
            KnownKinds.RECOMMEND_SERVER -> RecommendServerEvent().apply {
                this.content = convertDocument<String>(content, kind)
            }
            else -> throw IllegalArgumentException("Unknown event type")
        }
    }

    private fun setEventData(event: Event<*>, document: Document){
        event.id = document["_id"] as String
        event.pubkey = document["pubkey"] as String
        event.created_at = document["created_at"] as Long
        event.kind = document["kind"] as Int
        event.tags = convertTags(document["tags"])
        event.sig = document["sig"] as String
    }

    private fun convertTags(tags: Any?) = convertDocument(
        tags as ArrayList<ArrayList<String>>?, object : TypeReference<ArrayList<ArrayList<String>>>() {
            override fun getType() = ArrayList<ArrayList<String>>().javaClass
        })

    private inline fun <reified T> convertDocument(document: Any?, kinds: KnownKinds): T? {
        return convertDocument(document, object : TypeReference<T>() {
            override fun getType() = kinds.contentType.java
        })
    }

    private inline fun <reified T> convertDocument(document: Any?, typeReference: TypeReference<T>): T? {
        if(document == null) return null
        return document.mapTo(typeReference)
    }

    override fun getDocumentId(document: Event<*>): BsonValue {
        return BsonString(document.id)
    }

    override fun documentHasId(document: Event<*>): Boolean {
        return true
    }

    override fun getEncoderClass() = Event::class.java

    override fun generateIdIfAbsentFromDocument(document: Event<*>): Event<*> {
        return document
    }

}
