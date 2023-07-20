package SpendYourTime.Images

import Composants.Utils
import SpendYourTime.models.Case
import javafx.scene.image.Image
import javafx.scene.image.WritableImage

object Images {
    private fun extractAllCase(rName: String): ArrayList<WritableImage> {
        val img = Image(Images::class.java.getResourceAsStream(rName))
        val cases: ArrayList<WritableImage> = arrayListOf()
        for (i in 0 until img.height.toInt() step Utils.VALUE) {
            for (j in 0 until img.width.toInt() step Utils.VALUE) {
                cases.add(WritableImage(img.pixelReader, j, i, Utils.VALUE, Utils.VALUE))
            }
        }
        return cases
    }

    private val DATA =  arrayListOf(
        extractAllCase("/assets/room.png"),
        extractAllCase("/assets/office.png"),
        // extractAllCase("/assets/interiors.png")
    )

    fun getNames() = arrayListOf("room", "office")

    fun get(case : Case): WritableImage {
        return DATA[case.file][case.index]
    }

    fun get(file : Int): ArrayList<WritableImage> {
        return DATA[file]
    }

    fun isCorrect(case : Case): Boolean {
        if (case.file < 0 || case.file >= DATA.size) return false
        if (case.index < 0) return false
        return case.index < DATA[case.file].size
    }
}
