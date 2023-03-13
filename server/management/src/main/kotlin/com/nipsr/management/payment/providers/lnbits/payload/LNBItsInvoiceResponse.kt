package com.nipsr.management.payment.providers.lnbits.payload

import com.nipsr.management.payment.InvoiceResponse

data class LNBItsInvoiceResponse(
    override var payment_request: String,
    override var checking_id: String,
    var payment_hash: String,
    var lnurl_response: String? = null
) : InvoiceResponse()