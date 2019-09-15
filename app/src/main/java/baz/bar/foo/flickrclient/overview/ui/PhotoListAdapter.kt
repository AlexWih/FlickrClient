package baz.bar.foo.flickrclient.overview.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import baz.bar.foo.flickrclient.R
import baz.bar.foo.flickrclient.overview.models.Photo
import com.bumptech.glide.Glide

internal class PhotoListAdapter: RecyclerView.Adapter<PhotoHolder>() {

    private val list = mutableListOf<Photo>()

    fun showNewData(photos: List<Photo>) {
        list.clear()
        list.addAll(photos)
        notifyDataSetChanged() //TODO: to improve using Diff Utils if time allows.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_photo, parent, false)
        return PhotoHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val photo = list[position]
        holder.bind(photo)
    }

    override fun getItemCount() = list.size

}

class PhotoHolder(val view: View): RecyclerView.ViewHolder(view) {

    private val image = view.findViewById<ImageView>(R.id.view_photo_overview)

    fun bind(photo: Photo) {
        Glide.with(view).load("http://goo.gl/gEgYUd").into(image)
    }
}