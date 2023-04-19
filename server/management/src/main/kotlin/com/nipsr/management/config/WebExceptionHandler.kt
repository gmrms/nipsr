package com.nipsr.management.config

import java.util.*
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class WebExceptionHandler : ExceptionMapper<WebApplicationException> {
    override fun toResponse(exception: WebApplicationException): Response =
        Response.status(exception.response.status)
            .entity(ErrorMessage(exception.message ?: "Unknown error"))
            .build()

    class ErrorMessage(val message: String) {
        val timestamp = Date().time
    }

}