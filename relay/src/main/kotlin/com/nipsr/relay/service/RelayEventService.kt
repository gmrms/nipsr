package com.nipsr.relay.service

import com.nipsr.payload.model.Filter
import com.nipsr.payload.nips.NIP_01
import com.nipsr.payload.nips.NIP_12
import com.nipsr.payload.service.EventService
import io.quarkus.mongodb.FindOptions
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
        collection.find(filter.toBSON(), FindOptions().limit(filter.limit())).asFlow()

    @NIP_12
    fun Filter.toBSON(): Bson {

        val root = Document()

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

        if(!this.tags.isNullOrEmpty()) {
            for(tag in tags!!){
                root.append("tags", doc("\$elemMatch", doc("tag", tag[0]).append("value", doc("\$in", tag.subList(1, tag.size)))))
            }
        }

        if(since == null && until == null) return root
        val since = this.since ?: 0
        val until = this.until ?: Long.MAX_VALUE
        root.append("created_at", doc("\$gte", since).append("\$lte", until))

        return root
    }

    fun Filter.limit() = min(this.limit ?: DEFAULT_LIMIT, MAX_LIMIT)
    
}