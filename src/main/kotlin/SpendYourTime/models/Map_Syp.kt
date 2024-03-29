package SpendYourTime.models

import Composants.Utils
import com.fasterxml.jackson.annotation.JsonIgnore
import views.SpendYourTime

class Map_Syp constructor() {
    // Contains the floor of the map (0 = empty, 1 2 3 4 = different types of floor)
    private val _floor = ArrayList<ArrayList<Case>>()

    // Contains the first layer of the map (0 = empty, other = id of the object)
    private val _firstLayer = ArrayList<ArrayList<Case>>()

    // Contains the second layer of the map (0 = empty, other = id of the object)
    private val _secondLayer = ArrayList<ArrayList<Case>>()

    // Contains the hitbox of the map (0 = wall, 1 = empty, 2 = spawn point, 3 = action point)
    private val _hitBox = ArrayList<ArrayList<Int>>()

    private var _width = 0
    private var _height = 0

    @JsonIgnore
    var move: Thread? = null

    /** Setters and getters **/

    fun getWidth(): Int {
        return _width
    }

    fun setWidth(value: Int) {
        _width = value
    }

    fun getHeight(): Int {
        return _height
    }

    fun setHeight(value: Int) {
        _height = value
    }

    fun getFloor(): ArrayList<ArrayList<Case>> {
        return _floor
    }

    fun getFloorCase(x: Int, y: Int): Case {
        return _floor[x][y]
    }

    fun setFloor(x: Int, y: Int, value: Case) {
        if (x < 0 || x >= _width || y < 0 || y >= _height) return
        _floor[x][y] = value
    }

    fun getFirstLayer(): ArrayList<ArrayList<Case>> {
        return _firstLayer
    }

    fun getFirstLayerCase(x: Int, y: Int): Case {
        return _firstLayer[x][y]
    }

    fun setFirstLayer(x: Int, y: Int, value: Case) {
        if (x < 0 || x >= _width || y < 0 || y >= _height) return
        _firstLayer[x][y] = value
    }

    fun getSecondLayer(): ArrayList<ArrayList<Case>> {
        return _secondLayer
    }

    fun getSecondLayerCase(x: Int, y: Int): Case {
        return _secondLayer[x][y]
    }

    fun setSecondLayer(x: Int, y: Int, value: Case) {
        if (x < 0 || x >= _width || y < 0 || y >= _height) return
        _secondLayer[x][y] = value
    }

    fun getHitbox(): ArrayList<ArrayList<Int>> {
        return _hitBox
    }

    fun getHitboxCase(x: Int, y: Int): Int {
        return _hitBox[x][y]
    }

    fun setHitbox(toInt: Int, toInt1: Int, toInt2: Int) {
        if (toInt < 0 || toInt >= _width || toInt1 < 0 || toInt1 >= _height) return
        _hitBox[toInt][toInt1] = toInt2
    }

    /** Methods **/

    fun loadMap(mapSyp :  Map_Syp) {
        clear()
        _width = mapSyp._floor.size
        _height = mapSyp._floor[0].size
        for (i in 0 until _width) {
            _floor.add(ArrayList())
            _firstLayer.add(ArrayList())
            _secondLayer.add(ArrayList())
            _hitBox.add(ArrayList())
            for (j in 0 until _height) {
                _floor[i].add(Case(mapSyp._floor[i][j].file, mapSyp._floor[i][j].index))
                _firstLayer[i].add(Case(mapSyp._firstLayer[i][j].file, mapSyp._firstLayer[i][j].index))
                _secondLayer[i].add(Case(mapSyp._secondLayer[i][j].file, mapSyp._secondLayer[i][j].index))
                _hitBox[i].add(mapSyp._hitBox[i][j])
            }
        }
    }

    fun create(width: Int, height: Int) {
        this._width = width
        this._height = height
        for (i in 0 until this._width) {
            _floor.add(ArrayList())
            _firstLayer.add(ArrayList())
            _secondLayer.add(ArrayList())
            _hitBox.add(ArrayList())
            for (j in 0 until this._height) {
                _floor[i].add(Case(0, 0))
                _firstLayer[i].add(Case(0, 0))
                _secondLayer[i].add(Case(0, 0))
                _hitBox[i].add(0)
            }
        }
    }

    fun clear() {
        _floor.clear()
        _firstLayer.clear()
        _secondLayer.clear()
        _hitBox.clear()
    }

    fun isInWall(nextPoint: Point): Boolean {
        return _hitBox[nextPoint.x][nextPoint.y] == 1
    }

    fun getSpawnPoint(): Point {
        val possibleSpawnPoints = ArrayList<Point>()
        for (i in 0 until _width) {
            for (j in 0 until _height) {
                if (_hitBox[i][j] == 2) {
                    possibleSpawnPoints.add(Point(i, j))
                }
            }
        }
        if(possibleSpawnPoints.isNotEmpty()) {
            return possibleSpawnPoints.random().convertForReal()
        }
        return Point(3 * Utils.VALUE, 3 * Utils.VALUE)
    }

    fun move(player: Player?, destination: Point) {
        if (player == null) return
        val x = destination.x
        val y = destination.y

        if (move != null) {
            move!!.interrupt()
        }

        move = Thread {
            try {
                while (player.pos.x != x || player.pos.y != y) {
                    val nextPoint = when {
                        player.pos.x < x -> Point(player.pos.x + 1, player.pos.y)
                        player.pos.x > x -> Point(player.pos.x - 1, player.pos.y)
                        player.pos.y < y -> Point(player.pos.x, player.pos.y + 1)
                        else -> Point(player.pos.x, player.pos.y - 1)
                    }
                    if (isInWall(nextPoint.convertForTable()) || isInWall(nextPoint.add(15, 15).convertForTable())
                    ) {
                        player.animationState = Player.Companion.AnimationState.IDLE
                        SpendYourTime.instance.connexion.change(player)
                        return@Thread
                    }

                    player.animationState = Player.Companion.AnimationState.WALKING
                    when {
                        player.pos.x < nextPoint.x -> player.direction = Player.Companion.Direction.LEFT
                        player.pos.x > nextPoint.x -> player.direction = Player.Companion.Direction.RIGHT
                        player.pos.y < nextPoint.y -> player.direction = Player.Companion.Direction.DOWN
                        player.pos.y > nextPoint.y -> player.direction = Player.Companion.Direction.UP
                    }
                    player.pos = nextPoint
                    SpendYourTime.instance.connexion.change(player)
                    Thread.sleep(5)
                }
                player.animationState = Player.Companion.AnimationState.IDLE
                SpendYourTime.instance.connexion.change(player)
            } catch (_: InterruptedException) {
                // do nothing
            }
        }
        move!!.start()
    }
}
