package baz.bar.foo.flickrclient.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.internal.disposables.SequentialDisposable

sealed class ViewState {
    sealed class Loading : ViewState() {
        object Initial: Loading()
        object Reloading: Loading()
    }
    data class PhotosLoaded(val photoUris: List<String>) : ViewState()

    data class Error(val cause: Throwable) : ViewState()
}

interface PhotoOverviewViewModel {
    val viewStateLiveData: LiveData<ViewState>

    fun reload()
}

private const val NUMBER_LATEST_TO_DISPLAY = 20

internal class PhotoOverviewViewModelImpl(
    private val photoOverviewRepository: PhotoOverviewRepository
) : ViewModel(), PhotoOverviewViewModel {

    private val liveData = MutableLiveData<ViewState>()

    private val disposable: SequentialDisposable

    override val viewStateLiveData: LiveData<ViewState>
        get() = liveData

    init {
        disposable = SequentialDisposable(
            loadData().toObservable()
                .startWith(ViewState.Loading.Initial)
                .subscribe({
                    liveData.postValue(it)
                }, {
                    liveData.postValue(ViewState.Error(it))
                })
        )
    }

    override fun reload() {
        disposable.replace(
            loadData().toObservable()
                .startWith(ViewState.Loading.Reloading)
                .subscribe({
                    liveData.postValue(it)
                }, {
                    liveData.postValue(ViewState.Error(it))
                })
        )
    }

    private fun loadData(): Single<ViewState> {
        return photoOverviewRepository.getPhotos()
            .map {
                ViewState.PhotosLoaded(
                    photoUris = it.takeLast(NUMBER_LATEST_TO_DISPLAY).map { photo ->
                        "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg"
                    }
                ) as ViewState
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}