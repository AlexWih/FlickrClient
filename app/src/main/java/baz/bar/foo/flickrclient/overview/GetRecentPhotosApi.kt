package baz.bar.foo.flickrclient.overview

import baz.bar.foo.flickrclient.overview.models.RecentPhotos
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GetRecentPhotosApi {

    @GET("rest")
    fun getRecentPhotos(
        @Query("method") method: String = "flickr.photos.getRecent",
        @Query("api_key") apiKey: String = "da9d38d3dee82ec8dda8bb0763bf5d9c",
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsoncallback: Int = 1
    ): Single<RecentPhotos>
}