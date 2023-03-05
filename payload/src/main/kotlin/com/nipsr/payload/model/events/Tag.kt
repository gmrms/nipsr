package com.nipsr.payload.model.events

data class Tag(
    val tag: String,
    val value: String,
    val options: List<String>
) {

    companion object {
        fun fromList(values: List<String>) = Tag(values[0], values.hasIndexOrEmpty(1), values.hasOptionsOrEmpty(2))
        private fun List<String>.hasIndexOrEmpty(index: Int) = if (this.size > index) this[index] else ""
        private fun List<String>.hasOptionsOrEmpty(size: Int) = if (this.size > size) this.subList(size, this.size) else emptyList()
    }

}