package SpendYourTime.models

import SpendYourTime.Images.Skins
import javafx.scene.image.Image
import javafx.scene.image.WritableImage

class Player() {
    companion object {
        val VALUE: Int = 16

        enum class AnimationState {
            IDLE, WALKING
        }

        enum class Direction {
            UP, DOWN, LEFT, RIGHT
        }
    }

    var uuid: String = ""
    var pos: Point = Point()
    var name: String = ""
    var skin: Skin = Skin(0, 0, 0, 0, 0)
    var move: Thread? = null
    var map : Map? = null

    var direction: Direction = Direction.DOWN
    var animationState: AnimationState = AnimationState.IDLE
    var lastAnimTime: Double = 0.0
    var anim: Int = 0
        set(value) {
            field = value % 6
        }


    constructor(uuid: String, name: String, pos: Point, skin: Skin, map: Map) : this() {
        this.uuid = uuid
        this.name = name
        this.pos = pos
        this.skin = skin
        this.map = map
    }

    private fun cutImage(img: Image): WritableImage {
        val t = VALUE * when (direction) {
            Direction.UP -> 6
            Direction.DOWN -> 18
            Direction.LEFT -> 0
            Direction.RIGHT -> 12
        }
        val l = VALUE * when (animationState) {
            AnimationState.IDLE -> 2
            AnimationState.WALKING -> 4
        }
        return WritableImage(img.pixelReader, anim * VALUE + t, l, VALUE, VALUE * 2)
    }

    fun draw(t: Double, func: (WritableImage) -> Unit) {
        if (Skins.instance.isLoaded) {
            return
        }
        func(cutImage(Skins.instance.bodies[skin.body]))
        func(cutImage(Skins.instance.outfits[skin.outfit]))
        func(cutImage(Skins.instance.hairs[skin.hair]))
        func(cutImage(Skins.instance.eyes[skin.eyes]))
        func(cutImage(Skins.instance.accessories[skin.accessory]))



        if (lastAnimTime + 0.1 < t) {
            anim++
            //animationState = AnimationState.IDLE
            lastAnimTime = t
        }
    }

    fun moveTo(destination: Point) {
        val x = destination.x
        val y = destination.y

        if (move != null) {
            move!!.interrupt()
        }

        move = Thread {
            if(map == null) return@Thread
            try {
                while (pos.x != x || pos.y != y) {
                    val nextPoint = when {
                        pos.x < x -> Point(pos.x + 1, pos.y)
                        pos.x > x -> Point(pos.x - 1, pos.y)
                        pos.y < y -> Point(pos.x, pos.y + 1)
                        else -> Point(pos.x, pos.y - 1)
                    }
                    if (map!!.isInWall(nextPoint.convert()) || map!!.isInWall(
                            nextPoint.add(15, 15).convert()
                        )
                    ) {
                        animationState = AnimationState.IDLE
                        return@Thread
                    }

                    animationState = AnimationState.WALKING
                    when {
                        pos.x < nextPoint.x -> direction = Direction.LEFT
                        pos.x > nextPoint.x -> direction = Direction.RIGHT
                        pos.y < nextPoint.y -> direction = Direction.DOWN
                        pos.y > nextPoint.y -> direction = Direction.UP
                    }
                    pos = nextPoint
                    Thread.sleep(5)
                }
                animationState = AnimationState.IDLE
            } catch (_: InterruptedException) {
                // do nothing
            }
        }
        move!!.start()
    }
}
