package baz.bar.foo.flickrclient.overview

import baz.bar.foo.flickrclient.overview.models.Photos
import io.reactivex.Single
import retrofit2.http.GET

interface GetRecentPhotosApi {

    @GET("/?method=flickr.photos.getRecent&api_key=da9d38d3dee82ec8dda8bb0763bf5d9c&format=json&nojsoncallback=1")
    fun loadRecentPhotos(): Single<Photos>
}