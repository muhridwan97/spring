package com.example.demo.service

import com.example.demo.model.CreateProductRequest
import com.example.demo.model.ListProductRequest
import com.example.demo.model.ProductResponse
import com.example.demo.model.UpdateProductRequest

interface ProductService {

    fun create(CreateProductRequest:CreateProductRequest): ProductResponse

    fun get(id: String): ProductResponse

    fun update(id: String, updateProductRequest: UpdateProductRequest): ProductResponse

    fun delete(id: String)

    fun list(listProductRequest: ListProductRequest): List<ProductResponse>
}