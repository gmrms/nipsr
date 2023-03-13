package com.nipsr.management.payment

import com.nipsr.management.model.Invoice
import com.nipsr.management.model.InvoiceInput
import com.nipsr.management.payment.providers.PaymentHandler
import com.nipsr.management.service.RelayIngressService
import io.quarkus.scheduler.Scheduled
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Instance
import org.slf4j.LoggerFactory

@ApplicationScoped
class PaymentService(
    private val paymentHandlers: Instance<PaymentHandler>,
    private val relayIngressService: RelayIngressService
) {

    val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun createInvoice(invoiceInput: InvoiceInput) : Invoice {
        var invoice: Invoice? = null
        for(paymentHandler in paymentHandlers) {
            try {
                invoice = paymentHandler.createInvoice(invoiceInput)
            } catch (e: Exception) {
                logger.error("Error creating invoice with ${paymentHandler.provider()}", e)
            }
            if(invoice != null) {
                invoice.paymentProvider = paymentHandler.provider()
                break
            }
        }
        if(invoice == null) {
            throw Exception("No payment handler available")
        }
        return invoice.persist<Invoice>().awaitSuspending()
    }

    private suspend fun onPaymentSuccess(invoice: Invoice){
        relayIngressService.create(
            invoice.pubkey, invoice.identifier
        )
    }

    @Scheduled(every = "15s")
    suspend fun checkInvoice() {
        val notPaidInvoices = Invoice.find("paid", false).list().awaitSuspending()
        for(invoice in notPaidInvoices){
            if(invoice.expiration < System.currentTimeMillis() / 1000) {
                invoice.delete().awaitSuspending()
                continue
            }
            for(paymentHandler in paymentHandlers) {
                if(paymentHandler.provider() == invoice.paymentProvider) {
                    try {
                        val paid = paymentHandler.wasPaid(invoice)
                        if(paid) {
                            invoice.paid = true
                            invoice.update<Invoice>().awaitSuspending()
                            onPaymentSuccess(invoice)
                        }
                    } catch (e: Exception) {
                        logger.error("Error checking invoice with ${paymentHandler.provider()}", e)
                    }
                }
            }
        }
    }

}