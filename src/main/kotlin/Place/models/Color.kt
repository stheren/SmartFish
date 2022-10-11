package Place.models

import javafx.scene.paint.Paint
import kotlin.properties.Delegates

class Color{
    var red by   Delegates.notNull<Int>()
    var green by Delegates.notNull<Int>()
    var blue by  Delegates.notNull<Int>()

    private fun Int.intToHex(): String{
        var hex = this.toString(16)
        if (hex.length == 1) hex = "0$hex"
        return hex
    }

    fun toHex() : String {
        return "#${red.intToHex()}${green.intToHex()}${blue.intToHex()}"
    }

    fun fromHex(hex: String) : Color {
        red =   hex.substring(1,3).toInt(16)
        green = hex.substring(3,5).toInt(16)
        blue =  hex.substring(5,7).toInt(16)
        return this
    }

    fun toPaint(): Paint {
        return Paint.valueOf(toHex())
    }
}
