package es.goda87.embeddedgallery

import android.content.Context
import android.graphics.Color
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.embedded_gallery_view.view.*

internal typealias EmbeddedGalleryItemClickListener = (Int) -> Unit

class EmbeddedGalleryView(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.embedded_gallery_view, this)
    }

    private lateinit var adapter: RecyclerViewAdapter

    fun setImages(imagesUrls: List<String>, position: Int = 0, zoomable: Boolean = true) {
        adapter = RecyclerViewAdapter(imagesUrls, Glide.with(context), zoomable) {
            onItemClickListener?.invoke(it)
        }
        embedded_gallery_view_pager.adapter = adapter
        embedded_gallery_view_pager.setCurrentItem(position % imagesUrls.size, false)
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

    var onItemClickListener: EmbeddedGalleryItemClickListener? = null

    fun setViewPagerIndicator(properties: ViewPagerIndicatorProperties) {
        embedded_gallery_view_pager_indicator.apply {
            visibility = if (properties.visible) View.VISIBLE else View.GONE
            dotColor = properties.dotColor
            selectedDotColor = properties.dotSelectedColor
        }
    }
}

@Parcelize
data class ViewPagerIndicatorProperties(
    val visible: Boolean = false,
    val dotColor: Int = Color.GRAY,
    val dotSelectedColor: Int = Color.BLUE
): Parcelable
