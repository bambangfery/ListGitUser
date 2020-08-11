package com.test.listgituser.ui.search

import androidx.lifecycle.ViewModel
import com.test.listgituser.data.repositories.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val repository: SearchRepository
) : ViewModel(){

    suspend fun getSearchUser(
        query: String,
        page: String
    ) = withContext(Dispatchers.IO) {  repository.getSearchUser(query, page) }

}