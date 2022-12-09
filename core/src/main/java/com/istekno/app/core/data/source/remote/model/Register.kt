package com.istekno.app.core.data.source.remote.model

object Register {
    data class Request(
        val name: String,
        val email: String,
        val password: String,
    )
}