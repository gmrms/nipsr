package com.nipsr.payload.codec

import com.mongodb.MongoClientSettings
import com.nipsr.payload.ObjectMapperUtils.mapTo
import com.nipsr.payload.model.events.Event
import com.nipsr.payload.model.KnownKinds
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
        throw IllegalAccessException("Should not be used")
    }

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): Event<*> {
        val document = documentCodec.decode(reader, decoderContext)
        val kind = KnownKinds.fromCode(document["kind"] as Int)
        document["id"] = document["_id"]
        document.remove("_id")
        return document.mapTo(kind.type.java) as Event<*>
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
