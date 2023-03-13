package com.nipsr.management.service

import com.nipsr.management.config.NIP05Config
import com.nipsr.management.model.AvailabilityResponse
import com.nipsr.management.model.Invoice
import com.nipsr.management.model.InvoiceInput
import com.nipsr.management.model.RelayIngress
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.time.Duration
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.BadRequestException

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

    suspend fun findAllIngressByAddress(address: String) = RelayIngress.findAllByIdentifier(address)

    suspend fun checkAvailability(address: String) = AvailabilityResponse(
        isAvailable(address),
        getPrice(address)
    )

    private suspend fun isAvailable(address: String) = RelayIngress.existsByIdentifier(address)

    suspend fun requestAddress(pubkey: String, address: String): Invoice {
        if(niP05Config.minDigits() > address.length){
            throw BadRequestException("Address too short. The minimum length is ${niP05Config.minDigits()}.")
        }
        if(!isAvailable(address)){
            throw BadRequestException("Address already taken.")
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