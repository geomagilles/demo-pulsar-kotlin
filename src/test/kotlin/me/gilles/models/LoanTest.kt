package me.gilles.models

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class LoanTest : StringSpec({
    "Loan should be de/serializable" {
        val loan = Loan.random()
        val json = Json.encodeToString(Loan.serializer(), loan)

        val loan2 = Json.decodeFromString(Loan.serializer(), json)
        loan shouldBe loan2
    }
})