package me.gilles.models

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class LoanIdTest : StringSpec({
    "LoanId should serialize to a string" {
        val id = "&efg@yt§"
        val loanId = LoanId(id)
        Json.encodeToString(LoanId.serializer(), loanId) shouldBe "\"$id\""
    }

    "LoanId should be de/serializable" {
        val id = "&efg@yt§"
        val loanId = LoanId(id)
        val json = Json.encodeToString(LoanId.serializer(), loanId)

        val loanId2 = Json.decodeFromString(LoanId.serializer(), json)
        loanId shouldBe loanId2
    }
})