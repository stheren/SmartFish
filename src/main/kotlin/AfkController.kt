import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Font
import javafx.scene.text.Text
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AfkController {

    @FXML
    lateinit var AreaToTape : TextField
    @FXML
    lateinit var ConsoleZone : TextArea


    @FXML
    private lateinit var Name:Text
    @FXML
    private lateinit var btnRed: Button
    @FXML
    private lateinit var btnOrange: Button
    @FXML
    private lateinit var btnGreen: Button
    @FXML
    private lateinit var UpBarre: AnchorPane

    @FXML
    private  lateinit var btnLaunch: Button
    @FXML
    private lateinit var btnNothing: Button

    var isOpen = true



    fun initialize() {
        ConsoleZone.font = Font.font("Monospace", 6.0)

        Name.textProperty().set(System.getProperty("user.name"))

        btnRed.onAction = EventHandler { Platform.exit() }
        btnRed.onMouseEntered = EventHandler {
            btnRed.style =
                "-fx-background-color: RED; -fx-border-color: Red; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }
        btnRed.onMouseExited = EventHandler {
            btnRed.style =
                "-fx-background-color: TRANSPARENT; -fx-border-color: Red; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }

        btnOrange.onAction = EventHandler { WindowsAfk.pStage.isAlwaysOnTop = !WindowsAfk.pStage.isAlwaysOnTop }
        btnOrange.onMouseEntered = EventHandler {
            btnOrange.style =
                "-fx-background-color: ORANGE; -fx-border-color: ORANGE; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }
        btnOrange.onMouseExited = EventHandler {
            btnOrange.style =
                "-fx-background-color: TRANSPARENT; -fx-border-color: ORANGE; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }

        btnGreen.onAction = EventHandler { WindowsAfk.pStage.isIconified = true }
        btnGreen.onMouseEntered = EventHandler {
            btnGreen.style =
                "-fx-background-color: GREEN; -fx-border-color: GREEN; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }
        btnGreen.onMouseExited = EventHandler {
            btnGreen.style =
                "-fx-background-color: TRANSPARENT; -fx-border-color: GREEN; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }

        var xOffset = 0.0
        var yOffset = 0.0

        UpBarre.onMousePressed = EventHandler { event ->
            xOffset = WindowsAfk.pStage.x - event.screenX
            yOffset = WindowsAfk.pStage.y - event.screenY
        }

        UpBarre.onMouseDragged = EventHandler { event ->
            WindowsAfk.pStage.x = event.screenX + xOffset
            WindowsAfk.pStage.y = event.screenY + yOffset
        }

        btnLaunch.onMouseEntered = EventHandler {
            btnLaunch.style =
                "-fx-border-color: #5CB85C; -fx-background-color: #5CB85C; -fx-border-radius: 10; -fx-background-radius: 10;"
        }
        btnLaunch.onMouseExited = EventHandler {
            btnLaunch.style =
                "-fx-border-color: #5CB85C; -fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;"
        }

        btnNothing.onMouseEntered = EventHandler {
            btnNothing.style =
                "-fx-border-color: #d9534f; -fx-background-color: #d9534f; -fx-border-radius: 10; -fx-background-radius: 10;"
        }
        btnNothing.onMouseExited = EventHandler {
            btnNothing.style =
                "-fx-border-color: #d9534f; -fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;"
        }

        Thread(KeyBoarding(this)).start()
    }

    fun log(s:String){
        Platform.runLater {
            ConsoleZone.text += "\n[${LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))}] $s"
            ConsoleZone.scrollTop = Double.MAX_VALUE
        }
    }
}
