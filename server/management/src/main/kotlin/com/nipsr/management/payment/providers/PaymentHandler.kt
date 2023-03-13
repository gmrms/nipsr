package com.nipsr.management.payment.providers

import com.nipsr.management.model.Invoice
import com.nipsr.management.model.InvoiceInput
import com.nipsr.management.model.PaymentProvider

interface PaymentHandler {
    suspend fun createInvoice(invoice: InvoiceInput): Invoice
    suspend fun wasPaid(invoice: Invoice): Boolean
    fun provider(): PaymentProvider
}