package com.nipsr.relay.handlers.spec

/**
 * Wrapper for a map of message parts with helper methods to get and convert them.
 */
class MessageParts(iterable: Iterable<Any>) : ArrayList<Any>() {

    constructor() : this(emptyList())
    constructor(vararg elements: Any) : this(elements.asIterable())

    init {
        this.addAll(iterable)
    }

    inline fun <reified T> getAndConvert(index: IntRange, converter: (obj: List<Any>) -> T): T {
        return converter(this[index])
    }

    operator fun get(range: IntRange): List<Any> {
        val max = if(range.last > size - 1) {
            size - 1
        } else {
            range.last
        }
        return subList(range.first, max + 1)
    }

    companion object {
        fun fromList(list: List<Any>) = MessageParts().apply {
            addAll(list)
        }
    }
}