package com.example.demo.controller

import com.example.demo.model.Bank
import com.example.demo.model.CreateProductRequest
import com.example.demo.model.UpdateProductRequest
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
import org.springframework.test.web.servlet.*
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
//                header("X-Api-Key", "SECRET")
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
    
    @Nested
    @DisplayName("PATCH /api/products/{id}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PatchProduct {
    
        @Test
        fun `should updated an existing product` () {
            // given
            val updateProduct = UpdateProductRequest(
                "Ramdhan",500,5)
            val idProduct = "A001"

            // when
            val performPatch = mockMvc.patch("$baseUrl/$idProduct"){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updateProduct )
            }

            // then
            performPatch
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(WebResponse(
                            code = 200,
                            status = "OK",
                            data = updateProduct
                        )))
                    }
                }

            mockMvc.get("$baseUrl/${idProduct}")
                .andExpect { content { json(objectMapper.writeValueAsString(
                    WebResponse(
                        code = 200,
                        status = "OK",
                        data = updateProduct
                    ) ))
                }
                }
        }
        @Test
        fun `should return NOT FOUND if id does not exist` () {
            // given
            val updateProduct = UpdateProductRequest(
                "Ramdhan",500,5)
            val idProduct = "A002"

            // when
            val performPatch = mockMvc.patch("$baseUrl/$idProduct"){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updateProduct )
            }

            // then
            performPatch
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.code"){value(404)}
                        jsonPath("$.status"){value("NOT FOUND")}
                    }
                }
        }
        @Test
        fun `should return BAD REQUEST if invalid body` () {
            // given
            val updateProduct = UpdateProductRequest(
                "Ramdhan",0,-1)
            val idProduct = "A001"

            // when
            val performPatch = mockMvc.patch("$baseUrl/$idProduct"){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updateProduct )
            }

            // then
            performPatch
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
    }
    @Nested
    @DisplayName("DELETE /api/products/{id}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class DeleteProduct {
        
        @Test
        fun `should delete product` () {
            // given
            val idProduct = "A001"
            
            // when
            val performDelete = mockMvc.delete("$baseUrl/products/$idProduct"){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(idProduct)
            }

            // then
            performDelete
                .andDo { print() }
                .andExpect { status { isNotFound() } }

//            mockMvc.get("$baseUrl/$idProduct")
//                .andExpect {
//                    status { isNotFound() }
//                }
            
        }
    }
}