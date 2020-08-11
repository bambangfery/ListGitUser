package com.test.listgituser.data.network

import com.test.listgituser.data.pojo.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyApi {
    @GET("users")
    suspend fun getListUser(
        @Query("q") query: String,
        @Query("page") page: String,
        @Query("per_page") per_page: String
    ) : Response<SearchResponse>

}

