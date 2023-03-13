package com.nipsr.management.model

import java.time.Duration

data class InvoiceInput(
    val pubkey: String,
    val amount: Long,
    val duration: Duration,
    val memo: String,
    val identifier: String
)