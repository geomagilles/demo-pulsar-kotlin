package me.gilles.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.gilles.models.Loan

@Serializable @SerialName("LoanAccepted")
class LoanAccepted(
    override val loan: Loan
) : LoanEvent()

fun main() {
    val event = LoanAccepted(Loan.random())

    val json = Json.encodeToString(event)
    println(json)

    val event2 = Json.decodeFromString<LoanAccepted>(json)
    println(event2::class.simpleName)
    println(event2.loan)
}