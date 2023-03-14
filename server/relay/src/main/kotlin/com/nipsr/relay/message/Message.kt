package com.nipsr.relay.message

/**
 * Represents the messages sent by and to a client.
 */
class Message(
    list: List<Any>
) : ArrayList<Any>() {

    init {
        addAll(list)
    }

    constructor(vararg parts: Any) : this(parts.asList())

}