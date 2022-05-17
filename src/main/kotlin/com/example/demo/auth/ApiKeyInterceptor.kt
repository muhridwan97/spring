package com.example.demo.auth

import com.example.demo.error.UnauthorizedException
import com.example.demo.repository.ApiKeyRepository
import org.springframework.stereotype.Component
import org.springframework.ui.ModelMap
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.WebRequestInterceptor
import java.lang.Exception

@Component
class ApiKeyInterceptor(val apiKeyRepository: ApiKeyRepository) : WebRequestInterceptor{
    override fun preHandle(request: WebRequest) {
        val apiKey = request.getHeader("X-Api-Key")
        if(apiKey == null){
            throw UnauthorizedException()
        }

        if(!apiKeyRepository.existsById(apiKey)){
            throw UnauthorizedException()
        }
    }

    override fun postHandle(request: WebRequest, model: ModelMap?) {
        TODO("Not yet implemented")
    }

    override fun afterCompletion(request: WebRequest, ex: Exception?) {
        TODO("Not yet implemented")
    }
}