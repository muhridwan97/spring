package com.example.demo.controller

import com.example.demo.model.Bank
import com.example.demo.model.Customer
import com.example.demo.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customer")
class WebController {

    @Autowired
    lateinit var repository: CustomerRepository

    @ExceptionHandler(NoSuchElementException::class)
    fun handledNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @PostMapping("/save")
    fun save(): String {
        repository.save(Customer("Jack", "Smith"))
        repository.save(Customer("Adam", "Johnson"))
        repository.save(Customer("Kim", "Smith"))
        repository.save(Customer("David", "Williams"))
        repository.save(Customer("Peter", "Davis"))

        return "Done"
    }

    @GetMapping("/findall")
    fun findAll() = repository.findAll()

    @GetMapping("/findbyid/{id}")
    fun findById(@PathVariable id: Long): Any {
        val result = repository.findById(id)
        if(result.isEmpty){
            throw NoSuchElementException("Could not find a customer with last name $id")
        }
        return result
    }


    @GetMapping("findbylastname/{lastName}")
    fun findByLastName(@PathVariable lastName: String): Any{
        val result = repository.findByLastName(lastName)
        return result.firstOrNull(){it.lastName == lastName}
                ?: throw NoSuchElementException("Could not find a customer with last name $lastName")
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCustomer(@RequestBody customer: Customer): Customer {
        if (repository.existsById(customer.id)){
            throw IllegalArgumentException("Customer with account number ${customer.id} already exist")
        }
        repository.save(customer)
        return customer
    }

}