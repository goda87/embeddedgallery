package es.goda87.embeddedgallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import kotlinx.android.synthetic.main.embedded_gallery_activity.*

private const val URLS = "EMBEDDED_URLS_PARAM"
private const val POSITION = "POSITION_PARAM"
private const val RESOURCE_BACKGROUND = "RESOURCE_BACKGROUND_PARAM"
private const val BACK_BUTTON = "BACK_BUTTON_PARAM"
private const val ZOOMABLE = "ZOOMABLE_PARAM"
private const val POSITION_RESULT_KEY = "POSITION_RESULT_KEY_PARAM"
private const val PAGER_INDICATOR_PROPERTIES = "PAGER_INDICATOR_PROPERTIES"
private const val INTENT_TRANSITION_NAME = "embedded_gallery_transition"

@Deprecated("", replaceWith = ReplaceWith("EmbeddedGalleryActivityBuilder"))
class EmbeddedGalleryActivityIntentBuilder(private val context: Context) {
    var backgroundResource: Int? = null

    fun build(images: List<String>, position: Int = 0): Intent {
        return Intent(context, EmbeddedGalleryActivity::class.java).apply {
            putStringArrayListExtra(URLS, ArrayList(images))
            putExtra(POSITION, position)
            backgroundResource?.let { putExtra(RESOURCE_BACKGROUND, it) }
        }
    }
}

class EmbeddedGalleryActivityBuilder {
    var backgroundResource: Int? = null
    var backButton: Int? = null
    var zoomable: Boolean = true
    var transitionView: View? = null
    var requestCode: Int = -1
    var positionResultKey: String = POSITION_RESULT_KEY
    var pagerIndicatorProperties: ViewPagerIndicatorProperties? = null

    fun startActivity(activity: Activity, images: List<String>, position: Int = 0) {
        val intent = buildIntent(activity, images, position)
        val options = buildOptions(activity)
        activity.startActivityForResult(intent, requestCode, options)
    }

    private fun buildOptions(activity: Activity): Bundle? {
        return transitionView?.let {
            val viewPair = Pair.create(it, INTENT_TRANSITION_NAME)
            ActivityOptionsCompat.makeSceneTransitionAnimation(activity, viewPair).toBundle()
        }
    }

    private fun buildIntent(context: Context, images: List<String>, position: Int = 0): Intent {
        return Intent(context, EmbeddedGalleryActivity::class.java).apply {
            putStringArrayListExtra(URLS, ArrayList(images))
            putExtra(POSITION, position)
            putExtra(ZOOMABLE, zoomable)
            putExtra(POSITION_RESULT_KEY, positionResultKey)
            putExtra(PAGER_INDICATOR_PROPERTIES, pagerIndicatorProperties)
            backgroundResource?.let { putExtra(RESOURCE_BACKGROUND, it) }
            backButton?.let { putExtra(BACK_BUTTON, it) }
        }
    }
}

internal class EmbeddedGalleryActivity : AppCompatActivity() {

    private lateinit var positionResultKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)

        setContentView(R.layout.embedded_gallery_activity)
        loadExtras()
    }

    private fun loadExtras() {
        intent?.extras?.run {
            getStringArrayList(URLS)?.let { urls ->
                embedded_gallery_view.apply {
                    setImages(urls, getInt(POSITION,0), getBoolean(ZOOMABLE, true))
                    setBackgroundResource(getInt(RESOURCE_BACKGROUND, android.R.color.black))
                    getParcelable<ViewPagerIndicatorProperties>(PAGER_INDICATOR_PROPERTIES)?.let {
                        setViewPagerIndicator(it)
                    }
                }
            }
            if (containsKey(BACK_BUTTON)) {
                embedded_gallery_back_button.apply {
                    setImageResource(getInt(BACK_BUTTON))
                    visibility = View.VISIBLE
                    setOnClickListener { onBackPressed() }
                }
            }
            positionResultKey = getString(POSITION_RESULT_KEY, POSITION_RESULT_KEY)
        }
    }

    override fun onBackPressed() {
        updateResult(embedded_gallery_view.currentItem)
        super.onBackPressed()
    }

    private fun updateResult(position: Int) {
        val data = Intent()
        data.putExtra(positionResultKey, position)
        setResult(Activity.RESULT_OK, data)
    }
}
