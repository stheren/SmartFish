package Composants

import AfkController
import Place.models.Color
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.control.Button


class ButtonColor: Button() {

    companion object {
        const val BORDER_BACKGROUND_0 = "-fx-border-radius: 0; -fx-background-radius: 0;"
        const val BORDER_BACKGROUND_100 = "-fx-border-radius: 100; -fx-background-radius: 100;"
    }

    lateinit var color: String
    lateinit var rgbColor : Color
    var clicked = false

    init {
        Platform.runLater {
            style = "-fx-background-color: $color; -fx-border-color: #000000; -fx-border-width: 0;"
            style += if (clicked) BORDER_BACKGROUND_0 else BORDER_BACKGROUND_100
            prefHeight = 25.0
            prefWidth = 25.0
            rgbColor = Color().fromHex(color)
        }

        onAction = EventHandler {
            AfkController.instance.onColorClicked(this)
            style += BORDER_BACKGROUND_0
        }
    }

    fun resetBackground() {
        style += BORDER_BACKGROUND_100
    }

}
