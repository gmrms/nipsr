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
}