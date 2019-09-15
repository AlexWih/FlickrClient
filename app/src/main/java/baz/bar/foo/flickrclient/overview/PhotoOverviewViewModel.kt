package baz.bar.foo.flickrclient.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposables

sealed class ViewState {
    object Loading : ViewState()
    data class PhotosLoaded(val photoUris: List<String>) : ViewState()

    data class Error(val cause: Throwable) : ViewState()
}

interface PhotoOverviewViewModel {
    val viewStateLiveData: LiveData<ViewState>

    fun reload()
    fun retry()
}

private const val NUMBER_LATEST_TO_DISPLAY = 20

internal class PhotoOverviewViewModelImpl(
    private val photoOverviewRepository: PhotoOverviewRepository
) : ViewModel(), PhotoOverviewViewModel {

    private val liveData = MutableLiveData<ViewState>()

    private var disposable = Disposables.empty()

    override val viewStateLiveData: LiveData<ViewState>
        get() = liveData

    init {
        reload()
    }

    override fun reload() {
        disposable = photoOverviewRepository.getPhotos()
            .map {
                ViewState.PhotosLoaded(
                    photoUris = it.takeLast(NUMBER_LATEST_TO_DISPLAY).map {photo ->
                        "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg"
                    }
                ) as ViewState
            }
            .toObservable()
            .startWith(ViewState.Loading)
            .subscribe({
                liveData.postValue(it)
            }, {
                liveData.postValue(ViewState.Error(it))
            })
    }

    override fun retry() {
        TODO("not implemented")
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}