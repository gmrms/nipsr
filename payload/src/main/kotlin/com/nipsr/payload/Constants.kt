package com.nipsr.payload

object Constants {
    const val MESSAGE_TYPE = "_type"
    const val EVENT = "event"
    const val SUBSCRIPTION_ID = "subscription_id"
    const val FILTERS = "filters"
    const val TAGS = "tags"

    // Discriminators
    const val TEXT_NOTE = "text_note"
    const val SET_METADATA = "set_metadata"
    const val RECOMMEND_SERVER = "recommend_server"
    const val CONTACT_LIST = "contact_list"
    const val DELETION = "deletion"

    // Tags
    const val EVENT_TAG = "e"
    const val PUBKEY_TAG = "p"
    const val NONCE_TAG = "nonce"

    // Events Types
    val REGULAR_EVENTS_RANGE = 1000 until 10000
    val REPLACEABLE_EVENTS_RANGE = 10000 until 20000
    val EPHEMERAL_EVENTS_RANGE = 20000 until 30000
}