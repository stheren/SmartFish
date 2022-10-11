package views

import Composants.ButtonColor
import Place.Connexion
import Place.models.Color
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.canvas.Canvas
import javafx.scene.control.Alert
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.ProgressBar
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

class SmartPlace private constructor() : VBox() {
    companion object {
        var instance: SmartPlace = SmartPlace()

        private const val THE_VARIABLE_YOU_WANT_CHANGE = 5
    }

    private var showAlert = true

    private val place: Canvas = Canvas().apply {
        height = 500.0
        width =  500.0
        style =  "-fx-border-color: BLACK; -fx-border-width: 1;"
    }

    private val colorSelector = HBox().apply {
        spacing =   5.0
        alignment = Pos.CENTER
        style =     "-fx-background-color: #3c3f41; -fx-border-color: #000000; -fx-border-width: 1 0 0 0;"
        children.add(ButtonColor().apply {
            clicked = true
            color =   "#FFFFFF"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#E4E4E4"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#888888"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#222222"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#FFA7D1"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#E50000"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#E59500"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#A06A42"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#E5D900"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#94E044"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#02BE01"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#00D3DD"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#0083C7"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#0000EA"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#CF6EE4"
        })
        children.add(ButtonColor().apply {
            clicked = false
            color =   "#820080"
        })
    }

    private val progress = ProgressBar().apply {
        prefWidth = 500.0
        style =     "-fx-accent: GREEN;"
    }

    private var xOffsetSmartPlace = 0.0
    private var yOffsetSmartPlace = 0.0

    private var selectedColor: Color

    init {
        Connexion.initInstance(this)
        style = "-fx-background-color: #2b2b2b;"
        println("SmartPlace initialized")

        progress.progress = 1.0

        selectedColor =       Color()
        selectedColor.red =   255
        selectedColor.green = 255
        selectedColor.blue =  255

        children.add(place)
        place.scaleX -= 0.1
        place.scaleY -= 0.1

        place.onScroll = EventHandler { event ->
            println("Scroll ${event.deltaY}")
            if (event.deltaY > 0) {
                println("Zoom in")
                place.scaleX += if (place.scaleX < 10.0) 0.1 else 0.0
                place.scaleY += if (place.scaleY < 10.0) 0.1 else 0.0
            } else {
                println("Zoom out")
                place.scaleX -= if (place.scaleX > 0.5) 0.1 else 0.0
                place.scaleY -= if (place.scaleY > 0.5) 0.1 else 0.0
            }
        }

        place.onMousePressed = EventHandler { event ->
            if (event.isPrimaryButtonDown) {
                if (progress.progress == 1.0) {
                    val x: Int = (event.x / 5).toInt()
                    val y: Int = (event.y / 5).toInt()
                    Connexion.instance.request(x, y, selectedColor.red, selectedColor.green, selectedColor.blue)
                    Thread {
                        progress.style += "-fx-accent: DARKRED;"
                        for (t in 0..(THE_VARIABLE_YOU_WANT_CHANGE * 100)) {
                            Thread.sleep(10)
                            Platform.runLater {
                                progress.progress = t.toDouble() / (THE_VARIABLE_YOU_WANT_CHANGE * 100)
                                when (t) {
                                    250 -> {
                                        progress.style += "-fx-accent: RED;"
                                    }

                                    500 -> {
                                        progress.style += "-fx-accent: ORANGE;"
                                    }

                                    750 -> {
                                        progress.style += "-fx-accent: LIGHTGREEN;"
                                    }
                                }

                            }
                        }
                        Platform.runLater {
                            progress.progress = 1.0
                            progress.style +=   "-fx-accent: GREEN;"
                        }
                    }.start()
                } else {
                    if(showAlert) {
                        Platform.runLater {
                            val alert = Alert(Alert.AlertType.WARNING)
                            alert.title = "Trop de pixel !!"
                            alert.contentText =
                                "Vous ne pouvez pas placer plus de pixel pour le moment, attendez un peu ! ($THE_VARIABLE_YOU_WANT_CHANGE secondes)"
                            alert.buttonTypes.add(ButtonType("FUCK THIS ALERT", ButtonBar.ButtonData.FINISH))
                            val result = alert.showAndWait()
                            if (result.get().buttonData == ButtonBar.ButtonData.FINISH) {
                                alert.close()
                                showAlert = false
                            }
                        }
                    }
                }
            } else if (event.isSecondaryButtonDown) {
                xOffsetSmartPlace = place.translateX - event.screenX
                yOffsetSmartPlace = place.translateY - event.screenY
            }
        }

        place.onMouseDragged = EventHandler { event ->
            if (event.isSecondaryButtonDown) {
                place.translateX = event.screenX + xOffsetSmartPlace
                place.translateY = event.screenY + yOffsetSmartPlace
            }
        }

        children.add(colorSelector)
        children.add(progress)
    }

    fun drawPlace() {
        //Platform.runLater { SmartPlace.graphicsContext2D.clearRect(0.0, 0.0, 500.0, 500.0) }
        for (i in 0..99) {
            for (j in 0..99) {
                val newColor = Connexion.instance.place?.find(i, j)?.color?.toPaint()
                Platform.runLater {
                    place.graphicsContext2D.fill = newColor
                    place.graphicsContext2D.fillRect(i * 5.0, j * 5.0, 5.0, 5.0)
                }
            }
        }
    }

    fun updatePlace() {
        for (p in Connexion.instance.place?.updated!!) {
            Platform.runLater {
                place.graphicsContext2D.fill = p.color.toPaint()
                place.graphicsContext2D.fillRect(p.x * 5.0, p.y * 5.0, 5.0, 5.0)
            }
        }
    }

    fun onColorClicked(btn: ButtonColor) {
        for (i in colorSelector.children) {
            if (i is ButtonColor) {
                i.resetBackground()
            }
        }
        selectedColor.red =   btn.rgbColor.red
        selectedColor.green = btn.rgbColor.green
        selectedColor.blue =  btn.rgbColor.blue
    }
}
