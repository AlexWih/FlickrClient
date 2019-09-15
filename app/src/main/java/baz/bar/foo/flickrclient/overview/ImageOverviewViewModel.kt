package baz.bar.foo.flickrclient.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

sealed class ViewState {
    object Loading : ViewState()
    //data class ImagesLoaded(): ViewState()
    data class Error(val cause: Throwable) : ViewState()
}

interface ImageOverviewViewModel {
    val result: LiveData<ViewState>

    fun load()
    fun retry()
}

internal class ImageOverviewViewModelImpl(

) : ViewModel(), ImageOverviewViewModel {
    override val result: LiveData<ViewState>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun load() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retry() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}