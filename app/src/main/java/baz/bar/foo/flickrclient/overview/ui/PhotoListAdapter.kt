package baz.bar.foo.flickrclient.overview.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import baz.bar.foo.flickrclient.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_photo.view.*

internal class PhotoListAdapter : RecyclerView.Adapter<PhotoHolder>() {

    private val photoUris = mutableListOf<String>()

    fun showNewData(photos: List<String>) {
        photoUris.clear()
        photoUris.addAll(photos)
        notifyDataSetChanged() //TODO: to improve using Diff Utils if time allows.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_photo,
                parent,
                false
            )
        return PhotoHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val photoUri = photoUris[position]
        holder.bind(photoUri)
    }

    override fun getItemCount() = photoUris.size

}

class PhotoHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val image = view.view_photo_overview

    fun bind(photoUri: String) {
        Glide.with(view)
            .load(photoUri)
            .centerCrop()
            .into(image)
    }
}