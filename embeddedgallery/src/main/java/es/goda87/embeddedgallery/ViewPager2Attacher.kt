package es.goda87.embeddedgallery

import androidx.viewpager2.widget.ViewPager2
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator

internal class ViewPager2Attacher : ScrollingPagerIndicator.PagerAttacher<ViewPager2> {

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
        idleState = state == ViewPager2.SCROLL_STATE_IDLE
    }
}
