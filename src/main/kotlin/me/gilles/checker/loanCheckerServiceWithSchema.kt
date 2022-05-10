package me.gilles.checker

import kotlinx.coroutines.future.await
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import me.gilles.events.LoanAccepted
import me.gilles.events.LoanApplied
import me.gilles.events.LoanDeclined
import me.gilles.pulsar.Pulsar
import me.gilles.pulsar.sendEvent
import org.apache.pulsar.client.api.PulsarClientException
import org.slf4j.LoggerFactory

fun main() = runBlocking {
    val logger = LoggerFactory.getLogger("LoanCheckerService")

    Pulsar.client().use { pulsarClient ->
        val consumer = Pulsar.loanEventsConsumerWithSchema(pulsarClient, "loan-checker")

        val producer = Pulsar.loanEventProducerWithSchema(pulsarClient)

        val checker = LoanChecker()

        while (isActive) {
            val message = try {
                consumer.receiveAsync().await()
            } catch (e: PulsarClientException) {
                logger.warn("Failed to receive message: $e")
                continue
            }

            try {
                val event = message.value.message()

                if (event is LoanApplied) {
                    when (val status = checker.check(event.loan)) {
                        is LoanStatus.Approved -> {
                            println("Loan ${event.loan.loanId} is approved")
                            producer.sendEvent(LoanAccepted(event.loan))
                        }
                        is LoanStatus.Declined -> {
                            println("Loan ${event.loan.loanId} is rejected")
                            producer.sendEvent(LoanDeclined(event.loan, status.reason))
                        }
                    }
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