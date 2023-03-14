package com.nipsr.management.service

import com.nipsr.management.config.NIP05Config
import com.nipsr.management.model.payload.AvailabilityResponse
import com.nipsr.management.model.Invoice
import com.nipsr.management.model.payload.InvoiceInput
import com.nipsr.management.model.Identifier
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.time.Duration
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.BadRequestException

@ApplicationScoped
class IdentifierService(
    private val paymentService: PaymentService,
    private val niP05Config: NIP05Config
) {

    suspend fun create(pubkey: String, identifier: String) = Identifier.persist(
        Identifier().apply {
            this.pubkey = pubkey
            this.identifier = identifier
            this.expiration = Long.MAX_VALUE
        }
    ).awaitSuspending()

    suspend fun findAllIngressByIdentifier(identifier: String) = Identifier.findAllByIdentifier(identifier)

    suspend fun checkAvailability(identifier: String) = AvailabilityResponse(
        isAvailable(identifier),
        getPrice(identifier)
    )

    private suspend fun isAvailable(identifier: String) = !Identifier.existsByIdentifier(identifier)

    suspend fun requestAddress(pubkey: String, identifier: String): Invoice {
        if(niP05Config.minDigits() > identifier.length){
            throw BadRequestException("Address too short. The minimum length is ${niP05Config.minDigits()}.")
        }
        if(!isAvailable(identifier)){
            throw BadRequestException("Address already taken.")
        }
        val invoiceInput = InvoiceInput(
            pubkey = pubkey,
            amount = getPrice(identifier),
            duration = Duration.ofDays(1),
            memo = "NIP-05 - $identifier",
            identifier = identifier
        )
        return paymentService.createInvoice(invoiceInput)
    }

    private fun getPrice(identifier: String) =
        niP05Config.basePrice() / minOf(identifier.length - 1, 6)

}