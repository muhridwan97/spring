package com.example.demo.controller

import com.example.demo.model.CreateProductRequest
import com.example.demo.model.ProductResponse
import com.example.demo.model.WebResponse
import com.example.demo.service.ProductService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductController(val productService:ProductService) {

    @PostMapping(
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun createProduct(@RequestBody body: CreateProductRequest): WebResponse<ProductResponse>{
        val productResponse = productService.create(body)

        return WebResponse(
            code = 200,
            status = "OK",
            data = productResponse
        )
    }

    @GetMapping(
        value = ["/{idProduct}"],
        produces = ["application/json"],
    )
    fun getProduct(@PathVariable("idProduct") id: String) : WebResponse<ProductResponse>{
        val productResponse = productService.get(id)

        return WebResponse(
            code = 200,
            status = "OK",
            data = productResponse
        )
    }
}