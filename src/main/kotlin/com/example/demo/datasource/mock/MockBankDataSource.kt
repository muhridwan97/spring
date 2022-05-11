package com.example.demo.datasource.mock

import com.example.demo.datasource.BankDataSource
import com.example.demo.model.Bank
import org.springframework.stereotype.Repository

@Repository
class MockBankDataSource : BankDataSource {

    val banks = mutableListOf(
        Bank("1234",3.0,1),
        Bank("1010",2.0,14),
        Bank("1212",5.0,0)
    )

    override fun retrieveBanks(): Collection<Bank> = banks
    override fun retrieveBank(accountNumber: String): Bank =
        banks.firstOrNull(){it.accountNumber == accountNumber}
            ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")

    override fun createBank(bank: Bank): Bank {
        if (banks.any{ it.accountNumber == bank.accountNumber}){
            throw IllegalArgumentException("Bank with account number ${bank.accountNumber} already exist")
        }
        banks.add(bank)

        return bank
    }
}