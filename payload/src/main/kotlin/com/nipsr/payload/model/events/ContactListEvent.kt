package com.nipsr.payload.model.events

import com.nipsr.payload.nips.NIP_01

@NIP_01
class ContactListEvent : Event<String>() {
    override fun isReplaceable() = true
}