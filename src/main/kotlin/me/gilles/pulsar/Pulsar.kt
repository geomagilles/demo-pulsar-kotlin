package me.gilles.pulsar

import me.gilles.events.LoanEvent
import me.gilles.events.LoanEventEnvelope
import me.gilles.events.loanEventSchema
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.DeadLetterPolicy
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.SubscriptionInitialPosition
import org.apache.pulsar.client.api.SubscriptionType
import java.util.concurrent.TimeUnit

object Pulsar {
    private const val EVENTS_TOPIC = "persistent://demo/loans/loanEvents"
    private const val EVENTS_TOPIC_WITH_SCHEMA = "persistent://demo/loans/loanEventsWithSchema"

    internal fun client() = PulsarClient.builder().serviceUrl("pulsar://localhost:6650").build()

    internal fun loanEventsConsumer(client: PulsarClient, name: String) = client
        .newConsumer()
        .subscriptionName(name)
        .subscriptionType(SubscriptionType.Shared)
        .topic(EVENTS_TOPIC)
        .deadLetterPolicy(DeadLetterPolicy.builder().maxRedeliverCount(2).build())
        .negativeAckRedeliveryDelay(10, TimeUnit.SECONDS)
        .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
        .subscribe()

    internal fun loanEventProducer(client: PulsarClient): Producer<ByteArray> = client
        .newProducer()
        .topic(EVENTS_TOPIC)
        .create()

    internal fun loanEventsConsumerWithSchema(client: PulsarClient, name: String): Consumer<LoanEventEnvelope> = client
        .newConsumer(loanEventSchema())
        .subscriptionName(name)
        .subscriptionType(SubscriptionType.Shared)
        .topic(EVENTS_TOPIC_WITH_SCHEMA)
        .deadLetterPolicy(DeadLetterPolicy.builder().maxRedeliverCount(2).build())
        .negativeAckRedeliveryDelay(10, TimeUnit.SECONDS)
        .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
        .subscribe()

    internal fun loanEventProducerWithSchema(client: PulsarClient): Producer<LoanEventEnvelope> = client
        .newProducer(loanEventSchema())
        .topic(EVENTS_TOPIC_WITH_SCHEMA)
        .create()
}

@JvmName("sendLoanEventByteArray")
internal fun Producer<ByteArray>.sendEvent(event: LoanEvent) = send(event.toByteArray())

@JvmName("sendLoanEventEnvelope")
internal fun Producer<LoanEventEnvelope>.sendEvent(event: LoanEvent) = send(LoanEventEnvelope.from(event))
