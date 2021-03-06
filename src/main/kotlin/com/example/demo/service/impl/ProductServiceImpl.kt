package com.example.demo.service.impl

import com.example.demo.Entity.Product
import com.example.demo.error.NotFoundException
import com.example.demo.model.CreateProductRequest
import com.example.demo.model.ListProductRequest
import com.example.demo.model.ProductResponse
import com.example.demo.model.UpdateProductRequest
import com.example.demo.repository.ProductRepository
import com.example.demo.service.ProductService
import com.example.demo.validation.ValidationUtil
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

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
        val idExist = productRepository.findByIdOrNull(CreateProductRequest.id)
        if (idExist != null){
            throw IllegalArgumentException("Product with id ${CreateProductRequest.id} already exist")
        }

        productRepository.save(product)

        return convertProductToProductResponse(product)
    }

    override fun get(id: String): ProductResponse {
        val product = findProductByIdOrThrowNotFound(id)
        return convertProductToProductResponse(product)
    }

    override fun update(id: String, updateProductRequest: UpdateProductRequest): ProductResponse {
        val product = findProductByIdOrThrowNotFound(id)
        validationUtil.validate(updateProductRequest)
        product.apply {
            name = updateProductRequest.name!!
            price = updateProductRequest.price!!
            quantity = updateProductRequest.quantity!!
            updatedAt = Date()
        }

        productRepository.save(product)

        return convertProductToProductResponse(product)
    }

    override fun delete(id: String) {
        val product = findProductByIdOrThrowNotFound(id)
        productRepository.delete(product)
    }

    override fun list(listProductRequest: ListProductRequest): List<ProductResponse> {
        val page = productRepository.findAll(PageRequest.of(listProductRequest.page,listProductRequest.size))
        val product:List<Product> = page.get().collect(Collectors.toList())

        return product.map{convertProductToProductResponse(it)}
    }

    private fun convertProductToProductResponse(product: Product): ProductResponse{
        return ProductResponse(
            id = product.id,
            name = product.name,
            price = product.price,
            quantity = product.quantity,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt
        )
    }

    private fun findProductByIdOrThrowNotFound(id: String): Product{
        val product = productRepository.findByIdOrNull(id)
        if (product == null){
            throw NotFoundException()
        }else{
            return product
        }
    }
}