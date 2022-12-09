package com.istekno.app.core.utils

object Validator {
    data class Login(
        var email: Boolean = false,
        var password: Boolean = false
    ) {
        fun filled() = email && password
    }

    data class Register(
        var email: Boolean = false,
        var password: Boolean = false,
        var name: Boolean = false
    ) {
        fun filled() = name && password && email
    }

    data class Response(
        val error: Boolean,
        val message: String
    )
}