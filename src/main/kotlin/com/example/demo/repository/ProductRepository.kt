package com.example.demo.repository

import com.example.demo.Entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, String>{
}