package com.nipsr.payload

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object ObjectMapperUtils {

    val objectMapper = jacksonObjectMapper()

    fun <T : Any, D> T.mapTo(type: Class<D>): D = objectMapper.convertValue(this, type)
    fun <D> String.mapTo(type: Class<D>): D = objectMapper.readValue(this, type)
    fun <T : Any, D> T.mapTo(type: TypeReference<D>): D = objectMapper.convertValue(this, type)
    fun <D> String.mapTo(type: TypeReference<D>): D = objectMapper.readValue(this, type)
    fun <T : Any> T.toJsonString(): String = objectMapper.writeValueAsString(this)
    fun <T : Any> T.toJsonByteArray(): ByteArray = objectMapper.writeValueAsBytes(this)

}