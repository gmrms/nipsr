package com.nipsr.relay.validation.validators

import com.nipsr.payload.Constants.PUBKEY_TAG
import com.nipsr.payload.model.events.Event
import com.nipsr.payload.nips.NIP_02
import com.nipsr.relay.validation.EventValidator
import javax.enterprise.context.ApplicationScoped

@NIP_02
@ApplicationScoped
class ContactListValidator : EventValidator {
    override fun validate(event: Event<*>): Boolean {
        event.content = ""
        return event.tags.any { it.tag == PUBKEY_TAG }
    }

    override fun kindsToValidate(): IntRange {
        return 3..3
    }
}