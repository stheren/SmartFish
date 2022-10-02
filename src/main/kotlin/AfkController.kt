import Composants.SideMenuButton
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import views.Console
import views.Home
import views.SmartPlace
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AfkController {

    companion object {
        lateinit var instance: AfkController
    }

    @FXML
    lateinit var root: BorderPane
    lateinit var AreaToTape: TextField

    //    lateinit var ConsoleZone: TextArea
    lateinit var btnClose: Button
    lateinit var btnOnTop: Button
    lateinit var btnCollapse: Button
    lateinit var UpBarre: HBox

    lateinit var content: VBox

    lateinit var SideHome: SideMenuButton
    lateinit var SideConsole: SideMenuButton
    lateinit var SidePlace: SideMenuButton

    var isOpen = true

    var keyBoarding: KeyBoarding? = null


    fun initialize() {

        instance = this
        content.children.add(Home.instance)

        btnClose.onAction = EventHandler { Platform.exit() }
        btnClose.onMouseEntered = EventHandler {
            btnClose.style =
                "-fx-background-color: -fx-discord-red; -fx-border-color: -fx-discord-red; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }
        btnClose.onMouseExited = EventHandler {
            btnClose.style =
                "-fx-background-color: TRANSPARENT; -fx-border-color: -fx-discord-red; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }

        fun makeStyleOfBtnOnTop(b: Boolean) {
            if (WindowsAfk.pStage.isAlwaysOnTop || b) {
                btnOnTop.style =
                    "-fx-background-color: -fx-discord-orange; -fx-border-color: -fx-discord-orange; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
            } else {
                btnOnTop.style =
                    "-fx-background-color: TRANSPARENT; -fx-border-color: -fx-discord-orange; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
            }
        }

        btnOnTop.onAction = EventHandler {
            WindowsAfk.pStage.isAlwaysOnTop = !WindowsAfk.pStage.isAlwaysOnTop
            makeStyleOfBtnOnTop(false)
        }
        btnOnTop.onMouseEntered = EventHandler {
            makeStyleOfBtnOnTop(true)
        }
        btnOnTop.onMouseExited = EventHandler {
            makeStyleOfBtnOnTop(false)
        }

        btnCollapse.onAction = EventHandler { WindowsAfk.pStage.isIconified = true }
        btnCollapse.onMouseEntered = EventHandler {
            btnCollapse.style =
                "-fx-background-color: -fx-discord-green; -fx-border-color: -fx-discord-green; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }
        btnCollapse.onMouseExited = EventHandler {
            btnCollapse.style =
                "-fx-background-color: TRANSPARENT; -fx-border-color: -fx-discord-green; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
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

        SidePlace.setOnAction {
            Platform.runLater {
                content.children.clear()
                content.children.add(SmartPlace.instance)
            }
        }

        SideHome.setOnAction {
            Platform.runLater {
                content.children.clear()
                content.children.add(Home.instance)
            }
        }

        SideConsole.setOnAction {
            Platform.runLater {
                content.children.clear()
                content.children.add(Console.instance)
            }
        }

        root.onMouseClicked = EventHandler {
            if (keyBoarding == null) {
                keyBoarding = KeyBoarding(this)
            }
        }
    }

    fun log(s: String) {
        Platform.runLater {
            Console.instance.addText("\n[${LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))}] $s")
        }
    }

}
