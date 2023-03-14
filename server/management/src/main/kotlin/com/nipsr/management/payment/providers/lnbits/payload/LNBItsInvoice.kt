package com.nipsr.management.payment.providers.lnbits.payload

import com.nipsr.management.model.payload.InvoiceInput
import com.nipsr.management.payment.InvoiceResponse.Companion.secondsFromNow

data class LNBItsInvoice(
    val amount: Long,
    val memo: String,
    val expiry: Long,
    val unit: String = "sat",
    val out: Boolean = false
) {
    companion object {
        fun fromInvoiceInput(invoiceInput: InvoiceInput): LNBItsInvoice {
            return LNBItsInvoice(
                amount = invoiceInput.amount,
                memo = invoiceInput.memo,
                expiry = invoiceInput.duration.secondsFromNow()
            )
        }
    }
}