package com.nipsr.payload.model.inputs

import com.nipsr.payload.nips.NIP_01
import kotlin.properties.Delegates

@NIP_01
class EventInput {
    lateinit var id: String
    lateinit var pubkey: String
    var created_at by Delegates.notNull<Long>()
    var kind by Delegates.notNull<Int>()
    var tags: ArrayList<ArrayList<String>> = arrayListOf()
    var content: String = ""
    lateinit var sig: String
}