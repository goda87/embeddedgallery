package es.goda87.embeddedgallery

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.github.chrisbanes.photoview.PhotoView

internal class RecyclerViewAdapter(
    private val urls: List<String>,
    private val glide: RequestManager,
    private val zoomable: Boolean,
    private val onItemClickListener: EmbeddedGalleryItemClickListener
) : RecyclerView.Adapter<PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val photoView = if (zoomable) {
            PhotoView(parent.context)
        } else {
            ImageView(parent.context)
        }.apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return PhotoViewHolder(photoView)
    }

    override fun getItemCount(): Int = urls.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) = with(holder) {
        glide.load(urls[position]).into(photoView)
        photoView.setOnClickListener { onItemClickListener.invoke(position) }
    }
}

internal class PhotoViewHolder(val photoView: ImageView) : RecyclerView.ViewHolder(photoView)
