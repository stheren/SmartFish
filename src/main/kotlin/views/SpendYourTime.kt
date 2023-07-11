package views

import javafx.scene.layout.VBox

class SpendYourTime private constructor() : VBox() {
    companion object {
        lateinit var instance: SpendYourTime
    }
}
