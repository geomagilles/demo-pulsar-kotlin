package me.gilles.checker

import me.gilles.models.Loan

class LoanChecker {
    fun check(loan: Loan): LoanStatus {

        return when (loan.amount.cents < 50000) {
            true -> LoanStatus.Approved
            false -> LoanStatus.Declined("Amount too high")
        }
    }
}