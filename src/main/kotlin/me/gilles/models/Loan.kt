package me.gilles.models

import kotlinx.serialization.Serializable
import org.jetbrains.annotations.TestOnly

@Serializable
data class Loan(
    val loanId: LoanId,
    val userId: UserId,
    val amount: Amount
) {
    companion object {
        @TestOnly
        fun random() = Loan(LoanId(), UserId(), Amount.random())
    }
}
