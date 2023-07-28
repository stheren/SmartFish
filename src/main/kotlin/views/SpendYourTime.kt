package views

import Composants.Utils
import SpendYourTime.Connexion
import SpendYourTime.Images.Images
import SpendYourTime.Images.Skins
import SpendYourTime.models.Map_Syp
import SpendYourTime.models.Player
import SpendYourTime.models.Players
import SpendYourTime.models.Point
import WindowsAfk
import javafx.animation.AnimationTimer
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.util.Duration


class SpendYourTime private constructor() : StackPane() {
    companion object {
        var instance: SpendYourTime = SpendYourTime()

        const val STYLE_INPUT =
            "-fx-background-color: #000000, -fx-discord-blue; -fx-background-insets: 0, 1 1 1 1 ; -fx-background-radius: 0px; -fx-text-fill: #FFFFFF;"
    }

    private val view = Canvas(500.0, 500.0)
    private val gc: GraphicsContext = view.graphicsContext2D

    private var xOffsetView = 0.0
    private var yOffsetView = 0.0

    private var mouseX = 0.0
    private var mouseY = 0.0

    var Map_Syp = Map_Syp()

    // Contain all players send by the server
    var players = Players(Map_Syp)

    var player: Player? = null

    private var lastRefresh = 0L

    private var alert: VBox = VBox()

    val timerReset = Label("0")
    val timerGain = Label("0")

    val connexion = Connexion()

