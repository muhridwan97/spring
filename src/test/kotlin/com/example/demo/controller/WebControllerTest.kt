package com.example.demo.controller

import com.example.demo.model.Customer
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
internal class WebControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
){
    val baseUrl = "/api/customer"

    @Nested
    @DisplayName("GET /api/customer/findall")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetCustomer {

        @Test
        fun `should return all customer` () {
            // when/then
            mockMvc.get("$baseUrl/findall")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].firstName"){value("Jack")}
                }
        }

    }

    @Nested
    @DisplayName("GET /api/customer/findbyid/{id}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetCustomerById {

        @Test
        fun `should return customer by given id` () {
            // given
            val idCustomer = 2

            // when
            mockMvc.get("$baseUrl/findbyid/$idCustomer")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.firstName"){value("Jack")}
                    jsonPath("$.lastName"){value("Smith")}

                }

            // then

        }
        @Test
        fun `should return NOT FOUND if id does not exist` () {
            // given
            val idCustomer = 1

            // when
            mockMvc.get("$baseUrl/findbyid/$idCustomer")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }
    @Nested
    @DisplayName("GET /api/customer/findbylastname/{lastName}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetLastName {

        @Test
        fun `should return customer by given last name` () {
            // given
            val lastName = "Smith"

            // when
            mockMvc.get("$baseUrl/findbylastname/$lastName")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.firstName"){value("Jack")}
                    jsonPath("$.lastName"){value("Smith")}

                }

            // then

        }
        @Test
        fun `should return NOT FOUND if last name does not exist` () {
            // given
            val lastName = "Bagas"

            // when
            mockMvc.get("$baseUrl/findbylastname/$lastName")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }
    @Nested
    @DisplayName("POST /api/customer")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class AddCustomer {
    
        @Test
        @DirtiesContext
        @Transactional
        fun `should add a new customer` () {
            // given
            val customer = Customer("Muhammad", "Ramdhan")

            // when
            val performPost = mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(customer)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(customer))
                    }
                }

            mockMvc.get("$baseUrl/findbylastname/${customer.lastName}")
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.firstName") { value("Muhammad") }
                    jsonPath("$.lastName") { value("Ramdhan") }
                }

        }
    }
}