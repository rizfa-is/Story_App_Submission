package com.istekno.app.core.data.source.remote.model

object Login {
    data class Request(
        val email: String,
        val password: String
    )

    data class Response(
        val error: Boolean,
        val message: String,
        val loginResult: Data?
    ) {
        data class Data(
            val userId: String,
            val name: String,
            val token: String
        )
    }
}