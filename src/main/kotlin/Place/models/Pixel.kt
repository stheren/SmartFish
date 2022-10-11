package Place.models

import kotlin.properties.Delegates

class Pixel {
    var x by Delegates.notNull<Int>()
    var y by Delegates.notNull<Int>()

    lateinit var color: Color
}
