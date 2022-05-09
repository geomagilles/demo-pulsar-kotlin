package me.gilles.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.jetbrains.annotations.TestOnly
import java.util.Currency
import kotlin.random.Random

@Serializable
data class Amount(
    val cents: Int,
    @Serializable(with = CurrencySerializer::class)
    val currency: Currency
) {
    companion object {
        @TestOnly
        fun random(): Amount {
            val currency = Currency.getAvailableCurrencies().let { it.elementAt(Random.nextInt(it.size) - 1) }

            return Amount((Math.random() * 100000).toInt(), currency)
        }
    }
}

object CurrencySerializer : KSerializer<Currency> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Currency", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Currency) { encoder.encodeString(value.currencyCode) }
    override fun deserialize(decoder: Decoder): Currency = Currency.getInstance(decoder.decodeString())
}

fun main() {
    val amount = Amount.random()
    val json = Json.encodeToString(Amount.serializer(), amount)
    println(json)
    val amount2 = Json.decodeFromString(Amount.serializer(), json)
    println(amount2)
}