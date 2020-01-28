package es.goda87.embeddedgallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hello_world.setOnClickListener {
            val intent = EmbeddedGalleryActivityIntentBuilder(this).apply {
                backgroundResource = R.drawable.ic_launcher_background
            }.build(urls)
            startActivity(intent)
        }
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
