package Place.models

class Place{
    lateinit var grid : Array<Pixel>
    lateinit var updated: Array<Pixel>

    fun find(x : Int, y : Int) : Pixel? {
        return grid.find { it.x == x && it.y == y }
    }
}
