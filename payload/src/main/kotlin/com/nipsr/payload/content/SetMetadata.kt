package com.nipsr.payload.content

import java.net.URI

data class SetMetadata(
    val name: String,
    val about: String,
    val picture: URI
)