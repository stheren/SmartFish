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

    var direction: Direction = Direction.DOWN
    var animationState: AnimationState = AnimationState.IDLE
    var lastAnimTime : Double = 0.0
    var anim: Int = 0
        set(value) {
            field = value % 6
        }


    constructor(uuid: String, name: String, pos: Point, skin: Skin) : this() {
        this.uuid = uuid
        this.name = name
        this.pos = pos
        this.skin = skin
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



        if(lastAnimTime + 0.1 < t) {
            anim++
            animationState = AnimationState.IDLE
            lastAnimTime = t
        }
    }
}
