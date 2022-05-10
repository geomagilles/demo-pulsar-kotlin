import me.gilles.events.LoanApplied
import me.gilles.models.Loan
import me.gilles.pulsar.Pulsar
import me.gilles.pulsar.sendEvent

fun main() {
    Pulsar.client().use { pulsarClient ->
        val producer = Pulsar.loanEventProducer(pulsarClient)

        repeat(10) {
            val loanApplied = LoanApplied(Loan.random())

            println("Sending ${loanApplied.loan.loanId}")
            producer.sendEvent(loanApplied)
        }
    }
}
