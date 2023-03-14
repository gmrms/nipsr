package com.nipsr.management.model

import com.nipsr.management.model.enums.PaymentProvider
import io.quarkus.mongodb.panache.common.MongoEntity
import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoCompanion
import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoEntity
import io.smallrye.mutiny.coroutines.awaitSuspending


@MongoEntity(collection = "invoice")
class Invoice : ReactivePanacheMongoEntity() {
    lateinit var memo: String
    lateinit var pubkey: String
    lateinit var identifier: String
    lateinit var externalId: String
    lateinit var data: String
    lateinit var paymentProvider: PaymentProvider

    var amount: Long = 0
    var expiration: Long = 0

    var paid = false

    companion object : ReactivePanacheMongoCompanion<Invoice> {
        suspend fun findAllNotPaid() =
            find("paid", false).list().awaitSuspending()
    }
}