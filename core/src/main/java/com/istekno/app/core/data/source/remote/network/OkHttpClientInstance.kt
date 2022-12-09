package com.istekno.app.core.data.source.remote.network

import com.istekno.app.core.utils.AppContext
import com.istekno.app.core.utils.AppUtils.isTokenExpired
import com.istekno.app.core.utils.PreferencesManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpClientInstance {
    class Builder {
        private val loggingInterceptor = run {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        fun buildToken(): OkHttpClient {
            val okHttpClientBuilder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    if (isTokenExpired()) {
                        sendRequestLogout(chain)
                    } else {
                        val newRequestBuilder = chain.request().newBuilder()
                            .addHeader(
                                "Authorization",
                                "Bearer ${PreferencesManager.getInstance(AppContext.appContext).accessToken}"
                            )
                            .build()
                        val response = chain.proceed(newRequestBuilder)
                        val rawJson = response.body?.string()
                        val build = response.newBuilder()
                            .body(ResponseBody.create(response.body?.contentType(), rawJson!!))
                            .build()
                        response.close()
                        return@addInterceptor build
                    }
                }
                .connectTimeout(400, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(150, TimeUnit.SECONDS)
            return okHttpClientBuilder.build()
        }

        fun buildWithoutToken(): OkHttpClient {
            val okHttpClientBuilder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(400, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(150, TimeUnit.SECONDS)

            return okHttpClientBuilder.build()
        }

        private fun sendRequestLogout(chain: Interceptor.Chain): Response {
            val newRequest = chain.request().newBuilder().build()
            PreferencesManager.getInstance(AppContext.appContext).accessToken = ""
            return chain.proceed(newRequest)
        }
    }
}