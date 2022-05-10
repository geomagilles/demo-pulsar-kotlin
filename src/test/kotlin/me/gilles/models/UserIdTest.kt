package me.gilles.models

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class UserIdTest : StringSpec({
    "UserId should serialize to a string" {
        val id = "&efg@yt§"
        val userId = UserId(id)
        Json.encodeToString(UserId.serializer(), userId) shouldBe "\"$id\""
    }

    "UserId should be de/serializable" {
        val id = "&efg@yt§"
        val userId = UserId(id)
        val json = Json.encodeToString(UserId.serializer(), userId)

        val userId2 = Json.decodeFromString(UserId.serializer(), json)
        userId shouldBe userId2
    }
})