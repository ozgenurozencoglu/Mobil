package com.ozgenur.odev.data.api

import com.ozgenur.odev.data.model.User
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers():Response<MutableList<User>>
}