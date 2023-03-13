package com.nipsr.management.payment.providers.lnbits.payload

data class LNBItsInvoiceResponse(
    var payment_hash: String,
    var payment_request: String,
    var checking_id: String,
    var lnurl_response: String? = null
)