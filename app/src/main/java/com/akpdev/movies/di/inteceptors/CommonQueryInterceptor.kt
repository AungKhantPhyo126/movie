package com.akpdev.movies.di.inteceptors

import com.akpdev.movies.common.Constants
import okhttp3.Interceptor
import okhttp3.Response

class CommonQueryInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedUrl = originalRequest.url.newBuilder()
            .addQueryParameter("api_key", Constants.API_KEY)
            .build()

        val modifiedRequest = originalRequest.newBuilder()
            .url(modifiedUrl)
            .build()

        return chain.proceed(modifiedRequest)
    }
}