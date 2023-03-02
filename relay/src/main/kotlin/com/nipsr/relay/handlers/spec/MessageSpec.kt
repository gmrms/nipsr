package com.nipsr.relay.handlers.spec

import com.nipsr.payload.Constants.MESSAGE_TYPE

/**
 * A specification of a message.
 * Defines the fields and their indexes in a message.
 */
class MessageSpec : HashMap<String, IntRange>() {

    init {
        put(MESSAGE_TYPE, 0)
    }

    fun put(name: String, singular: Int) {
        this[name] = singular..singular
    }
}