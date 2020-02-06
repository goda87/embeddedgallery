package es.goda87.embeddedgallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.embedded_gallery_activity.*

private const val URLS = "EMBEDDED_URLS_PARAM"
private const val POSITION = "POSITION_PARAM"
private const val RESOURCE_BACKGROUND = "RESOURCE_BACKGROUND_PARAM"

class EmbeddedGalleryActivityIntentBuilder(private val context: Context) {
    companion object {
        const val INTENT_TRANSITION_NAME = "embedded_gallery_transition"
    }

    var backgroundResource: Int? = null

    fun build(images: List<String>, position: Int = 0): Intent {
        return Intent(context, EmbeddedGalleryActivity::class.java).apply {
            putStringArrayListExtra(URLS, ArrayList(images))
            putExtra(POSITION, position)
            backgroundResource?.let { putExtra(RESOURCE_BACKGROUND, it) }
        }
    }
}

internal class EmbeddedGalleryActivity : AppCompatActivity() {

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
                    setImages(urls, getInt(POSITION,0))
                    setBackgroundResource(getInt(RESOURCE_BACKGROUND, android.R.color.black))
                }
            }
        }
    }
}
