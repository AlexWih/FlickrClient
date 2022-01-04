package baz.bar.foo.flickrclient.overview

import baz.bar.foo.flickrclient.overview.models.Photo
import io.reactivex.Single

interface PhotoOverviewRepository {

    fun getPhotos(): Single<List<Photo>>
}

internal class PhotoOverviewRepositoryImpl(
    private val getRecentPhotosApi: GetRecentPhotosApi

) : PhotoOverviewRepository {
    override fun getPhotos(): Single<List<Photo>> {
        return getRecentPhotosApi.getRecentPhotos().map {
            it.photos.photo // todo: check stat here.
        }
    }
}
