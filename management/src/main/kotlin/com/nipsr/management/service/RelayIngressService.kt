package com.nipsr.management.service

import com.nipsr.management.config.NIP05Config
import com.nipsr.management.model.AvailabilityResponse
import com.nipsr.management.model.Invoice
import com.nipsr.management.model.InvoiceInput
import com.nipsr.management.model.RelayIngress
import com.nipsr.management.payment.PaymentService
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.time.Duration
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RelayIngressService(
    private val paymentService: PaymentService,
    private val niP05Config: NIP05Config
) {

    suspend fun create(pubkey: String, address: String) = RelayIngress.persist(
        RelayIngress().apply {
            this.pubkey = pubkey
            this.identifier = address
            this.expiration = Long.MAX_VALUE
        }
    ).awaitSuspending()

    suspend fun findAllIngressByName(name: String) =
        RelayIngress.find("identifier", name)
            .list().awaitSuspending()

    suspend fun checkAvailability(address: String) = AvailabilityResponse(
        RelayIngress.find("identifier", address)
            .count().awaitSuspending() == 0L,
        getPrice(address)
    )

    suspend fun requestAddress(pubkey: String, address: String): Invoice {
        if(niP05Config.minDigits() > address.length){
            throw Exception("Address too short the minimum is ${niP05Config.minDigits()}")
        }
        val invoiceInput = InvoiceInput(
            pubkey = pubkey,
            amount = getPrice(address),
            duration = Duration.ofDays(1),
            memo = "NIP-05 - $address",
            identifier = address
        )
        return paymentService.createInvoice(invoiceInput)
    }

    private fun getPrice(address: String) =
        niP05Config.basePrice() / minOf(address.length - 1, 6)

}