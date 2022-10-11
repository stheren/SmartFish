package SpendYourTime.models

import SpendYourTime.Images.Room
import javafx.scene.image.WritableImage

class Map private constructor() : ArrayList<ArrayList<Int>>() {
    companion object {
        var n = 0
        var m = 0
        val instance: Map = Map()

        fun loadMap(Arr: Array<Array<Int>>) {
            instance.clear()
            n = Arr.size
            m = Arr[0].size
            for (i in 0 until n) {
                instance.add(ArrayList())
                for (j in 0 until m) {
                    instance[i].add(Arr[i][j])
                }
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


    fun set(x: Int, y: Int, value: Int) {
        this[x][y] = value
    }

    private val players = ArrayList<Player>()

    fun addPlayer(uuid: String, name: String, pos: Point, skin: Skin) {
        val player = players.find { it.uuid == uuid }
        if (player != null) {
            if (player.pos != pos) {
                player.animationState = Player.Companion.AnimationState.WALKING
                if (player.pos.x < pos.x) {
                    player.direction = Player.Companion.Direction.LEFT
                } else if (player.pos.x > pos.x) {
                    player.direction = Player.Companion.Direction.RIGHT
                } else if (player.pos.y < pos.y) {
                    player.direction = Player.Companion.Direction.DOWN
                } else if (player.pos.y > pos.y) {
                    player.direction = Player.Companion.Direction.UP
                }
                player.pos = pos
            }

            player.name = name
            player.skin = skin
        } else {
            players.add(Player(uuid, name, pos, skin))
        }
    }

    fun getPlayers() = players

}
