package SpendYourTime.Images

import javafx.scene.image.Image
import javafx.scene.image.WritableImage

class Room private constructor() {
    companion object {
        private const val VALUE = 16;

        private val img = Image(Room::class.java.getResourceAsStream("/assets/room.png"))

        val topLeftWall = WritableImage(img.pixelReader, VALUE * 7, VALUE, VALUE, VALUE)
        val topRightWall = WritableImage(img.pixelReader, VALUE * 9, VALUE, VALUE, VALUE)
        val leftWall = WritableImage(img.pixelReader, VALUE * 7, VALUE * 2, VALUE, VALUE)
        val rightWall = WritableImage(img.pixelReader, VALUE * 9, VALUE * 2, VALUE, VALUE)
        val bottomLeftWall = WritableImage(img.pixelReader, VALUE * 7, VALUE * 3, VALUE, VALUE)
        val bottomRightWall = WritableImage(img.pixelReader, VALUE * 9, VALUE * 3, VALUE, VALUE)
        val topWall = WritableImage(img.pixelReader, VALUE * 1, VALUE * 9, VALUE, VALUE)
        val topWallLeft = WritableImage(img.pixelReader, 0, VALUE * 9, VALUE, VALUE)
        val topWallRight = WritableImage(img.pixelReader, VALUE * 2, VALUE * 9, VALUE, VALUE)
        val bottomWall = WritableImage(img.pixelReader, VALUE * 8, VALUE * 3, VALUE, VALUE)

        val fullTopLeftWall = WritableImage(img.pixelReader, VALUE, VALUE, VALUE, VALUE)
        val fullTopRightWall = WritableImage(img.pixelReader, VALUE * 2, VALUE, VALUE, VALUE)
        val fullBotLeftWall = WritableImage(img.pixelReader, VALUE, VALUE * 2, VALUE, VALUE)
        val fullBotRightWall = WritableImage(img.pixelReader, VALUE * 2, VALUE * 2, VALUE, VALUE)

        val floor_1 = WritableImage(img.pixelReader, VALUE * 11, VALUE * 6, VALUE, VALUE)
        val floor_2 = WritableImage(img.pixelReader, VALUE * 14, VALUE * 6, VALUE, VALUE)
        val floor_3 = WritableImage(img.pixelReader, VALUE * 11, VALUE * 8, VALUE, VALUE)

        val floor_4 = WritableImage(img.pixelReader, VALUE * 14, VALUE * 8, VALUE, VALUE)
        val floor_5 = WritableImage(img.pixelReader, VALUE * 11, VALUE * 10, VALUE, VALUE)
        val floor_6 = WritableImage(img.pixelReader, VALUE * 14, VALUE * 10, VALUE, VALUE)
        val floor_7 = WritableImage(img.pixelReader, VALUE * 11, VALUE * 12, VALUE, VALUE)
        val floor_8 = WritableImage(img.pixelReader, VALUE * 14, VALUE * 12, VALUE, VALUE)

        val shadowLeftWall = WritableImage(img.pixelReader, VALUE * 13, VALUE * 2, VALUE, VALUE)
        val shadowTopLeftWall = WritableImage(img.pixelReader, VALUE * 13, VALUE, VALUE, VALUE)
        val shadowTopWall = WritableImage(img.pixelReader, VALUE * 14, VALUE, VALUE, VALUE)
        val shadowTopLeftCornerWall = WritableImage(img.pixelReader, VALUE * 15, VALUE, VALUE, VALUE)


        val empty = WritableImage(img.pixelReader, 0, 0, VALUE, VALUE)

        val notFound = WritableImage(img.pixelReader, 0, VALUE * 2, VALUE, VALUE)

    }
}
