package com.nipsr.management.service

import com.nipsr.management.config.NIP05Config
import com.nipsr.management.model.Identifier
import com.nipsr.management.model.IdentifierRequest
import com.nipsr.management.model.Invoice
import com.nipsr.management.model.payload.AvailabilityResponse
import com.nipsr.management.model.payload.InvoiceInput
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.time.Duration
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.BadRequestException
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter

@ApplicationScoped
class IdentifierService(
    @Channel("identifier-created")
    private val createdEmitter: Emitter<IdentifierMessage>,
    @Channel("identifier-deleted")
    private val deletedEmitter: Emitter<IdentifierMessage>,
    private val paymentService: PaymentService,
    private val niP05Config: NIP05Config,
) {

    companion object {
        const val DEFAULT_IDENTIFIER_DURATION = Long.MAX_VALUE
        val DEFAULT_INVOICE_DURATION = Duration.ofMinutes(5)
    }

    suspend fun create(pubkey: String, identifier: String, domain: String) {
        if(!isAvailable(identifier, domain)) {
            throw BadRequestException("Identifier already taken.")
        }
        Identifier.persist(
            Identifier().apply {
                this.pubkey = pubkey
                this.identifier = identifier
                this.expiration = DEFAULT_IDENTIFIER_DURATION
                this.domain = domain
            }
        ).awaitSuspending()
        createdEmitter.send(IdentifierMessage(pubkey, DEFAULT_IDENTIFIER_DURATION))
    }

    suspend fun delete(identifier: String) {
        val ident = Identifier.find("identifier", identifier).firstResult().awaitSuspending()
            ?: throw BadRequestException("Identifier not found.")

        val allOfPubkey = Identifier.find("pubkey", ident.pubkey).list().awaitSuspending()

        Identifier.deleteById(ident.id!!).awaitSuspending()
        if(allOfPubkey.size - 1 <= 0){
            deletedEmitter.send(IdentifierMessage(ident.pubkey))
        }
    }


    suspend fun findAllIngressByIdentifierAndDomain(identifier: String, domain: String) =
        Identifier.findAllByIdentifierAndDomain(identifier, domain)

    suspend fun checkAvailability(identifier: String, domain: String) = AvailabilityResponse(
        isAvailable(identifier, domain),
        getPrice(identifier)
    )

    private suspend fun isAvailable(identifier: String, domain: String) =
        !Identifier.existsByIdentifierDomain(identifier, domain)

    suspend fun requestAddress(identifierRequest: IdentifierRequest): Invoice {
        val identifier = identifierRequest.identifier
        val domain = identifierRequest.domain
        if(niP05Config.minDigits() > identifier.length){
            throw BadRequestException("Identifier too short. The minimum length is ${niP05Config.minDigits()}.")
        }
        if(!niP05Config.domains().contains(domain)){
            throw BadRequestException("This domain is unavailable for registration. The allowed domains are ${niP05Config.domains()}.")
        }
        if(!isAvailable(identifier, domain)){
            throw BadRequestException("Identifier already taken.")
        }
        val invoiceInput = InvoiceInput(
            pubkey = identifierRequest.pubkey,
            amount = getPrice(identifier),
            duration = DEFAULT_INVOICE_DURATION,
            identifier = identifier,
            memo = "NIP-05 - ${identifier}@${domain}",
            domain = domain
        )
        return paymentService.createInvoice(invoiceInput)
    }

    private fun getPrice(identifier: String) =
        niP05Config.basePrice() / minOf(identifier.length - 1, 6)

    data class IdentifierMessage(
        val pubkey: String,
        val expiration: Long = 0
    )

}