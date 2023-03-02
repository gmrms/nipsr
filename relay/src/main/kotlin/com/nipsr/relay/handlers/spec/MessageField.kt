package com.nipsr.relay.handlers.spec

import kotlin.reflect.KClass

data class MessageField(
    val name: String,
    val type: KClass<*>,
    var vararg: Boolean = false
)