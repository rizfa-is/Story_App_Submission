package com.istekno.app.core.data.source.remote.network.auth

import com.istekno.app.core.data.source.remote.model.Login
import com.istekno.app.core.data.source.remote.model.Register
import com.istekno.app.core.utils.Validator
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("register")
    suspend fun registerUserAsync(
        @Body request: Register.Request
    ): Validator.Response

    @POST("login")
    suspend fun loginUserAsync(
        @Body request: Login.Request
    ): Login.Response

}