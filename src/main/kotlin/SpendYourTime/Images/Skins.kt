package SpendYourTime.Images

import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.ProgressBar
import javafx.scene.image.Image
import javafx.scene.layout.VBox
import views.SpendYourTime
import java.io.File
import java.net.URL

class Skins {
    companion object {
        val instance = Skins()
    }

    val hairs = ArrayList<Image>()
    val outfits = ArrayList<Image>()
    val bodies = ArrayList<Image>()
    val accessories = ArrayList<Image>()
    val eyes = ArrayList<Image>()

    fun getMaxHair() = hairs.size
    fun getMaxOutfit() = outfits.size
    fun getMaxBody() = bodies.size
    fun getMaxAccessory() = accessories.size
    fun getMaxEye() = eyes.size

    var isLoaded = true
    var pourcentage = 0.0

    private fun loadImages(path: String, process: (Image) -> Unit) {
        val mapper = javaClass.getResourceAsStream("${path}/mapper.txt")
        mapper?.bufferedReader()?.readLines()?.forEach {
            process(Image(javaClass.getResourceAsStream(it)))
            pourcentage += 1.0 / 432
            progressBar.progress = pourcentage
        } ?: println("Error loading $path")
    }

    private var progressBar: ProgressBar = ProgressBar()

    init {
        Platform.runLater {
            SpendYourTime.instance.toast("Loading skins...", VBox().apply {
                progressBar.progress = pourcentage
                children.add(progressBar)
            }, "-fx-discord-green", 10.0, Pos.TOP_CENTER)
        }
        Thread {
            loadImages("/assets/Character/Eyes") {
                eyes.add(it)
            }
            loadImages("/assets/Character/Hairstyles") {
                hairs.add(it)
            }
            loadImages("/assets/Character/Outfits") {
                outfits.add(it)
            }
            loadImages("/assets/Character/Accessories") {
                accessories.add(it)
            }
            loadImages("/assets/Character/Bodies") {
                bodies.add(it)
            }
            isLoaded = false
        }.start()
    }

}
