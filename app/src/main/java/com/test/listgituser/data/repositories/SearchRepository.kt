package com.test.listgituser.data.repositories

import androidx.lifecycle.MutableLiveData
import com.test.listgituser.data.network.MyApi
import com.test.listgituser.data.network.SafeApiRequest
import com.test.listgituser.data.pojo.Items
import com.test.listgituser.data.pojo.SearchResponse

class SearchRepository(
    private val api: MyApi
) : SafeApiRequest() {
    private val quotes = MutableLiveData<List<Items>>()

    suspend fun getSearchUser(query: String, page: String): SearchResponse{
        return apiRequest { api.getListUser(query, page, "20") }
    }

}