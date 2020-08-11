package com.test.listgituser.data.pojo

import com.google.gson.annotations.SerializedName

data class SearchResponse (
    @SerializedName("total_count")
    val total_count: Integer,
    @SerializedName("incomplete_results")
    val incomplete_results: Boolean,
    @SerializedName("items")
    val items: ArrayList<Items?>

)
