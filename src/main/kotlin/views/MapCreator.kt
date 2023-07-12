package views

import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.input.KeyCode
import javafx.scene.layout.*
import org.json.JSONArray
import java.io.File

class MapCreator private constructor() : StackPane() {
    companion object {
        var instance: MapCreator = MapCreator()
    }

    var selectedButton : Button = Button()

    val buttons = Array(30) { arrayOfNulls<Button>(30) }

    val colors = arrayListOf(
        "#000000",
        "#0000FF",
        "#00FF00",
        "#00FFFF",
        "#FF0000",
        "#FF00FF",
        "#FFFF00",
        "#FFFFFF",
        "#808080"
    )

    init {
        VBox.setVgrow(this, Priority.ALWAYS)
        val gridPane = GridPane()
        val bottomBar = createBottomBar()

        for (row in 0 until 30) {
            for (column in 0 until 30) {
                val button = createButton()
                buttons[row][column] = button
                gridPane.add(button, row, column)
            }
        }

         setOnKeyPressed {
            when(it.code) {
                KeyCode.DIGIT0 -> setButton(0)
                KeyCode.DIGIT1 -> setButton(1)
                KeyCode.DIGIT2 -> setButton(2)
                KeyCode.DIGIT3 -> setButton(3)
                KeyCode.DIGIT4 -> setButton(4)
                KeyCode.DIGIT5 -> setButton(5)
                KeyCode.DIGIT6 -> setButton(6)
                KeyCode.DIGIT7 -> setButton(7)
                KeyCode.DIGIT8 -> setButton(8)
                else -> println("other")
            }
        }

        children.addAll(gridPane, bottomBar)
    }

    fun setButton(num : Int){
        selectedButton.style = "-fx-background-color: ${colors[num]};"
        selectedButton.text = num.toString()
    }

    private fun createButton(): Button {
        val button = Button("0")
        button.onMouseEntered = EventHandler {
            selectedButton = button
        }
        return button
    }

    private fun createBottomBar(): HBox {
        val saveButton = Button("Save")
        saveButton.setOnAction {
            saveMap()
        }

        val bottomBar = HBox(saveButton)
        bottomBar.maxHeight = 25.0
        bottomBar.alignment = Pos.BOTTOM_CENTER
        StackPane.setAlignment(bottomBar, Pos.BOTTOM_CENTER)
        bottomBar.style = "-fx-padding: 10px; -fx-background-color: #3c3f41;"
        return bottomBar
    }

    private fun saveMap() {
        // Save value of each button in a 2D array
        val map = Array(30) { Array(30) { 0 } }
        for (row in 0 until 30) {
            for (column in 0 until 30) {
                map[row][column] = buttons[row][column]?.text?.toInt() ?: 0
            }
        }
        // Save to json format
        val json = JSONArray(map)
        val file = File("map.old.json").writeText(json.toString())
    }
}
