package com.nipsr.management.model.payload

import java.time.Duration

data class InvoiceInput(
    val pubkey: String,
    val amount: Long,
    val duration: Duration,
    val memo: String,
    val identifier: String
)