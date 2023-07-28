package SpendYourTime.Images

import SpendYourTime.models.Skin
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.ProgressBar
import javafx.scene.image.Image
import javafx.scene.layout.VBox
import views.SpendYourTime

class Skins {
    companion object {
        val instance = Skins()

//        const val MAX_EYES = 7
//        const val MAX_HAIRS = 200
//        const val MAX_OUTFITS = 132
//        const val MAX_ACCESSORIES = 84
//        const val MAX_BODIES = 9

    }

    val hairs = ArrayList<Image>()
    val outfits = ArrayList<Image>()
    val bodies = ArrayList<Image>()
    val accessories = ArrayList<Image>()
    val eyes = ArrayList<Image>()

    fun getMaxHair() = hairs.size - 1
    fun getMaxOutfit() = outfits.size - 1
    fun getMaxBody() = bodies.size - 1
    fun getMaxAccessory() = accessories.size - 1
    fun getMaxEye() = eyes.size - 1

    fun getHair(index: Int) = if(index in 0..getMaxHair()) hairs[index] else hairs[0]
    fun getOutfit(index: Int) = if(index in 0..getMaxOutfit()) outfits[index] else outfits[0]
    fun getBody(index: Int) = if(index in 0..getMaxBody()) bodies[index] else bodies[0]
    fun getAccessory(index: Int) = if(index in 0..getMaxAccessory()) accessories[index] else accessories[0]
    fun getEye(index: Int) = if(index in 0..getMaxEye()) eyes[index] else eyes[0]

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
            }, "-fx-discord-green", 10.0, Pos.TOP_CENTER, 110.0)
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

    fun createRandomSkin() : Skin {
        if(isLoaded) throw Exception("Skins are not loaded yet")
        return Skin(
            (0..getMaxHair()).random(),
            (0..getMaxOutfit()).random(),
            (0..getMaxBody()).random(),
            (0..getMaxAccessory()).random(),
            (0..getMaxEye()).random()
        )
    }
}
