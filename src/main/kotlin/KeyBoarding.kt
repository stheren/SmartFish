import javafx.application.Platform
import java.awt.Robot
import java.awt.event.KeyEvent
import kotlin.concurrent.timer


class KeyBoarding(val root: AfkController) : Robot() {

    private var time = 0
    private val timer = timer(period = 1000) {
        try {
            Platform.runLater { root.AreaToTape.textProperty().set("") }
            val timeText = makeTheZero((time / 60 / 60)) + ":" + makeTheZero((time / 60) % 60) + ":" + makeTheZero(time % 60)
            convertStringToKeyPress(timeText)
            time += 1
            root.log("iod : done. (n°$time)")
        } catch (e: Exception) {
            root.log("[Error Catched] " + e.message)
        }
    }

    fun stop(){
        timer.cancel()
    }

    private fun pressKeyOnBlocNote(keyCode: Int) {
        Platform.runLater { root.AreaToTape.requestFocus() }
        if (root.AreaToTape.isFocused) {
            keyPress(keyCode)
            keyRelease(keyCode)
        }
    }

    private fun makeTheZero(number: Int): String {
        return if (number < 10) "0$number" else number.toString()
    }

    private fun convertStringToKeyPress(chaine: String) {
        for (element: Char in chaine) {
            when (element) {
                ':' -> pressKeyOnBlocNote(KeyEvent.VK_COLON)
                '1' -> pressKeyOnBlocNote(KeyEvent.VK_NUMPAD1)
                '2' -> pressKeyOnBlocNote(KeyEvent.VK_NUMPAD2)
                '3' -> pressKeyOnBlocNote(KeyEvent.VK_NUMPAD3)
                '4' -> pressKeyOnBlocNote(KeyEvent.VK_NUMPAD4)
                '5' -> pressKeyOnBlocNote(KeyEvent.VK_NUMPAD5)
                '6' -> pressKeyOnBlocNote(KeyEvent.VK_NUMPAD6)
                '7' -> pressKeyOnBlocNote(KeyEvent.VK_NUMPAD7)
                '8' -> pressKeyOnBlocNote(KeyEvent.VK_NUMPAD8)
                '9' -> pressKeyOnBlocNote(KeyEvent.VK_NUMPAD9)
                '0' -> pressKeyOnBlocNote(KeyEvent.VK_NUMPAD0)
                else -> {
                    root.log("Error : $element")
                }
            }
        }
    }
}
