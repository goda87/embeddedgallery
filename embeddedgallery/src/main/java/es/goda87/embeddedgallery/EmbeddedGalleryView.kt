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
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.embedded_gallery_view.view.*
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator.PagerAttacher

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
        embedded_gallery_view_pager.adapter = adapter
        embedded_gallery_view_pager.setCurrentItem(position % imagesUrls.size, false)
        embedded_gallery_view_pager_indicator.visibility = View.VISIBLE
        embedded_gallery_view_pager_indicator.attachToPager(
            embedded_gallery_view_pager,
            ViewPager2Attacher()
        )
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

private class ViewPager2Attacher : PagerAttacher<ViewPager2> {

    private var pager: ViewPager2? = null
    private var callback: ViewPager2.OnPageChangeCallback? = null

    override fun detachFromPager() {
        callback?.let { pager?.unregisterOnPageChangeCallback(it) }
    }

    override fun attachToPager(indicator: ScrollingPagerIndicator, pager: ViewPager2) {
        this.pager = pager
        callback = PageChangeCallback(indicator, pager)
        pager.adapter?.let {
            indicator.setDotCount(it.itemCount)
            indicator.setCurrentPosition(pager.currentItem)
        }
        pager.registerOnPageChangeCallback(callback!!)
    }
}

private class PageChangeCallback(
    private val indicator: ScrollingPagerIndicator,
    private val pager: ViewPager2
) : ViewPager2.OnPageChangeCallback() {
    private var idleState = true
    override fun onPageSelected(position: Int) {
        if (idleState) {
            indicator.setCurrentPosition(pager.currentItem)
        }
    }

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
        val offset: Float = when {
            positionOffset < 0 -> 0f
            positionOffset > 1 -> 1f
            else -> positionOffset
        }
        indicator.onPageScrolled(position, offset)
    }

    override fun onPageScrollStateChanged(state: Int) {
        idleState = state == SCROLL_STATE_IDLE
    }
}
