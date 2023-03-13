package com.nipsr.management.payment.providers.lnbits

import com.nipsr.management.model.Invoice
import com.nipsr.management.model.InvoiceInput
import com.nipsr.management.model.PaymentProvider
import com.nipsr.management.payment.providers.PaymentHandler
import com.nipsr.management.payment.providers.lnbits.payload.LNBItsInvoice
import io.quarkus.arc.properties.IfBuildProperty
import javax.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.rest.client.inject.RestClient

@ApplicationScoped
@IfBuildProperty(name = "$LNBITS_CONFIG.enabled", stringValue = "true")
class LNBitsPaymentHandler(
    @RestClient
    private val client: LNBitsClient
) : PaymentHandler {

    override suspend fun createInvoice(invoiceInput: InvoiceInput): Invoice {
        val lnBitsInvoice = LNBItsInvoice.fromInvoiceInput(invoiceInput)
        val lnBitsInvoiceResponse = client.createInvoice(lnBitsInvoice)
        val invoice = Invoice().apply {
            this.amount = invoiceInput.amount
            this.pubkey = invoiceInput.pubkey
            this.memo = invoiceInput.memo
            this.identifier = invoiceInput.identifier
            this.data = lnBitsInvoiceResponse.payment_request
            this.externalId = lnBitsInvoiceResponse.checking_id
            this.expiration = lnBitsInvoice.expiry
        }
        return invoice
    }

    override suspend fun wasPaid(invoice: Invoice): Boolean {
        return client.getInvoice(invoice.externalId).paid
    }

    override fun provider() = PaymentProvider.LNBits

}