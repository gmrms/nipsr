package com.nipsr.management.payment

import com.nipsr.management.model.Invoice
import com.nipsr.management.model.InvoiceInput
import java.time.Duration
import java.util.*

abstract class InvoiceResponse {
    open lateinit var payment_request: String
    open lateinit var checking_id: String

    fun toInvoice(input: InvoiceInput) = Invoice().apply {
        this.amount = input.amount
        this.pubkey = input.pubkey
        this.memo = input.memo
        this.identifier = input.identifier
        this.expiration = input.duration.secondsFromNow()
        this.data = payment_request
        this.externalId = checking_id
    }

    companion object {

        fun Duration.secondsFromNow(): Long {
            return Calendar.getInstance().timeInMillis / 1000 + this.toSeconds()
        }

    }

}