package SpendYourTime.models

class Point()
{
    val VALUE  = 16
    var x: Int = 0
    var y: Int = 0

    constructor(x: Int, y: Int) : this()
    {
        this.x = x
        this.y = y
    }

    constructor(x: Double, y: Double) : this()
    {
        this.x = x.toInt()
        this.y = y.toInt()
    }

    // Convert to Point on map
    fun convert(): Point
    {
        return Point(x / VALUE, y / VALUE)
    }

    override fun toString(): String
    {
        return "($x, $y)"
    }

    fun add(i: Int, i1: Int): Point
    {
        return Point(x + i, y + i1)
    }
}
