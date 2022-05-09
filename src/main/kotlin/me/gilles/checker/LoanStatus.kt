package me.gilles.checker

sealed class LoanStatus {
    object Approved : LoanStatus()
    class Declined(val reason: String) : LoanStatus()
}