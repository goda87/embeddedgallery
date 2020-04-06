package es.goda87.embeddedgallery

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.chahinem.pageindicator.PageIndicator
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.embedded_gallery_view.view.*

typealias EmbeddedGAlleryItemClickListener = (Int) -> Unit

class EmbeddedGalleryView(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.embedded_gallery_view, this)
    }

    private lateinit var adapter: RVAdapter

    fun setImages(imagesUrls: List<String>, position: Int = 0, zoomable: Boolean = true) {
        adapter = RVAdapter(imagesUrls, Glide.with(context), zoomable) {
            onItemClickListener?.invoke(it)
        }
        embedded_gallery_view_pager_indicator.count = adapter.itemCount
        embedded_gallery_view_pager.adapter = adapter
        embedded_gallery_view_pager.setCurrentItem(position % imagesUrls.size, false)
        embedded_gallery_view_pager.registerOnPageChangeCallback(
            OnPageChangeCallback(embedded_gallery_view_pager_indicator)
        )
//        embedded_gallery_view_pager_indicator.visibility = View.VISIBLE
    }

    val itemCount: Int get() = adapter.itemCount

    var currentItem: Int
        get() = embedded_gallery_view_pager.currentItem
        set(value) {
            embedded_gallery_view_pager.setCurrentItem(value % itemCount, false)
        }

    var onItemClickListener: EmbeddedGAlleryItemClickListener? = null
}

private class RVAdapter(
    private val urls: List<String>,
    private val glide: RequestManager,
    private val zoomable: Boolean,
    private val onItemClickListener: EmbeddedGAlleryItemClickListener
) : RecyclerView.Adapter<PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val photoView = if (zoomable) {
            PhotoView(parent.context)
        } else {
            ImageView(parent.context)
        }.apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        return PhotoViewHolder(photoView)
    }

    override fun getItemCount(): Int = urls.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) = with(holder) {
        glide.load(urls[position]).into(photoView)
        photoView.setOnClickListener { onItemClickListener.invoke(position) }
    }
}

private class PhotoViewHolder(val photoView: ImageView) : RecyclerView.ViewHolder(photoView)

private class OnPageChangeCallback(
    private val indicator: PageIndicator
) : ViewPager2.OnPageChangeCallback() {
    private var selectedPage = 0

    override fun onPageSelected(position: Int) {
//        val diff = selectedPage - position
//        when {
//            diff > 0 -> swipeNext(diff)
//            diff < 0 -> swipePrevious(diff)
//            else -> indicator.swipePrevious()
//        }
        (position - selectedPage).let {
            when {
                it > 0 -> swipeNext(it)
                it < 0 -> swipePrevious(it)
            }
        }
        selectedPage = position
    }

    private fun swipeNext(n: Int) {
        for (i in 1..n) {
            indicator.swipeNext()
        }
    }

    private fun swipePrevious(n: Int) {
        for (i in 1..-n) {
            indicator.swipePrevious()
        }
    }
}