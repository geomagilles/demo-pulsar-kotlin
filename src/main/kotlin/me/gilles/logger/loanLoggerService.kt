package me.gilles.logger

import kotlinx.coroutines.future.await
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import me.gilles.events.LoanEvent
import me.gilles.pulsar.Pulsar
import org.apache.pulsar.client.api.PulsarClientException

fun main() = runBlocking {
    Pulsar.client().use { pulsarClient ->
        val consumer = Pulsar.loanEventsConsumer(pulsarClient, "logger")

        while (isActive) {
            val message = try {
                consumer.receiveAsync().await()
            } catch (e: PulsarClientException) {
                println("Failed to receive message: ${e.message}")
                continue
            }

            try {
                val event = LoanEvent.fromByteArray(message.data)

                println("event: ${message.publishTime} ${Json.encodeToString(LoanEvent.serializer(), event)}")

                consumer.acknowledge(message)
            } catch (e: Exception) {
                println("Failed to process message: ${message.messageId}")
                consumer.negativeAcknowledge(message)
                continue
            }
        }
    }
}