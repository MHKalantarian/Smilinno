package com.mhksoft.smilinno.data.model


import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonProperty

@Keep
data class Slider(
    @JsonProperty("id") val id: Int? = null,
    @JsonProperty("path") val path: String? = null,
    @JsonProperty("title") val title: String? = null
)