    init {
        connexion.start()
        connexion.setOnLoadMap {
            connexion.join(WindowsAfk.username ?: System.getProperty("user.name"), Map_Syp.getSpawnPoint());
        }

        Skins.instance
        VBox.setVgrow(this, Priority.ALWAYS)

        style = "-fx-background-color: #2c2f31;"
        children.add(view)

        val ui = Image(javaClass.getResourceAsStream("/assets/UI.png"))
        val selector = WritableImage(ui.pixelReader, Utils.VALUE, 0, Utils.VALUE, Utils.VALUE)
        val startNanoTime = System.nanoTime()

        view.onMouseMoved = EventHandler { event ->
            mouseX = event.x
            mouseY = event.y
        }

        view.onMousePressed = EventHandler { event ->
            doIfDontAlert {
                if (event.isPrimaryButtonDown) {
                    Map_Syp.move(player, Point(mouseX - (mouseX % 16), mouseY - (mouseY % 16)))
                    // Map.instance.set((event.x / 16).toInt(), (event.y / 16).toInt(), 1);
                } else if (event.isSecondaryButtonDown) {
                    xOffsetView = view.translateX - event.screenX
                    yOffsetView = view.translateY - event.screenY
                }
            }
        }

        view.translateY = -20.0
        view.onMouseDragged = EventHandler { event ->
            doIfDontAlert {
                if (event.isSecondaryButtonDown) {
                    view.translateX = event.screenX + xOffsetView
                    view.translateY = event.screenY + yOffsetView
                }
            }
        }

        view.scaleX = 0.95
        view.scaleY = 0.95
        view.onScroll = EventHandler { event ->
            doIfDontAlert {
                if (event.deltaY > 0) {
                    view.scaleX += if (view.scaleX < 10.0) 0.1 else 0.0
                    view.scaleY += if (view.scaleY < 10.0) 0.1 else 0.0
                } else {
                    view.scaleX -= if (view.scaleX > 0.5) 0.1 else 0.0
                    view.scaleY -= if (view.scaleY > 0.5) 0.1 else 0.0
                }
            }
        }

        view.onMouseEntered = EventHandler { _ ->
            scene.cursor = Cursor.HAND;
        }

        view.onMouseExited = EventHandler { _ ->
            scene.cursor = Cursor.DEFAULT;
        }

        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                val frame = (currentNanoTime - lastRefresh) / 1000000000.0
                timerReset.text = "FPS: ${(1 / frame).toInt()}"
                val t = (currentNanoTime - startNanoTime) / 1000000000.0


                // Clear the canvas
                gc.clearRect(0.0, 0.0, view.width, view.height)

                for (i in 0 until Map_Syp.getHeight()) {
                    for(j in 0 until Map_Syp.getWidth()) {
                        gc.drawImage(Images.get(Map_Syp.getFloor()[i][j]), (i * Utils.VALUE).toDouble(), (j * Utils.VALUE).toDouble())
                        gc.drawImage(Images.get(Map_Syp.getFirstLayer()[i][j]), (i * Utils.VALUE).toDouble(), (j * Utils.VALUE).toDouble())
                    }
                }

                for (i in 0 until Map_Syp.getHeight()) {
                    for (j in 0 until Map_Syp.getWidth()) {
                        players.forEach {
                            if (it.isOnline && it.pos.x - (it.pos.x % Utils.VALUE) == i * Utils.VALUE && it.pos.y - (it.pos.y % Utils.VALUE) == j * Utils.VALUE) {
                                it.draw(t) { image ->
                                    gc.drawImage(image, it.pos.x.toDouble(), it.pos.y.toDouble() - Utils.VALUE)
                                }
                                // Write the player name and center it
                                // gc.fillText(it.name, it.pos.x.toDouble() + (Utils.VALUE - it.name.length * 3.5) / 2, it.pos.y.toDouble() - Utils.VALUE - 5)
                            }
                        }
                        gc.drawImage(
                            Images.get(Map_Syp.getSecondLayer()[i][j]),
                            (i * Utils.VALUE).toDouble(),
                            (j * Utils.VALUE).toDouble()
                        )
                    }
                }


                gc.drawImage(
                    selector,
                    mouseX - (mouseX % 16),
                    mouseY - (mouseY % 16),
                    Utils.VALUE.toDouble(),
                    Utils.VALUE.toDouble()
                )
                lastRefresh = currentNanoTime
            }
        }.start()


        // Create right bar
        val bottomBar = HBox().apply {
            style = "-fx-background-color: #3c3f41;"
            maxHeight = Double.MIN_VALUE
            // maxWidth = 30.0
            setAlignment(this, Pos.BOTTOM_CENTER)
            alignment = Pos.CENTER
            spacing = 50.0
//            children.add(VBox().apply {
//                setExclusiveSize( 100.0, 25.0)
//                children.add(timerReset.apply {
//                    style = "-fx-text-fill: #FFFFFF; -fx-font-size: 10px;"
//                })
//                children.add(timerGain.apply {
//                    style = "-fx-text-fill: #FFFFFF; -fx-font-size: 10px;"
//                })
//            })
            children.add(Button().apply {
                text = "Random Skin"
                style = "-fx-background-color: #3c3f41; -fx-text-fill: #FFFFFF; -fx-font-size: 10px; -fx-border-color: #FFFFFF; -fx-border-width: 1px;"
                setOnAction {
                    player?.skin = Skins.instance.createRandomSkin()
                    connexion.change(player ?: return@setOnAction)
                }
            })
        }
        children.add(bottomBar)
    }

    fun popup(title: String, message: String, color: String) {
        popup(title,
            Pane(Label(message).apply {
                style = "-fx-text-fill: #FFFFFF; -fx-font-size: 15px;"
            }).apply {
                minWidth = 250.0
                minHeight = 75.0
            },
            color,
            null
        )
    }

    fun popup(title: String, body: Pane, color: String, action: (() -> Boolean)?) {
        Platform.runLater {
            alert = VBox()
            alert.spacing = 10.0
            alert.padding = Insets(5.0)
            alert.style =
                "-fx-border-color: $color; -fx-border-width: 2px; -fx-background-color: #3c3f41; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-padding: 5px;"

            alert.children.add(BorderPane().apply {
                left = Label(title).apply {
                    style = "-fx-text-fill: #FFFFFF; -fx-font-size: 15px; -fx-font-weight: bold;"
                }

                center = VBox().apply {
                    minWidth = 50.0
                    maxWidth = 50.0
                    prefWidth = 50.0
                }

                right = Button("").apply {
                    style =
                        "-fx-background-color: Transparent; -fx-border-color: $color; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
                    maxHeight = 20.0
                    maxWidth = 20.0
                    minHeight = 20.0
                    minWidth = 20.0
                    onMouseClicked = EventHandler { this@SpendYourTime.children.remove(alert) }
                    onMouseEntered = EventHandler {
                        style =
                            "-fx-background-color: $color; -fx-border-color: $color; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
                    }
                    onMouseExited = EventHandler {
                        style =
                            "-fx-background-color: Transparent; -fx-border-color: $color; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
                    }
                }
            })

            alert.children.add(body)

            if (action != null) {
                alert.children.add(HBox().apply {
                    alignment = Pos.CENTER_RIGHT
                    children.add(Button("Ok").apply {
                        style =
                            "-fx-background-color: TRANSPARENT; -fx-border-color: $color; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100; -fx-text-fill: $color; -fx-font-size: 10px; -fx-font-weight: bold;"
                        onMouseClicked = EventHandler {
                            if (action())
                                this@SpendYourTime.children.remove(alert)
                        }
                        onMouseEntered = EventHandler {
                            style =
                                "-fx-background-color: $color; -fx-border-color: $color; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100; -fx-text-fill: #FFFFFF; -fx-font-size: 10px; -fx-font-weight: bold;"
                        }
                        onMouseExited = EventHandler {
                            style =
                                "-fx-background-color: TRANSPARENT; -fx-border-color: $color; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100; -fx-text-fill: $color; -fx-font-size: 10px; -fx-font-weight: bold;"
                        }
                    })
                })
            }

            alert.maxHeight = Double.MIN_VALUE
            alert.maxWidth = Double.MIN_VALUE

            alert.effect = DropShadow(100.0, Color.BLACK)
            children.add(alert)
            alert.toFront()
        }
    }

    fun toast(title: String, body: Pane, color: String, time: Double, pos: Pos, width: Double) {
        Platform.runLater {
            val toast = VBox()
            toast.spacing = 10.0
            toast.padding = Insets(5.0)
            toast.style =
                "-fx-border-color: $color; -fx-border-width: 2px; -fx-background-color: #3c3f41; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-padding: 5px;"

            toast.children.add(HBox().apply {
                minWidth = width
                alignment = Pos.CENTER_LEFT
                children.add(Label(title).apply {
                    style = "-fx-text-fill: #FFFFFF; -fx-font-size: 15px; -fx-font-weight: bold;"
                })
            })

            toast.children.add(body)

            toast.maxHeight = Double.MIN_VALUE
            toast.maxWidth = Double.MIN_VALUE

            children.add(toast)
            setAlignment(toast, pos)
            setMargin(toast, Insets(10.0, 10.0, 10.0, 10.0))
            toast.toFront()

            val timeline = Timeline()
            timeline.keyFrames.add(
                KeyFrame(
                    Duration(time * 1000.0),
                    EventHandler {
                        children.remove(toast)
                    }
                )
            )
            timeline.play()
        }
    }

    fun displayError(title: String, message: String) {
        toast(title, HBox().apply {
            alignment = Pos.CENTER
            children.add(Label(message).apply {
                style = "-fx-text-fill: #FFFFFF; -fx-font-size: 12.5px;"
            })
        }, "-fx-discord-red", 2.5, Pos.BOTTOM_RIGHT, 300.0)
    }

    private fun doIfDontAlert(action: (() -> Unit)) {
        if (this.children.contains(alert)) {
            Thread {
                for (i in (0..5)) {
                    Platform.runLater {
                        alert.style =
                            "-fx-border-color: White; -fx-border-width: 2px; -fx-background-color: #3c3f41; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-padding: 5px;"
                    }
                    Thread.sleep(100)
                    Platform.runLater {
                        alert.style =
                            "-fx-border-color: Black; -fx-border-width: 2px; -fx-background-color: #3c3f41; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-padding: 5px;"
                    }
                    Thread.sleep(100)
                }
            }.start()
        } else {
            action()
        }
    }
}

