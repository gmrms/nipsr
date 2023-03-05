package com.nipsr.payload.model.inputs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

data class TagInput(
    val tag: String,
    val value: String,
    val options: List<String>
) {

    @JsonValue
    fun toList() = listOf(tag, value) + options

    companion object {
        @JvmStatic
        @JsonCreator
        fun fromList(values: List<String>) = TagInput(values[0], values.hasIndexOrEmpty(1), values.hasOptionsOrEmpty(2))

        private fun List<String>.hasIndexOrEmpty(index: Int) = if (this.size > index) this[index] else ""
        private fun List<String>.hasOptionsOrEmpty(size: Int) = if (this.size > size) this.subList(size, this.size) else emptyList()
    }

}