package views

import SpendYourTime.Composants.Score
import SpendYourTime.Connexion
import SpendYourTime.Images.Skins
import SpendYourTime.models.Map
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
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.input.KeyCode
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.util.Duration


class SpendYourTime private constructor() : StackPane() {
    companion object {
        var instance: SpendYourTime = SpendYourTime()

        const val STYLE_INPUT =
            "-fx-background-color: #000000, -fx-discord-blue; -fx-background-insets: 0, 1 1 1 1 ; -fx-background-radius: 0px; -fx-text-fill: #FFFFFF;"

    }

    private val VALUE = 16.0

    private val view = Canvas(480.0, 480.0)
    private val gc: GraphicsContext = view.graphicsContext2D

    private var xOffsetView = 0.0
    private var yOffsetView = 0.0

    private var mouseX = 0.0
    private var mouseY = 0.0

    private var playerX = 0.0
    private var playerY = 0.0

    private var lastRefresh = 0L

    private var alert: VBox = VBox()

    public var scoreBoard: ArrayList<Score> = arrayListOf()

    val timerReset = Label("0")
    val timerGain = Label("0")

    init {
        Connexion.instance.start()
        Skins.instance
        VBox.setVgrow(this, javafx.scene.layout.Priority.ALWAYS)

        style = "-fx-background-color: #2c2f31;"
        children.add(view)

        val ui = Image(javaClass.getResourceAsStream("/assets/UI.png"))
        val selector = WritableImage(ui.pixelReader, VALUE.toInt(), 0, VALUE.toInt(), VALUE.toInt())
        val startNanoTime = System.nanoTime()

        view.onMouseMoved = EventHandler { event ->
            mouseX = event.x
            mouseY = event.y
        }

        view.onMousePressed = EventHandler { event ->
            doIfDontAlert {
                if (event.isPrimaryButtonDown) {
//                Map.instance.regenerate(30, 30)
//                Map.instance.set((event.x/16).toInt(), (event.y/16).toInt(), 1)
                    Connexion.instance.build((event.x / 16).toInt(), (event.y / 16).toInt())
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

        WindowsAfk.controller.AreaToTape.onKeyPressed = EventHandler { event ->
            doIfDontAlert {
                when (event.code) {
                    KeyCode.UP -> Connexion.instance.move(0, -1)
                    KeyCode.DOWN -> Connexion.instance.move(0, 1)
                    KeyCode.LEFT -> Connexion.instance.move(-1, 0)
                    KeyCode.RIGHT -> Connexion.instance.move(1, 0)
                    else -> {}
                }
            }
        }

        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                val frame = (currentNanoTime - lastRefresh) / 1000000000.0
                WindowsAfk.controller.FPS.text = "FPS: ${(1 / frame).toInt()}"
                val t = (currentNanoTime - startNanoTime) / 1000000000.0


                // Clear the canvas
                gc.clearRect(0.0, 0.0, view.width, view.height)

                // Draw the UI
                Map.instance.forEachIndexed { i, list ->
                    list.forEachIndexed { j, value ->
                        if (value == 0) {
                            Map.instance.drawWall(i, j) {
                                gc.drawImage(it, i * VALUE, j * VALUE, VALUE, VALUE)
                            }
                        } else {
                            Map.instance.drawFloor(i, j, value) {
                                gc.drawImage(it, i * VALUE, j * VALUE)
                            }
                        }
                    }
                }

                Map.instance.getPlayers().forEach {
                    //gc.strokeRect(it.pos.x.toDouble(), it.pos.y.toDouble(), VALUE, VALUE)
                    it.draw(t) { image ->
                        gc.drawImage(image, it.pos.x.toDouble(), it.pos.y.toDouble() - VALUE)
                    }
                    // Write the player name
                    gc.fillText(it.name, it.pos.x.toDouble() + 8, it.pos.y.toDouble() - 16, 32.0)
                }
                gc.drawImage(selector, mouseX - (mouseX % 16), mouseY - (mouseY % 16), VALUE, VALUE)
                lastRefresh = currentNanoTime
            }
        }.start()

        val nameField = TextField()
        val teamChoice = ComboBox<String>()
        val choice = arrayListOf("Facebook", "Amazon", "Google", "Microsoft")
        nameField.style = STYLE_INPUT
        teamChoice.style = STYLE_INPUT

        // Make Choice of team and name
        popup("Who are you ?", VBox().apply {
            children.add(nameField.apply {
                promptText = "Name"
                textProperty().addListener { _, _, newValue ->
                    if (newValue.length > 10) {
                        text = newValue.substring(0, 10)
                    }
                }
            })
            children.add(teamChoice.apply {
                items.addAll(choice)
                maxWidth = Double.MAX_VALUE
            })
        }, "-fx-discord-green") {
            nameField.style = STYLE_INPUT
            teamChoice.style = STYLE_INPUT
            if (nameField.text.isNotEmpty() && teamChoice.value != null) {
                Connexion.instance.join(nameField.text, choice.indexOf(teamChoice.value) + 1)
                return@popup true
            } else {
                if (nameField.text.isEmpty()) {
                    nameField.promptText = "Name is empty"
                    nameField.style ="-fx-background-color: #000000, -fx-discord-red; -fx-background-insets: 0, 1 1 1 1 ; -fx-background-radius: 0px; -fx-text-fill: #FFFFFF;"

                }
                if (teamChoice.value == null) {
                    teamChoice.style ="-fx-background-color: #000000, -fx-discord-red; -fx-background-insets: 0, 1 1 1 1 ; -fx-background-radius: 0px; -fx-text-fill: #FFFFFF;"
                }
                return@popup false
            }
        }

        // Create bottom bar
        val bottomBar = HBox().apply {
            style = "-fx-background-color: #3c3f41;"
            maxHeight = Double.MIN_VALUE
            setAlignment(this, Pos.BOTTOM_CENTER)
            alignment = Pos.CENTER
            spacing = 50.0
            for (name in choice) {
                val s = Score(name)
                children.add(s)
                scoreBoard.add(s)
            }
            children.add(VBox().apply {
                children.add(timerReset.apply {
                    style = "-fx-text-fill: #FFFFFF; -fx-font-size: 10px;"
                })
                children.add(timerGain.apply {
                    style = "-fx-text-fill: #FFFFFF; -fx-font-size: 10px;"
                })
            })

        }
        children.add(bottomBar)


    }

    fun popup(title: String, body: Pane, color: String, action: (() -> Boolean)?) {
        Platform.runLater {
            alert = VBox()
            alert.spacing = 10.0
            alert.padding = javafx.geometry.Insets(5.0)
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

//                right = Button("").apply {
//                    style =
//                        "-fx-background-color: Transparent; -fx-border-color: $color; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
//                    maxHeight = 20.0
//                    maxWidth = 20.0
//                    minHeight = 20.0
//                    minWidth = 20.0
//                    onMouseClicked = EventHandler { this@SpendYourTime.children.remove(alert) }
//                    onMouseEntered = EventHandler {
//                        style =
//                            "-fx-background-color: $color; -fx-border-color: $color; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
//                    }
//                    onMouseExited = EventHandler {
//                        style =
//                            "-fx-background-color: Transparent; -fx-border-color: $color; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
//                    }
//                }
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

    fun toast(title: String, body: Pane, color: String, time: Double, pos: Pos) {
        Platform.runLater {
            val toast = VBox()
            toast.spacing = 10.0
            toast.padding = javafx.geometry.Insets(5.0)
            toast.style =
                "-fx-border-color: $color; -fx-border-width: 2px; -fx-background-color: #3c3f41; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-padding: 5px;"

            toast.children.add(HBox().apply {
                minWidth = 200.0
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
        }, "-fx-discord-red", 2.5, Pos.BOTTOM_RIGHT)
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

