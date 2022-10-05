package SpendYourTime.models

import SpendYourTime.Images.Room
import com.fasterxml.jackson.databind.ObjectMapper
import javafx.scene.image.WritableImage
import java.io.File

class Map private constructor() : ArrayList<ArrayList<Int>>() {
    companion object {
        var n = 0
        var m = 0
        val instance: Map = Map()

        fun setFromJson() {
            try {
                val mapper = ObjectMapper()
                val map = mapper.readValue(File("src/main/kotlin/SpendYourTime/map.json"), Map::class.java)
                instance.clear()
                println(instance)
                map.forEach { instance.add(it) }
                n = instance.size
                m = instance[0].size
            } catch (ex: Exception) {
                println(ex)
            }
        }
    }

    fun get(x: Int, y: Int): Int {
        return this[x][y]
    }

    private fun Right(i: Int, j: Int): Boolean {
        return i + 1 < n && this[i + 1][j] >= 1
    }

    private fun Left(i: Int, j: Int): Boolean {
        return i - 1 >= 0 && this[i - 1][j] >= 1
    }

    private fun Up(i: Int, j: Int): Boolean {
        return j - 1 >= 0 && this[i][j - 1] >= 1
    }

    private fun Down(i: Int, j: Int): Boolean {
        return j + 1 < m && this[i][j + 1] >= 1
    }

    private fun LeftUpCorner(i: Int, j: Int): Boolean {
        return i - 1 >= 0 && j - 1 >= 0 && this[i - 1][j - 1] >= 1
    }

    private fun RightUpCorner(i: Int, j: Int): Boolean {
        return i + 1 < n && j - 1 >= 0 && this[i + 1][j - 1] >= 1
    }

    private fun LeftDownCorner(i: Int, j: Int): Boolean {
        return i - 1 >= 0 && j + 1 < m && this[i - 1][j + 1] >= 1
    }

    private fun RightDownCorner(i: Int, j: Int): Boolean {
        return i + 1 < n && j + 1 < m && this[i + 1][j + 1] >= 1
    }

    fun drawWall(x: Int, y: Int, draw: (WritableImage) -> Unit) {
        when {
            Left(x, y) && Right(x, y) && Up(x, y) && Down(x, y) -> draw(Room.notFound)

            Left(x, y) && Right(x, y) && Up(x, y) -> draw(Room.notFound)
            Left(x, y) && Right(x, y) && Down(x, y) -> draw(Room.notFound)
            Left(x, y) && Up(x, y) && Down(x, y) -> draw(Room.notFound)
            Right(x, y) && Up(x, y) && Down(x, y) -> draw(Room.notFound)

            Up(x, y) && Left(x, y) -> {
                if (RightDownCorner(x, y)) draw(Room.topLeftWall)
                draw(Room.fullBotRightWall)
            }

            Up(x, y) && Right(x, y) -> {
                if (LeftDownCorner(x, y)) draw(Room.topRightWall)
                draw(Room.fullBotLeftWall)
            }

            Down(x, y) && Left(x, y) -> draw(Room.topWallLeft)
            Down(x, y) && Right(x, y) -> draw(Room.topWallRight)
            Up(x, y) && Down(x, y) -> {
                draw(Room.bottomWall)
                draw(Room.topWall)
            }

            Left(x, y) && Right(x, y) -> {
                draw(Room.rightWall)
                draw(Room.leftWall)
            }

            Up(x, y) -> {
                if (LeftDownCorner(x, y)) {
                    draw(Room.topRightWall)
                }
                if (RightDownCorner(x, y)) {
                    draw(Room.topLeftWall)
                }
                draw(Room.bottomWall)
            }

            Down(x, y) -> draw(Room.topWall)
            Left(x, y) -> {
                if (RightDownCorner(x, y)) {
                    draw(Room.topLeftWall)
                } else if (RightUpCorner(x, y)) {
                    draw(Room.bottomLeftWall)
                }
                draw(Room.rightWall)
            }

            Right(x, y) -> {

                if (LeftDownCorner(x, y)) {
                    draw(Room.topRightWall)
                } else if (LeftUpCorner(x, y)) {
                    draw(Room.bottomRightWall)
                }
                draw(Room.leftWall)
            }

            LeftUpCorner(x, y) && RightUpCorner(x, y) && LeftDownCorner(x, y) && RightDownCorner(
                x,
                y
            ) -> {
                draw(Room.topRightWall)
                draw(Room.topLeftWall)
            }

            LeftUpCorner(x, y) && RightUpCorner(x, y) && LeftDownCorner(x, y) -> {
                draw(Room.topRightWall)
                draw(Room.bottomLeftWall)
            }

            LeftUpCorner(x, y) && RightUpCorner(x, y) && RightDownCorner(x, y) -> {
                draw(Room.topLeftWall)
                draw(Room.bottomRightWall)
            }

            LeftUpCorner(x, y) && LeftDownCorner(x, y) && RightDownCorner(x, y) -> {
                draw(Room.topRightWall)
                draw(Room.topLeftWall)
            }

            RightUpCorner(x, y) && LeftDownCorner(x, y) && RightDownCorner(x, y) -> {
                draw(Room.topLeftWall)
                draw(Room.topRightWall)
            }

            LeftUpCorner(x, y) && RightUpCorner(x, y) -> {
                draw(Room.bottomRightWall)
                draw(Room.bottomLeftWall)
            }

            LeftUpCorner(x, y) && LeftDownCorner(x, y) -> draw(Room.topRightWall)

            LeftUpCorner(x, y) && RightDownCorner(x, y) -> {
                draw(Room.bottomRightWall)
                draw(Room.topLeftWall)
            }

            RightUpCorner(x, y) && LeftDownCorner(x, y) -> {
                draw(Room.bottomLeftWall)
                draw(Room.topRightWall)
            }

            RightUpCorner(x, y) && RightDownCorner(x, y) -> draw(Room.topLeftWall)

            LeftDownCorner(x, y) && RightDownCorner(x, y) -> {
                draw(Room.topRightWall)
                draw(Room.topLeftWall)
            }

            LeftUpCorner(x, y) -> draw(Room.bottomRightWall)
            RightUpCorner(x, y) -> draw(Room.bottomLeftWall)
            LeftDownCorner(x, y) -> draw(Room.topRightWall)
            RightDownCorner(x, y) -> draw(Room.topLeftWall)


            else -> draw(Room.empty)
        }
    }

