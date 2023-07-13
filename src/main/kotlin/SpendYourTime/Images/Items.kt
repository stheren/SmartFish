package SpendYourTime.Images

import javafx.scene.image.Image
import javafx.scene.image.WritableImage

class Items private constructor() {
    companion object {
        private const val VALUE = 16;

        private val img = Image(Room::class.java.getResourceAsStream("/assets/office.png"))

        val desk1 = WritableImage(img.pixelReader, VALUE*5, VALUE*39, VALUE, VALUE)
        val desk2 = WritableImage(img.pixelReader, VALUE*6, VALUE*39, VALUE, VALUE)
        val desk3 = WritableImage(img.pixelReader, VALUE*6, VALUE*38, VALUE, VALUE)

        val chair1 = WritableImage(img.pixelReader, VALUE*3, VALUE*10, VALUE, VALUE)
        val chair2 = WritableImage(img.pixelReader, VALUE*3, VALUE*11, VALUE, VALUE)

        fun get(value: Int): WritableImage {
            when (value) {
                1 -> return desk1
                2 -> return desk2
                3 -> return desk3
                4 -> return chair1
                5 -> return chair2
            }
            return Room.empty
        }
    }
}
