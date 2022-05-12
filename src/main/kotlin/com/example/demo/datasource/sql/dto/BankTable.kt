package com.example.demo.datasource.sql.dto

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class BankTable (
    val account_number: String,
    val default_transaction_fee: Int,
    val trust: Double,
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1) {

    private constructor() : this("", 0, 0.0)
}