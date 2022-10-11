package SpendYourTime.Images

import javafx.scene.image.Image
import java.io.File

class Skins {
    companion object{
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

    private fun loadImages(path : String, process: (Image) -> Unit){
        javaClass.getResource(path)?.let { url ->
            File(url.toURI()).listFiles()?.forEach {file ->
                process(Image(file.toURI().toString()))
                pourcentage += 1.0 / 432
            }
        }
    }

    init{
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