    fun drawFloor(x: Int, y: Int, value: Int, draw: (WritableImage) -> Unit) {
        //Value can be 1 to 8
        when (value) {
            1 -> draw(Room.floor_2)
            2 -> draw(Room.floor_3)
            3 -> draw(Room.floor_6)
            4 -> draw(Room.floor_7)
        }
        when {
            !Left(x, y) && !Up(x, y) -> draw(Room.shadowTopLeftWall)
            !Left(x, y) -> draw(Room.shadowLeftWall)
            !Up(x, y) -> draw(Room.shadowTopWall)
            !LeftUpCorner(x, y) -> draw(Room.shadowTopLeftCornerWall)
        }
    }

    fun regenerate(x: Int?, y: Int?) {
        this.clear()
        n = x ?: n
        m = y ?: m
        for (i in 0 until n) {
            val row = ArrayList<Int>()
            for (j in 0 until m) {
                if ((0..1).random() == 1)
                //row.add((1..4).random())
                    row.add(1)
                else
                    row.add(0)
            }
            this.add(row)
        }
        surroundWithZero()
        correct()
    }

    private fun correct() {
        var again = true
        while (again) {
            again = false
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (this[i][j] == 0) {
                        val t = Up(i, j).ToInt() + Down(i, j).ToInt() + Left(i, j).ToInt() + Right(i, j).ToInt()
                        if (t >= 3) {
                            this[i][j] = 1
                            again = true
                        }
                    }
                }
            }
        }
    }

    private fun surroundWithZero() {
        for (j in 0 until m) {
            this[0][j] = 0
            this[n - 1][j] = 0
        }
        for (i in 0 until n) {
            this[i][0] = 0
            this[i][m - 1] = 0
        }
    }

    fun empty(x: Int, y: Int) {
        this.clear()
        n = x ?: n
        m = y ?: m
        for (i in 0 until n) {
            val row = ArrayList<Int>()
            for (j in 0 until m) {
                row.add(0)
            }
            this.add(row)
        }
    }

    fun set(x: Int, y: Int, value: Int) {
        this[x][y] = value
        correct()
    }

    private fun Boolean.ToInt() = if (this) 1 else 0
}