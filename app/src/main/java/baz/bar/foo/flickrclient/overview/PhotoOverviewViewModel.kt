package baz.bar.foo.flickrclient.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import baz.bar.foo.flickrclient.overview.models.Photo
import io.reactivex.disposables.Disposables

sealed class ViewState {
    object Loading : ViewState()
    data class PhotosLoaded(val list: List<Photo>) :
        ViewState() //TODO: do not expose data models from network layer to UI.

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
                    list = it.takeLast(NUMBER_LATEST_TO_DISPLAY)
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