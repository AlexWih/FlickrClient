package baz.bar.foo.flickrclient.overview.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Photos(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    @JsonProperty("photo") val photo: List<Photo>
)