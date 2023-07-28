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
    lateinit var pos: Point
    var name: String = ""
    var skin: Skin = Skin(0, 0, 0, 0, 0)

    var direction: Direction = Direction.DOWN
    var animationState: AnimationState = AnimationState.IDLE
    var lastAnimTime: Double = 0.0
    var anim: Int = 0
        set(value) {
            field = value % 6
        }

    var score: Int = 0
    var isOnline: Boolean = false

    constructor(uuid: String, name: String, pos: Point, skin: Skin, score: Int,isOnline: Boolean) : this() {
        this.uuid = uuid
        this.name = name
        this.pos = pos
        this.skin = skin
        this.score = score
        this.isOnline = isOnline
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
        func(cutImage(Skins.instance.getBody(skin.body)))
        func(cutImage(Skins.instance.getOutfit(skin.outfit)))
        func(cutImage(Skins.instance.getHair(skin.hair)))
        func(cutImage(Skins.instance.getEye(skin.eyes)))
        func(cutImage(Skins.instance.getAccessory(skin.accessory)))



        if (lastAnimTime + 0.1 < t) {
            anim++
            //animationState = AnimationState.IDLE
            lastAnimTime = t
        }
    }

    override fun toString(): String {
        return "Player(uuid='$uuid', pos=$pos, name='$name', skin=$skin, direction=$direction, animationState=$animationState, lastAnimTime=$lastAnimTime, anim=$anim, score=$score, isOnline=$isOnline)"
    }
}
