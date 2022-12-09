package com.istekno.app.core.domain.repository

import com.istekno.app.core.data.source.remote.model.Login
import com.istekno.app.core.data.source.remote.model.Register
import com.istekno.app.core.data.source.remote.network.auth.AuthService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val authService: AuthService) {

    suspend fun login(request: Login.Request) = authService.loginUserAsync(request)

    suspend fun register(request: Register.Request) = authService.registerUserAsync(request)
}