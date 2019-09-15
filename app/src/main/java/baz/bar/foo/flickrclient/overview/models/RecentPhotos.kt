package baz.bar.foo.flickrclient.overview.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class RecentPhotos(
    val photos: Photos,
    val stat: String
)