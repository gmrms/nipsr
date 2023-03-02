package com.nipsr.relay.service

import com.nipsr.payload.filters.Filter
import com.nipsr.payload.nips.NIP_01
import com.nipsr.payload.service.EventService
import io.smallrye.mutiny.coroutines.asFlow
import javax.enterprise.context.ApplicationScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.bson.Document
import org.bson.conversions.Bson
import kotlin.math.min

@NIP_01
@ApplicationScoped
class RelayEventService : EventService() {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun findByFilters(filter: Filter) =
        collection.aggregate(
            arrayListOf(
                filter.matchAllButTags(),
                filter.project(tempTags = true),
                filter.unwind(),
                filter.matchTags(),
                filter.project(recoverTags = true),
                filter.group(),
                filter.limit()
            )
        ).asFlow()

    fun Filter.matchAllButTags(): Bson {
        val root = Document()
        val document = doc("\$match", root)
        if(!this.ids.isNullOrEmpty()) {
            val filters = arrayListOf<Document>()
            for(id in ids!!){
                filters.add(doc("_id", doc("\$regex", "^$id")))
            }
            root.append("\$or", filters)
        }
        if(!this.authors.isNullOrEmpty()) {
            val filters = arrayListOf<Document>()
            for(author in authors!!){
                filters.add(doc("pubkey", doc("\$regex", "^$author")))
            }
            root.append("\$or", filters)
        }
        if(!this.kinds.isNullOrEmpty()) root.append("kind", doc("\$in", kinds))
        val since = this.since ?: 0
        val until = this.until ?: Long.MAX_VALUE
        root.append("created_at", doc("\$gte", since).append("\$lte", until))
        return document
    }

    fun Filter.project(tempTags: Boolean = false, recoverTags: Boolean = false): Bson {
        val root = Document()
        val document = doc("\$project", root)
        root.append("pubkey", 1)
            .append("kind", 1)
            .append("created_at", 1)
            .append("content", 1)
            .append("sig", 1)
        if(tempTags) {
            root.append("temp", "\$tags")
        }
        if(recoverTags) {
            root.append("tags", "\$temp")
        } else {
            root.append("tags", 1)
        }
        return document
    }

    fun Filter.unwind() = doc("\$unwind", doc("path", "\$tags"))

    fun Filter.matchTags(): Bson {
        if(this.tags.isNullOrEmpty()) return doc("\$match", Document())
        val root = arrayListOf<Document>()
        val document = doc("\$match", doc("\$or", root))
        if(!this.tags.isNullOrEmpty()){
            for(tag in tags!!){
                root.add(
                    doc("tags", doc("\$in", tag))
                        .append("tags.0", tag[0])
                )
            }
        }
        return document
    }

    fun Filter.group() = doc("\$group", doc("_id", "\$_id")
        .append("pubkey", doc("\$first", "\$pubkey"))
        .append("created_at", doc("\$first", "\$created_at"))
        .append("kind", doc("\$first", "\$kind"))
        .append("tags", doc("\$first", "\$tags"))
        .append("content", doc("\$first", "\$content"))
        .append("sig", doc("\$first", "\$sig"))
    )

    fun Filter.limit() =
        doc("\$limit", min(this.limit ?: EventService.DEFAULT_LIMIT, EventService.MAX_LIMIT))
    
}