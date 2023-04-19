package com.nipsr.management.controller

import com.nipsr.management.model.Invoice
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.NotFoundException
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import org.bson.types.ObjectId

@ApplicationScoped
@Path("/invoice")
class InvoiceController {

    @GET
    @Path("/{id}")
    suspend fun isPaid(
        @PathParam("id") id: ObjectId
    ) = Invoice.findById(id).awaitSuspending()?.paid ?: throw NotFoundException("Invoice not found")

}