package com.nipsr.management.payment.providers.lnbits.payload

import com.nipsr.management.model.InvoiceInput
import java.util.*

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
                expiry = Calendar.getInstance().timeInMillis / 1000 + invoiceInput.duration.toSeconds()
            )
        }
    }
}