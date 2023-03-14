package com.nipsr.relay.validation.validators

import com.nipsr.payload.Constants.EVENT_TAG
import com.nipsr.payload.model.events.Event
import com.nipsr.payload.nips.NIP_09
import com.nipsr.relay.validation.EventValidator
import javax.enterprise.context.ApplicationScoped

@NIP_09
@ApplicationScoped
class DeletionValidator : EventValidator {
    override fun validate(event: Event<*>): Boolean {
        event.content = ""
        return event.tags.any { it.tag == EVENT_TAG }
    }

    override fun kindsToValidate(): IntRange {
        return 5..5
    }

}