package views

import SpendYourTime.models.Map
import WindowsAfk
import javafx.animation.AnimationTimer
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.input.KeyCode
import javafx.scene.layout.VBox


class SpendYourTime : VBox() {
    private val VALUE = 16.0

    private val view = Canvas(500.0, 500.0)
    private val gc: GraphicsContext = view.graphicsContext2D

    private var xOffsetView = 0.0
    private var yOffsetView = 0.0

    private var mouseX = 0.0
    private var mouseY = 0.0

    private var playerX = 0.0
    private var playerY = 0.0

    private var lastRefresh = 0L

    init {
        style = "-fx-background-color: #3c3f41;"
        children.add(view)

        val player = Image(javaClass.getResourceAsStream("/assets/skin.png"))
        val ui = Image(javaClass.getResourceAsStream("/assets/UI.png"))
        val selector = WritableImage(ui.pixelReader, VALUE.toInt(), 0, VALUE.toInt(), VALUE.toInt())
        val startNanoTime = System.nanoTime()

        var anim = 0
//        Map.instance.empty(30, 30);
        Map.instance.regenerate(30, 30)


        view.onMouseMoved = EventHandler { event ->
            mouseX = event.x
            mouseY = event.y
        }

        view.onMousePressed = EventHandler { event ->
            if (event.isPrimaryButtonDown) {
                Map.instance.regenerate(30, 30)
//                Map.instance.set((event.x/16).toInt(), (event.y/16).toInt(), 1)
            } else if (event.isSecondaryButtonDown) {
                xOffsetView = view.translateX - event.screenX
                yOffsetView = view.translateY - event.screenY
            }
        }

        view.onMouseDragged = EventHandler { event ->
            if (event.isSecondaryButtonDown) {
                view.translateX = event.screenX + xOffsetView
                view.translateY = event.screenY + yOffsetView
            }
        }

        view.onScroll = EventHandler { event ->
            println("Scroll ${event.deltaY}")
            if (event.deltaY > 0) {
                view.scaleX += if (view.scaleX < 10.0) 0.1 else 0.0
                view.scaleY += if (view.scaleY < 10.0) 0.1 else 0.0
            } else {
                view.scaleX -= if (view.scaleX > 0.5) 0.1 else 0.0
                view.scaleY -= if (view.scaleY > 0.5) 0.1 else 0.0
            }
        }

        WindowsAfk.controller.AreaToTape.onKeyPressed = EventHandler { event ->
            when (event.code) {
                KeyCode.UP -> playerY -= 4
                KeyCode.DOWN -> playerY += 4
                KeyCode.LEFT -> playerX -= 4
                KeyCode.RIGHT -> playerX += 4
                else -> {}
            }
        }


        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                val frame = (currentNanoTime - lastRefresh) / 1000000000.0
                WindowsAfk.controller.FPS.text = "FPS: ${1 / frame}"
                if ((1 / frame) < 50) {
                    println("FPS: ${1 / frame}")
                }
                val t = (currentNanoTime - startNanoTime) / 1000000000.0

                anim = t.toInt() % 6
                val newImage = WritableImage(
                    player.pixelReader,
                    anim * VALUE.toInt(),
                    VALUE.toInt() * 2,
                    VALUE.toInt(),
                    VALUE.toInt() * 2
                )
                gc.clearRect(0.0, 0.0, view.width, view.height)


                Map.instance.forEachIndexed { i, list ->
                    list.forEachIndexed { j, value ->
                        if (value == 0) {
                            Map.instance.drawWall(i, j) {
                                gc.drawImage(it, i * VALUE, j * VALUE, VALUE, VALUE)
                            }
                        } else {
                            Map.instance.drawFloor(i, j) {
                                gc.drawImage(it, i * VALUE, j * VALUE)
                            }
                        }
                    }
                }

                gc.drawImage(selector, mouseX - (mouseX % 16), mouseY - (mouseY % 16), VALUE, VALUE)

                gc.drawImage(newImage, playerX, playerY)
                lastRefresh = currentNanoTime
            }
        }.start()
    }
}