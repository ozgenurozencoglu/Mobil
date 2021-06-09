package com.ozgenur.odev.data.repository

import com.ozgenur.odev.data.api.RetrofitBuilder

class MainRepository {
    suspend fun getUsers() = RetrofitBuilder.api.getUsers()
}