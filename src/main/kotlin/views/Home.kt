package views

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox


class Home private constructor() : VBox() {
    companion object {
        val instance = Home()

        private const val PATCH_STYLE = "-fx-text-fill: #93baba; -fx-font-size: 10px;"
    }

    init {
        alignment = Pos.CENTER
        children.add(Label("SmartFish").apply {
            style =
                "-fx-font-size: 60px; -fx-text-fill: linear-gradient(from 25% 25% to 75% 75%,-fx-discord-red, -fx-discord-yellow, -fx-discord-blue); -fx-font-weight: bold;"
        })
        style = "-fx-background-color: #2C2F33;"
        setVgrow(this, Priority.ALWAYS)

        children.add(Label("Version 2.0.0").apply {
            style = PATCH_STYLE
        })
    }

}
