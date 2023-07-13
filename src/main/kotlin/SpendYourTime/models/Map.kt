package SpendYourTime.models

import SpendYourTime.Images.Items
import SpendYourTime.Images.Room
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import javafx.scene.image.WritableImage

class Map private constructor() {
    val _floor =
        ArrayList<ArrayList<Int>>() // Contains the floor of the map (0 = empty, 1 2 3 4 = different types of floor)
    val _firstLayer =
        ArrayList<ArrayList<Int>>() // Contains the first layer of the map (0 = empty, other = id of the object)
    private val _secondLayer =
        ArrayList<ArrayList<Int>>() // Contains the second layer of the map (0 = empty, other = id of the object)

    companion object {
        var n = 0
        var m = 0
        val instance: Map = Map()
    }

    fun loadMap(Arr: Array<Array<Int>>) {
        instance.clear()
        n = Arr.size
        m = Arr[0].size
        for (i in 0 until n) {
            _floor.add(ArrayList())
            for (j in 0 until m) {
                _floor[i].add(Arr[i][j])
            }
        }
    }

    fun STATIC_MAP() {
        // Load the map from the file "map.txt"
        Map::class.java.getResource("/assets/map.json")?.let { it ->
            JsonMapper().readValue<Array<Array<Int>>>(it.readText()).let {
                loadMap(it)
            }
        }
        // Filled the first layer with 0 (30x30)
        for (i in 0 until n) {
            _firstLayer.add(ArrayList())
            for (j in 0 until m) {
                _firstLayer[i].add(0)
            }
        }
        // Filled the second layer with 0 (30x30)
        for (i in 0 until n) {
            _secondLayer.add(ArrayList())
            for (j in 0 until m) {
                _secondLayer[i].add(0)
            }
        }

        // Add the desk
        _firstLayer[14][16] = 1
        _firstLayer[15][16] = 2
        _firstLayer[15][15] = 3

        // Add the chair
        _firstLayer[14][14] = 4
        _firstLayer[14][15] = 5

    }


    private fun clear() {
        _floor.clear()
        _firstLayer.clear()
        _secondLayer.clear()
    }

    fun get(x: Int, y: Int): Int {
        return _floor[x][y]
    }

    private fun Right(i: Int, j: Int): Boolean {
        return i + 1 < n && _floor[i + 1][j] >= 1
    }

    private fun Left(i: Int, j: Int): Boolean {
        return i - 1 >= 0 && _floor[i - 1][j] >= 1
    }

    private fun Up(i: Int, j: Int): Boolean {
        return j - 1 >= 0 && _floor[i][j - 1] >= 1
    }

    private fun Down(i: Int, j: Int): Boolean {
        return j + 1 < m && _floor[i][j + 1] >= 1
    }

    private fun LeftUpCorner(i: Int, j: Int): Boolean {
        return i - 1 >= 0 && j - 1 >= 0 && _floor[i - 1][j - 1] >= 1
    }

    private fun RightUpCorner(i: Int, j: Int): Boolean {
        return i + 1 < n && j - 1 >= 0 && _floor[i + 1][j - 1] >= 1
    }

    private fun LeftDownCorner(i: Int, j: Int): Boolean {
        return i - 1 >= 0 && j + 1 < m && _floor[i - 1][j + 1] >= 1
    }

    private fun RightDownCorner(i: Int, j: Int): Boolean {
        return i + 1 < n && j + 1 < m && _floor[i + 1][j + 1] >= 1
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
            1 -> draw(Room.floor_1)
            2 -> draw(Room.floor_2)
            3 -> draw(Room.floor_3)
            4 -> draw(Room.floor_4)
            5 -> draw(Room.floor_5)
            6 -> draw(Room.floor_6)
            7 -> draw(Room.floor_7)
            8 -> draw(Room.floor_8)
        }
        when {
            !Left(x, y) && !Up(x, y) -> draw(Room.shadowTopLeftWall)
            !Left(x, y) -> draw(Room.shadowLeftWall)
            !Up(x, y) -> draw(Room.shadowTopWall)
            !LeftUpCorner(x, y) -> draw(Room.shadowTopLeftCornerWall)
        }
    }


    fun set(x: Int, y: Int, value: Int) {
        _floor[x][y] = value
    }

    private val players = ArrayList<Player>()

    fun addPlayer(uuid: String, name: String, pos: Point, skin: Skin): Player {
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
        players.sortBy { it.pos.y }
        return players.find { it.uuid == uuid }!!
    }

    fun getPlayers() = players

    fun isInWall(nextPoint: Point): Boolean {
        return _floor[nextPoint.x][nextPoint.y] == 0
    }

    fun drawFirstLayer(i: Int, j: Int, value: Int, draw: (WritableImage) -> Unit) {
        draw(Items.get(value))
    }

}
