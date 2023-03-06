package com.nipsr.processor.handlers

import com.nipsr.payload.model.events.ContactListEvent
import com.nipsr.payload.nips.NIP_01
import com.nipsr.payload.nips.NIP_02
import com.nipsr.processor.handlers.spec.UniquePerKindEventHandler
import javax.enterprise.context.ApplicationScoped

@NIP_02
@ApplicationScoped
class ContactListEventHandler : UniquePerKindEventHandler<ContactListEvent>() {
    override fun handlesType() = ContactListEvent::class
}