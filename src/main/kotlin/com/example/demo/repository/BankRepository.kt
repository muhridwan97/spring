//package com.example.demo.repository
//
//import com.example.demo.model.Bank
//import org.springframework.data.repository.CrudRepository
//import org.springframework.stereotype.Repository
//
//interface BankRepository: CrudRepository<Bank, Long> {
//
//    fun retrieveBanks(): Collection<Bank>
//    fun retrieveBank(accountNumber: String): Bank
//    fun createBank(bank: Bank): Bank
//    fun updateBank(bank: Bank): Bank
//    fun deleteBank(accountNumber: String)
//}