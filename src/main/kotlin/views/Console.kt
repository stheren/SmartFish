package views

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.TextArea
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox

class Console private constructor() : VBox() {
    companion object {
        val instance = Console()
    }

    private val consoleZone : TextArea
    private var numberOfMessage : Int = 0

    init {
        style = "-fx-background-color: #2C2F33;"
        setVgrow(this, Priority.ALWAYS)
        alignment = Pos.CENTER
        spacing = 10.0

        children.add(HBox().apply {
            alignment = Pos.CENTER
            children.add(Pane().apply {
                prefWidth = 66.6
                prefHeight = 200.0
                style = "-fx-background-color: #00bae0; -fx-background-radius: 25 0 0 25;"
            })
            children.add(VBox().apply {
                prefWidth = 350.0
                prefHeight = 200.0
                padding = Insets(10.0)
                style = "-fx-background-color: BLACK;"
                children.add(TextArea().apply {
                    consoleZone = this
                    prefWidth = 350.0
                    prefHeight = 200.0
                    style =
                        "-fx-text-fill: GRAY; -fx-font-size: 8px; -fx-font-weight: bold; -fx-background-color: Black, White ; -fx-background-insets: 0, 1 1 1 1 ; -fx-background-radius: 0px ;"
                })
            })
            children.add(Pane().apply {
                prefWidth = 66.6
                prefHeight = 200.0
                style = "-fx-background-color: #ff5f53; -fx-background-radius: 0 25 25 0;"
            })
        })
    }

    fun addText(text: String) {
        numberOfMessage++
        if(numberOfMessage > 100) {
            consoleZone.text = ""
            numberOfMessage = 0
        }
        consoleZone.appendText(text)
        consoleZone.scrollTop = Double.MAX_VALUE
    }
}
