package com.example.demo.service

import com.example.demo.model.CreateProductRequest
import com.example.demo.model.ProductResponse

interface ProductService {

    fun create(CreateProductRequest:CreateProductRequest): ProductResponse

    fun get(id: String): ProductResponse

}