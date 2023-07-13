package SpendYourTime.models

import SpendYourTime.Images.Images
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import javafx.scene.image.WritableImage

class Map private constructor() {
    val _floor =
        ArrayList<ArrayList<Int>>() // Contains the floor of the map (0 = empty, 1 2 3 4 = different types of floor)
    val _firstLayer =
        ArrayList<ArrayList<Int>>() // Contains the first layer of the map (0 = empty, other = id of the object)
    val _secondLayer =
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
    }

    private fun clear() {
        _floor.clear()
        _firstLayer.clear()
        _secondLayer.clear()
    }

    fun get(x: Int, y: Int): Int {
        return _floor[x][y]
    }

    fun setFloor(x: Int, y: Int, value: Int) {
        _floor[x][y] = value
    }

    fun setFirstLayer(x: Int, y: Int, value: Int) {
        _firstLayer[x][y] = value
    }

    fun setSecondLayer(x: Int, y: Int, value: Int) {
        _secondLayer[x][y] = value
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

}
