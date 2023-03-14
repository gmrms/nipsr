package com.nipsr.relay.validation

import com.nipsr.payload.model.events.Event

interface EventValidator {
    fun validate(event: Event<*>): Boolean
    fun kindsToValidate(): IntRange
}