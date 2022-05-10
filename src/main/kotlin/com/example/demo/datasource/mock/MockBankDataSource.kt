package com.example.demo.datasource.mock

import com.example.demo.datasource.BankDataSource
import com.example.demo.model.Bank
import org.springframework.stereotype.Repository

@Repository
class MockBankDataSource : BankDataSource {

    val banks = listOf(
        Bank("1234",3.0,1),
        Bank("1010",2.0,14),
        Bank("1212",5.0,0)
    )

    override fun retrieveBanks(): Collection<Bank> = banks
}