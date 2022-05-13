package com.example.demo.controller

import com.example.demo.model.CreateProductRequest
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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.*
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
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
        @Transactional
        fun `should add a new product` () {
            // given
            val newProduct = CreateProductRequest(
                "1",
                "Ridwan",
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
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(newProduct))
                    }
                }

//            mockMvc.get("$baseUrl/${newProduct.accountNumber}")
//                .andExpect { content { json(objectMapper.writeValueAsString(newBank)) } }

        }
    }
}