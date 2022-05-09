package me.gilles.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.gilles.models.Loan

@Serializable @SerialName("LoanDeclined")
class LoanDeclined(
    override val loan: Loan,
    val reason: String
) : LoanEvent()

fun main() {
    val event = LoanDeclined(Loan.random(), "any reason")

    val json = Json.encodeToString(event)
    println(json)

    val event2 = Json.decodeFromString<LoanDeclined>(json)
    println(event2::class.simpleName)
    println(event2.loan)
    println(event2.reason)
}