package com.darkshandev.githubuser.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private var token: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val basicReq = req.newBuilder()
            .addHeader("Authorization", "token $token")
            .build()

        return chain.proceed(basicReq)
    }
}