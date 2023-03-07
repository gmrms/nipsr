package com.nipsr.relay.exeptions

open class RelayException(message: String) : RuntimeException(message) {
    constructor(message: String, cause: Throwable) : this(message) {
        initCause(cause)
    }
}