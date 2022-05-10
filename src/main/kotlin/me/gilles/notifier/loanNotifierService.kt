package me.gilles.notifier

import kotlinx.coroutines.future.await
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import me.gilles.events.LoanAccepted
import me.gilles.events.LoanDeclined
import me.gilles.events.LoanEvent
import me.gilles.pulsar.Pulsar
import org.apache.pulsar.client.api.PulsarClientException
import org.slf4j.LoggerFactory

fun main() = runBlocking {
    val logger = LoggerFactory.getLogger("LoanNotifierService")

    Pulsar.client().use { pulsarClient ->
        val consumer = Pulsar.loanEventsConsumer(pulsarClient, "loan-notifier")

        while (isActive) {
            val message = try {
                consumer.receiveAsync().await()
            } catch (e: PulsarClientException) {
                logger.warn("Failed to receive message: $e")
                continue
            }

            try {
                when (LoanEvent.fromByteArray(message.data)) {
                    is LoanAccepted -> {
                        // tell user that loan has been accepted
                    }
                    is LoanDeclined -> {
                        // tell user that loan has been declined
                    }
                    else -> Unit // ignore other events
                }

                consumer.acknowledge(message)
            } catch (e: Exception) {
                logger.error("Failed to receive message: ${message.messageId} $e")
                consumer.negativeAcknowledge(message)
                continue
            }
        }
    }
}