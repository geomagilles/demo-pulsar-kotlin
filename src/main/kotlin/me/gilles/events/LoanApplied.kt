package me.gilles.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.gilles.models.Loan

@Serializable @SerialName("LoanApplied")
class LoanApplied(
    override val loan: Loan
) : LoanEvent()

fun main() {
    val event = LoanApplied(Loan.random())

    val json = Json.encodeToString(event)
    println(json)

    val event2: LoanApplied = Json.decodeFromString(json)
    println(event2::class.simpleName)
    println(event2.loan)
}