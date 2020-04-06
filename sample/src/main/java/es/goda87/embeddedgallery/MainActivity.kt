package es.goda87.embeddedgallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_CODE = 666
private const val POSITION_RESULT = "POSITION_RESULT"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hello_world.setOnClickListener {
            openGallery(minigallery.currentItem, minigallery)
        }
        minigallery.apply {
            setImages(urls, zoomable = false)
            onItemClickListener = { openGallery(it, minigallery) }
            setViewPagerIndicator(indicatorProperties)
        }
    }

    private fun openGallery(position: Int, fromView: View) =
        EmbeddedGalleryActivityBuilder().apply {
            backgroundResource = android.R.color.background_light
            backButton = R.drawable.icons_normal_ic_back_blue
            zoomable = true
            transitionView = fromView
            requestCode = REQUEST_CODE
            positionResultKey = POSITION_RESULT
            pagerIndicatorProperties = indicatorProperties
        }.startActivity(this, urls, position)

    private val indicatorProperties = ViewPagerIndicatorProperties(
        visible = true,
        dotColor = ContextCompat.getColor(baseContext, R.color.grey),
        dotSelectedColor = ContextCompat.getColor(baseContext, R.color.blue)
    )

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.getIntExtra(POSITION_RESULT, 0)?.let {
                minigallery.currentItem = it
            }
        }
    }
}
