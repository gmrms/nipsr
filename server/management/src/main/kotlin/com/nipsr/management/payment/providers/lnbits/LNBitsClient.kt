package com.nipsr.management.payment.providers.lnbits

import com.nipsr.management.payment.providers.lnbits.payload.LNBItsInvoice
import com.nipsr.management.payment.providers.lnbits.payload.LNBItsInvoiceDetailResponse
import com.nipsr.management.payment.providers.lnbits.payload.LNBItsInvoiceResponse
import io.quarkus.arc.properties.IfBuildProperty
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(configKey = "lnbits")
@RegisterClientHeaders(LNBitsClientHeadersFactory::class)
@IfBuildProperty(name = "$LNBITS_CONFIG.enabled", stringValue = "true")
interface LNBitsClient {

        @POST
        @Path("/payments")
        fun createInvoice(invoice: LNBItsInvoice): LNBItsInvoiceResponse

        @GET
        @Path("/payments/{id}")
        fun getInvoice(@PathParam("id") id: String): LNBItsInvoiceDetailResponse

}