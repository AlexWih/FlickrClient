package baz.bar.foo.flickrclient.overview.ui

import android.app.Activity
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import baz.bar.foo.flickrclient.R
import baz.bar.foo.flickrclient.overview.PhotoOverviewViewModel
import baz.bar.foo.flickrclient.overview.PhotoOverviewViewModelImpl
import baz.bar.foo.flickrclient.overview.ViewState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_photo_overview.*
import org.koin.android.ext.android.get


class PhotosOverviewFragment : Fragment() {

    private val adapter: PhotoListAdapter = PhotoListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater
            .from(context)
            .inflate(
                R.layout.fragment_photo_overview,
                container,
                false
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPhotoList()

        val viewModel: PhotoOverviewViewModel =
            ViewModelProviders.of(this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    //NOTE: actually creating new instances is responsibility of DI, doing it here in sake of simplicity. To be improved later.
                    return PhotoOverviewViewModelImpl(
                        photoOverviewRepository = get()
                    ) as T
                }
            }).get(PhotoOverviewViewModelImpl::class.java)
        viewModel.viewStateLiveData.observe(
            viewLifecycleOwner,
            Observer {
                render(viewModel, it)
            }

        )
        swipe_refresh_photo_overview.setOnRefreshListener {
            viewModel.reload()
        }
    }

    private fun initPhotoList() {
        val isOrientationLandscape = resources.configuration.orientation == ORIENTATION_LANDSCAPE
        val layoutManager = GridLayoutManager(
            context, if (isOrientationLandscape) {
                4
            } else {
                2
            }
        )
        recycler_photo_overview.addItemDecoration(
            ItemDecorationImpl(requireActivity())
        )
        recycler_photo_overview.layoutManager = layoutManager
        recycler_photo_overview.adapter = adapter
    }

    private fun render(viewModel: PhotoOverviewViewModel, viewState: ViewState) {
        when (viewState) {
            is ViewState.Loading -> {
                when (viewState) {
                    ViewState.Loading.Initial -> {
                        view_overview_loading.show()
                        swipe_refresh_photo_overview.isRefreshing = false
                    }
                    ViewState.Loading.Reloading -> {
                        swipe_refresh_photo_overview.isRefreshing = true
                    }
                }
            }
            is ViewState.Error -> {
                view_overview_loading.hide()
                swipe_refresh_photo_overview.isRefreshing = false
                val snackbar = Snackbar
                    .make(
                        requireView(),
                        getString(R.string.msg_error_occurred),
                        Snackbar.LENGTH_LONG
                    )
                    .setAction(getString(R.string.msg_retry)) {
                        viewModel.reload()
                    }

                snackbar.show()

            }
            is ViewState.PhotosLoaded -> {
                view_overview_loading.hide()
                swipe_refresh_photo_overview.isRefreshing = false
                adapter.showNewData(viewState.photoUris)
            }
        }
    }
}

class ItemDecorationImpl(activity: Activity) : RecyclerView.ItemDecoration() {

    private val isOrientationLandscape =
        activity.resources.configuration.orientation == ORIENTATION_LANDSCAPE

    private val spaceBetween: Float


    init {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x

        spaceBetween =
            if (isOrientationLandscape) {
                0.04f
            } else {
                0.1f
            } * width
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = spaceBetween.toInt()
        outRect.bottom = spaceBetween.toInt()
    }
}