package com.nipsr.relay.model

import com.nipsr.payload.model.Filter

data class Subscription(
    val id: String,
    val filters: Iterable<Filter>? = null
) {

    init {
        require(id.isNotBlank() && id.length <= 64) { "Invalid subscription Id" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Subscription

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}