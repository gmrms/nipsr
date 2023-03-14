package com.nipsr.management.model.payload

data class AvailabilityResponse(
    val available: Boolean,
    val price: Long
)