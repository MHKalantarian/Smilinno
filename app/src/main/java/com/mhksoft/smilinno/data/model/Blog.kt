package com.mhksoft.smilinno.data.model


import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonProperty

@Keep
data class Blog(
    @JsonProperty("author") val author: String? = null,
    @JsonProperty("body") val body: String? = null,
    @JsonProperty("comments") val comments: List<Comment?>? = null,
    @JsonProperty("date") val date: String? = null,
    @JsonProperty("id") val id: Long? = null,
    @JsonProperty("path") val path: String? = null,
    @JsonProperty("title") val title: String? = null
) {
    @Keep
    data class Comment(
        @JsonProperty("avatar") val avatar: String? = null,
        @JsonProperty("body") val body: String? = null,
        @JsonProperty("createdOn") val createdOn: String? = null,
        @JsonProperty("id") val id: Int? = null,
        @JsonProperty("username") val username: String? = null
    )
}