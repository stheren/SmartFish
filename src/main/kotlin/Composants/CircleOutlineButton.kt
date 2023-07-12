package Composants

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.control.Button

class CircleOutlineButton: Button() {

    var filled = false
    lateinit var color: String

    init {
        Platform.runLater {
            style =
                "-fx-background-color: Transparent; -fx-border-color: $color; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
            prefHeight = 20.0
            prefWidth = 20.0
            minHeight = 20.0
            minWidth = 20.0
            maxHeight = 20.0
            maxWidth = 20.0

            onMouseEntered = EventHandler {
                style =
                    "-fx-background-color: $color; -fx-border-color: $color; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
            }

            onMouseExited = EventHandler {
                style =
                    "-fx-background-color: ${if (filled) color else "Transparent"}; -fx-border-color: $color; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
            }
        }
    }


}
