package com.example.demo.controller

import com.example.demo.model.*
import com.example.demo.service.ProductService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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

    @PatchMapping(
        value = ["/{idProduct}"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun updateProduct(@PathVariable("idProduct") id: String,
                      @RequestBody updateProductRequest: UpdateProductRequest) : WebResponse<ProductResponse>{
        val productResponse = productService.update(id,updateProductRequest)
        return WebResponse(
            code = 200,
            status = "OK",
            data = productResponse
        )

    }

    @DeleteMapping(
        value = ["/{idProduct}"],
        produces = ["application/json"]
    )
    fun deleteProduct(@PathVariable("idProduct") id: String): WebResponse<String>{
        productService.delete(id)
        return WebResponse(
            code = 200,
            status = "OK",
            data = id
        )
    }

    @GetMapping(
        produces = ["application/json"]
    )
    fun listProducts(@RequestParam(value = "page", defaultValue = "0") page: Int,
                     @RequestParam(value = "size", defaultValue = "10") size: Int): WebResponse<List<ProductResponse>>{
        val request = ListProductRequest(page,size)
        val response = productService.list(request)
        return WebResponse(
            code = 200,
            status = "OK",
            data = response
        )
    }
}