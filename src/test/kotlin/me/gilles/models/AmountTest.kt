package me.gilles.models

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class AmountTest : StringSpec({
    "Amount should be serializable" {
        val amount = Amount.random()
        val json = Json.encodeToString(Amount.serializer(), amount)

        val amount2 = Json.decodeFromString(Amount.serializer(), json)
        amount shouldBe amount2
    }
})