package com.nipsr.payload.model.events

import com.nipsr.payload.nips.NIP_01

@NIP_01
class TextNoteEvent : Event<String>(){
    override fun isRegular() = true
}