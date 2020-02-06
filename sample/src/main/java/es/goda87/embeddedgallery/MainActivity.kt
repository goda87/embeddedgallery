package es.goda87.embeddedgallery

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import es.goda87.embeddedgallery.EmbeddedGalleryActivityIntentBuilder.Companion.INTENT_TRANSITION_NAME
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hello_world.setOnClickListener {
            openGallery(2, minigallery)
        }

        minigallery.setImages(urls)
        minigallery.onItemClickListener = { openGallery(it, minigallery) }
    }

    private fun openGallery(position: Int, fromView: View) {
        val viewPair = Pair.create(fromView, INTENT_TRANSITION_NAME)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, viewPair)

        val intent = EmbeddedGalleryActivityIntentBuilder(this).apply {
            backgroundResource = android.R.color.background_light
        }.build(urls, position)

        startActivity(intent, options.toBundle())
    }

    private val urls = listOf(
        "https://misanimales.com/wp-content/uploads/2017/04/jamas-tienes-que-hacerle-a-tu-gato.jpg",
        "https://misanimales.com/wp-content/uploads/2020/01/gato-bolsa.jpg",
        "https://misanimales.com/wp-content/uploads/2015/02/ni%C3%B1os2-500x374.jpg",
        "https://misanimales.com/wp-content/uploads/2016/10/crecen-los-gatos.jpg",
        "https://misanimales.com/wp-content/uploads/2016/08/socializar-a-un-gato.jpg",
        "https://misanimales.com/wp-content/uploads/2017/02/perros-y-gatos-en-la-nieve.jpg",
        "https://misanimales.com/wp-content/uploads/2020/01/ciencia-afecto-gatos.jpg",
        "https://misanimales.com/wp-content/uploads/2020/01/gato-despues-castracion.jpg"
    )
}
