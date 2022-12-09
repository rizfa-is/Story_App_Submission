package com.istekno.app.core.data.source.remote.network.auth

import com.istekno.app.core.BuildConfig.BASE_URL
import com.istekno.app.core.data.source.remote.network.OkHttpClientInstance
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AuthClient {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val okHttpClientWithoutToken = OkHttpClientInstance.Builder().buildWithoutToken()

    private fun apiClientWithoutToken(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClientWithoutToken)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val retrofitClientWithoutToken: AuthService by lazy {
        apiClientWithoutToken().create(AuthService::class.java)
    }
}