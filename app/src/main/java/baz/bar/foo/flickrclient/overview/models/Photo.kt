package baz.bar.foo.flickrclient.overview.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Photo(
    val id: String,
    val farm: Int,
    val server: String,
    val secret: String
)
