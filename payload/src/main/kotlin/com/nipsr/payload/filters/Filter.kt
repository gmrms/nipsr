package com.nipsr.payload.filters

import com.nipsr.payload.nips.NIP_01

@NIP_01
data class Filter(
    val ids: List<String>? = null,
    val authors: List<String>? = null,
    val kinds: List<Int>? = null,
    val tags: ArrayList<ArrayList<String>>? = null,
    val since: Long? = null,
    val until: Long? = null,
    val limit: Int? = null
)