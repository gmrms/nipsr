package com.nipsr.payload.model.events

import com.nipsr.payload.nips.NIP_09

@NIP_09
class DeletionEvent : Event<String>(){
    override fun isRegular() = true
}