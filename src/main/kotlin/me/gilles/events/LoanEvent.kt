package me.gilles.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.gilles.models.Loan

@Serializable
sealed class LoanEvent {
    abstract val loan: Loan

    open fun toByteArray() = Json.encodeToString(this).toByteArray()

    companion object {
        fun fromByteArray(bytes: ByteArray): LoanEvent = Json.decodeFromString(String(bytes))
    }
}

fun main() {
    val event: LoanEvent = LoanApplied(Loan.random())

    val bytes = event.toByteArray()
    println(String(bytes))

    val event2: LoanEvent = LoanEvent.fromByteArray(bytes)
    println(event2::class.simpleName)
    println(event2.loan)
}