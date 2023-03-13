package com.nipsr.management.payment.providers.lnbits.payload

data class LNBItsInvoiceDetailResponse(
    val paid: Boolean,
    val preimage: String
)