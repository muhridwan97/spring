package com.example.demo.controller

import com.example.demo.model.Bank
import com.example.demo.model.CreateProductRequest
import com.example.demo.model.WebResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.Commit
import org.springframework.test.annotation.Rollback
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.*
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
internal class ProductControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
){

    val baseUrl = "/api/products"

    @Nested
    @DisplayName("POST /api/products")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class AddProduct {

        @Test
        fun `should add a new product` () {
            // given
            val newProduct = CreateProductRequest(
                "1",
                "Ramdhan",
                1200,
                1,
            )

            // when
            val performPost = mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newProduct)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(
                            WebResponse(
                                code = 200,
                                status = "OK",
                                data = newProduct
                            ) ))
                    }
                }

            mockMvc.get("$baseUrl/${newProduct.id}")
                .andExpect { content { json(objectMapper.writeValueAsString(
                        WebResponse(
                        code = 200,
                        status = "OK",
                        data = newProduct
                    ) ))
                    }
                }

        }
        @Test
        fun `should return BAD REQUEST if body given not validate param` () {
            // given
            val newProduct = CreateProductRequest(
                "1",
                "Ramdhan",
                0,
                1,
            )

            // when
            val performPost = mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newProduct)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.code"){value(400)}
                        jsonPath("$.status"){value("BAD REQUEST")}
                    }
                }
        }
        @Test
        fun `should return BAD REQUEST when id given already exist` () {
            // given
            val newProduct = CreateProductRequest(
                "A001",
                "Ramdhan",
                20,
                1,
            )

            // when
            val performPost = mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newProduct )
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }
    }
    
    @Nested
    @DisplayName("GET /api/products/{id}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetProductById {
    
        @Test
        fun `should return product by given id` () {
            // given
            val idProduct = "A001"
            
            // when/then
            mockMvc.get("$baseUrl/$idProduct")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.data.name"){value("Ridwan")}

                }
            
        }
    }
}