package SpendYourTime.Images

import Composants.Utils
import javafx.scene.image.Image
import javafx.scene.image.WritableImage

object Images
{
    private fun extractAllCase(rName: String): ArrayList<WritableImage>
    {
        val img = Image(Images::class.java.getResourceAsStream(rName))

        val cases: ArrayList<WritableImage> = arrayListOf()

        for (i in 0 until img.height.toInt() step Utils.VALUE)
        {
            for (j in 0 until img.width.toInt() step Utils.VALUE)
            {
                cases.add(WritableImage(img.pixelReader, j, i, Utils.VALUE, Utils.VALUE))
            }
        }

        return cases
    }

    val office = extractAllCase("/assets/office.png")
    val room   = extractAllCase("/assets/room.png")
}
