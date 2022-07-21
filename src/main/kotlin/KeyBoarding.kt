import javafx.application.Platform
import java.awt.Robot
import java.awt.event.KeyEvent


class KeyBoarding(val root:AfkController) : Robot(), Runnable {

    override fun run() {
            Platform.runLater { root.AreaToTape.requestFocus() }
            root.log("Application Is start.")
            var time = 0
//            keyRelease(KeyEvent.VK_CAPS_LOCK)
            root.log("Iterations of display : start.")
            while (root.isOpen) {
                try {
                    val timeText = makeTheZero((time / 60 / 60)) + ":" + makeTheZero((time / 60) % 60) + ":" + makeTheZero(time % 60)
                    convertStringToKeyPress(timeText)
                    Thread.sleep(1000)
                    Platform.runLater { root.AreaToTape.textProperty().set("") }
                    time += 1
                    root.log("iod : done. (n°$time)")
                    if(time%60==0){
                        Platform.runLater { root.ConsoleZone.textProperty().set("") }
                    }
                } catch (e: Exception) {
                    root.log("[Error Catched] " + e.message)
                }
            }
            Platform.exit()
//            keyPress(KeyEvent.VK_CAPS_LOCK)
//            keyRelease(KeyEvent.VK_CAPS_LOCK)
    }

    private fun pressKeyOnBlocNote(KeyCode: Int) {
        if (root.AreaToTape.isFocused) {
            keyPress(KeyCode)
            keyRelease(KeyCode)
        }
    }

    private fun makeTheZero(number: Int): String {
        return if (number < 10) "0$number" else number.toString()
    }

    private fun convertStringToKeyPress(chaine: String) {
        for (element:Char in chaine) {
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
                }
            }
        }
    }
}
