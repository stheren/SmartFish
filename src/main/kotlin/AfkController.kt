import Composants.CircleOutlineButton
import Composants.SideMenuButton
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import views.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AfkController {

    companion object {
        lateinit var instance: AfkController
    }

    @FXML
    lateinit var root: BorderPane
    lateinit var AreaToTape: TextField

    lateinit var btnGroup: HBox
    //    lateinit var ConsoleZone: TextArea
    lateinit var btnClose: CircleOutlineButton
    lateinit var btnOnTop: CircleOutlineButton
    lateinit var btnCollapse: CircleOutlineButton
    lateinit var btnExtend: CircleOutlineButton
    lateinit var UpBarre: HBox

    lateinit var content: VBox

    lateinit var SideHome: SideMenuButton
    lateinit var SideConsole: SideMenuButton
    lateinit var SidePlace: SideMenuButton
    lateinit var SideSyp: SideMenuButton
    lateinit var SideCreator: SideMenuButton

    var keyBoarding: KeyBoarding? = null


    fun initialize() {

        // Init SpendYourTime
        SpendYourTime.instance

        instance = this
        content.children.add(Home.instance)

        btnClose.onAction = EventHandler { Platform.exit() }

        btnOnTop.onAction = EventHandler {
            WindowsAfk.pStage.isAlwaysOnTop = !WindowsAfk.pStage.isAlwaysOnTop
            btnOnTop.filled = WindowsAfk.pStage.isAlwaysOnTop
        }

        btnCollapse.onAction = EventHandler { WindowsAfk.pStage.isIconified = true }

        btnExtend.onAction = EventHandler {
            WindowsAfk.extend()
            btnExtend.filled = WindowsAfk.isExtend
        }

        if(WindowsAfk.creator) {
            val label = javafx.scene.control.Label("Creator Mode")
            HBox.setMargin(label, javafx.geometry.Insets(0.0, 0.0, 0.0, 10.0))
            label.style = "-fx-text-fill: -fx-discord-red; -fx-font-size: 15px; -fx-font-weight: bold;"
            btnGroup.children.add(0, label)
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

        SideSyp.setOnAction {
            Platform.runLater {
                content.children.clear()
                content.children.add(SpendYourTime.instance)
            }
        }

        if (WindowsAfk.creator) {
            SideCreator.setOnAction {
                Platform.runLater {
                    content.children.clear()
                    content.children.add(MapCreator.instance)
                }
            }
        }else{
            SideCreator.isVisible = false
        }

        if(!WindowsAfk.creator) {
            root.onMouseClicked = EventHandler {
                if (keyBoarding == null) {
                    keyBoarding = KeyBoarding(this)
                }
            }
        }
    }

    fun log(s: String) {
        Platform.runLater {
            Console.instance.addText("\n[${LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))}] $s")
        }
    }

}
