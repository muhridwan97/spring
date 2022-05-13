package com.example.demo.service.impl

import com.example.demo.Entity.Product
import com.example.demo.model.CreateProductRequest
import com.example.demo.model.ProductResponse
import com.example.demo.repository.ProductRepository
import com.example.demo.service.ProductService
import com.example.demo.validation.ValidationUtil
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductServiceImpl(
    val productRepository: ProductRepository,
    val validationUtil: ValidationUtil) : ProductService {

    override fun create(CreateProductRequest: CreateProductRequest): ProductResponse {
        validationUtil.validate(CreateProductRequest)
        val product = Product(
            id = CreateProductRequest.id!!,
            name = CreateProductRequest.name!!,
            price = CreateProductRequest.price!!,
            quantity = CreateProductRequest.quantity!!,
            createdAt = Date(),
            updatedAt = null
        )

        productRepository.save(product)

        return ProductResponse(
            id = product.id,
            name = product.name,
            price = product.price,
            quantity = product.quantity,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt
        )
    }
}