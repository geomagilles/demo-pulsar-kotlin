package me.gilles.models

import kotlinx.serialization.Serializable
import java.util.UUID

@JvmInline @Serializable
value class LoanId(private val id: String = UUID.randomUUID().toString())