package com.nipsr.management.service

import com.nipsr.management.model.Invoice
import com.nipsr.management.model.payload.InvoiceInput
import com.nipsr.management.payment.providers.PaymentHandler
import io.quarkus.scheduler.Scheduled
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Instance
import javax.ws.rs.InternalServerErrorException
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory

@ApplicationScoped
class PaymentService(
    private val paymentHandlers: Instance<PaymentHandler>,
    private val identifierService: IdentifierService
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
            throw InternalServerErrorException("No payment handler available")
        }
        return invoice.persist<Invoice>().awaitSuspending()
    }

    private suspend fun onPaymentSuccess(invoice: Invoice){
        identifierService.create(
            invoice.pubkey, invoice.identifier, invoice.domain
        )
    }

    @Scheduled(every = "5s")
    suspend fun checkInvoice() {
        val notPaidInvoices = Invoice.findAllNotPaid()
        for(invoice in notPaidInvoices){
            try {
                val handler = paymentHandlers.first { it.provider() == invoice.paymentProvider }
                val paid = handler.wasPaid(invoice)
                if(paid) {
                    invoice.paid = true
                    invoice.update<Invoice>().awaitSuspending()
                    onPaymentSuccess(invoice)
                } else {
                    if(invoice.expiration < System.currentTimeMillis() / 1000) {
                        invoice.delete().awaitSuspending()
                    }
                }
            } catch (e: Exception) {
                logger.error("Error checking invoice with ${invoice.paymentProvider}", e)
            }
            // Delay requests so it doesn't get rate limited
            delay(1000)
        }
    }

}