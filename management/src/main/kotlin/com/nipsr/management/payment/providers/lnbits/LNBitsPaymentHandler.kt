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
    private val lnBitsClient: LNBitsClient
) : PaymentHandler {

    override suspend fun createInvoice(invoice: InvoiceInput): Invoice {
        val lnBitsInvoice = LNBItsInvoice.fromInvoiceInput(invoice)
        val lnBitsInvoiceResponse = lnBitsClient.createInvoice(lnBitsInvoice)
        return lnBitsInvoiceResponse.toInvoice(invoice)
    }

    override suspend fun wasPaid(invoice: Invoice): Boolean {
        return lnBitsClient.getInvoice(invoice.externalId).paid
    }

    override fun provider() = PaymentProvider.LNBits

}