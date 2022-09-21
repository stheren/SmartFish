import Composants.ButtonColor
import Place.Connexion
import Place.models.Color
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ProgressBar
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox


class AfkController {

    companion object {
        var adminMode: Boolean = false
        lateinit var instance: AfkController
    }

    @FXML
    lateinit var AreaToTape: TextField
//    lateinit var ConsoleZone: TextArea
    lateinit var btnClose: Button
    lateinit var btnOnTop: Button
    lateinit var btnCollapse: Button
    lateinit var UpBarre: HBox
    lateinit var SmartPlace: Canvas

    lateinit var selecteColor: Color
    lateinit var colorList : HBox

    lateinit var progress: ProgressBar

    var isOpen = true

    val keyBoarding = KeyBoarding(this)


    fun initialize() {

        instance = this
        Connexion.initInstance()

        selecteColor = Color()
        selecteColor.red = 255
        selecteColor.green = 255
        selecteColor.blue = 255

        progress.progress = 1.0
        progress.style = "-fx-accent: GREEN;"

        SmartPlace.scaleX -= 0.1
        SmartPlace.scaleY -= 0.1

//        ConsoleZone.font = Font.font("Monospace", 6.0)

        btnClose.onAction = EventHandler { Platform.exit() }
        btnClose.onMouseEntered = EventHandler {
            btnClose.style =
                "-fx-background-color: RED; -fx-border-color: Red; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }
        btnClose.onMouseExited = EventHandler {
            btnClose.style =
                "-fx-background-color: TRANSPARENT; -fx-border-color: Red; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }

        btnOnTop.onAction = EventHandler { WindowsAfk.pStage.isAlwaysOnTop = !WindowsAfk.pStage.isAlwaysOnTop }
        btnOnTop.onMouseEntered = EventHandler {
            btnOnTop.style =
                "-fx-background-color: ORANGE; -fx-border-color: ORANGE; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }
        btnOnTop.onMouseExited = EventHandler {
            btnOnTop.style =
                "-fx-background-color: TRANSPARENT; -fx-border-color: ORANGE; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }

        btnCollapse.onAction = EventHandler { WindowsAfk.pStage.isIconified = true }
        btnCollapse.onMouseEntered = EventHandler {
            btnCollapse.style =
                "-fx-background-color: GREEN; -fx-border-color: GREEN; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }
        btnCollapse.onMouseExited = EventHandler {
            btnCollapse.style =
                "-fx-background-color: TRANSPARENT; -fx-border-color: GREEN; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;"
        }

        var xOffset = 0.0
        var yOffset = 0.0

        var xOffsetSmartPlace = 0.0
        var yOffsetSmartPlace = 0.0

        UpBarre.onMousePressed = EventHandler { event ->
            xOffset = WindowsAfk.pStage.x - event.screenX
            yOffset = WindowsAfk.pStage.y - event.screenY
        }

        UpBarre.onMouseDragged = EventHandler { event ->
            WindowsAfk.pStage.x = event.screenX + xOffset
            WindowsAfk.pStage.y = event.screenY + yOffset
        }


        SmartPlace.onScroll = EventHandler { event ->
            if (event.deltaY > 0) {
                SmartPlace.scaleX += if(SmartPlace.scaleX < 10.0) 0.1 else 0.0
                SmartPlace.scaleY += if(SmartPlace.scaleY < 10.0) 0.1 else 0.0
            } else {
                SmartPlace.scaleX -= if(SmartPlace.scaleX > 0.5) 0.1 else 0.0
                SmartPlace.scaleY -= if(SmartPlace.scaleY > 0.5) 0.1 else 0.0
            }
        }

        val waitBeetweenPixel = 5 // in second


        SmartPlace.onMousePressed = EventHandler { event ->
            if (event.isPrimaryButtonDown) {
                if (progress.progress == 1.0) {
                    val x: Int = (event.x / 5).toInt()
                    val y: Int = (event.y / 5).toInt()
                    Connexion.instance.request(x, y, selecteColor.red, selecteColor.green, selecteColor.blue)
                    if (!adminMode) {
                        Thread {
                            progress.style += "-fx-accent: DARKRED;"
                            for (t in 0..(waitBeetweenPixel * 100)) {
                                Thread.sleep(10)
                                Platform.runLater {
                                    progress.progress = t.toDouble() / (waitBeetweenPixel * 100)
                                    if (t == 250) {
                                        progress.style += "-fx-accent: RED;"
                                    } else if (t == 500) {
                                        progress.style += "-fx-accent: ORANGE;"
                                    } else if (t == 750) {
                                        progress.style += "-fx-accent: LIGHTGREEN;"
                                    }

                                }
                            }
                            Platform.runLater {
                                progress.progress = 1.0
                                progress.style += "-fx-accent: GREEN;"
                            }
                        }.start()
                    }
                } else {
                    Platform.runLater {
                        val alert = Alert(Alert.AlertType.WARNING)
                        alert.title = "Trop de pixel !!"
                        alert.contentText =
                            "Vous ne pouvez pas placer plus de pixel pour le moment, attendez un peu ! ($waitBeetweenPixel secondes)"
                        alert.showAndWait()
                    }
                }
            } else if (event.isSecondaryButtonDown) {
                xOffsetSmartPlace = SmartPlace.translateX - event.screenX
                yOffsetSmartPlace = SmartPlace.translateY - event.screenY
            }
        }

        SmartPlace.onMouseDragged = EventHandler { event ->
            if (event.isSecondaryButtonDown) {
                SmartPlace.translateX = event.screenX + xOffsetSmartPlace
                SmartPlace.translateY = event.screenY + yOffsetSmartPlace
            }else if(event.isPrimaryButtonDown && adminMode){
                val x: Int = (event.x / 5).toInt()
                val y: Int = (event.y / 5).toInt()
                Connexion.instance.request(x, y, selecteColor.red, selecteColor.green, selecteColor.blue)
            }
        }

        AreaToTape.onKeyPressed = EventHandler {
            if (it.code == KeyCode.M && it.isControlDown && it.isShiftDown) {
                if (adminMode) {
                    adminMode = false
                    AreaToTape.style = "-fx-background-color: TRANSPARENT;"
                } else {
                    adminMode = true
                    AreaToTape.style = "-fx-background-color: #FF0000;"
                }
            }
        }
    }

    fun onColorClicked(btn : ButtonColor) {
        for(i in colorList.children){
            if(i is ButtonColor){
                i.resetBackground()
            }
        }
        // Convert color hex to rgb
        selecteColor.red = btn.rgbColor.red
        selecteColor.green = btn.rgbColor.green
        selecteColor.blue = btn.rgbColor.blue
    }

    fun log(s: String) {
//        Platform.runLater {
//            ConsoleZone.text += "\n[${LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))}] $s"
//            ConsoleZone.scrollTop = Double.MAX_VALUE
//        }
    }

    fun drawPlace() {
        //Platform.runLater { SmartPlace.graphicsContext2D.clearRect(0.0, 0.0, 500.0, 500.0) }
        for (i in 0..99) {
            for (j in 0..99) {
                val newColor = Connexion.instance.place?.find(i, j)?.color?.toPaint()
                Platform.runLater {
                    SmartPlace.graphicsContext2D.fill = newColor
                    SmartPlace.graphicsContext2D.fillRect(i * 5.0, j * 5.0, 5.0, 5.0)
                }
            }
        }
    }

    fun updatePlace() {
        for (p in Connexion.instance.place?.updated!!) {
            Platform.runLater {
                SmartPlace.graphicsContext2D.fill = p.color.toPaint()
                SmartPlace.graphicsContext2D.fillRect(p.x * 5.0, p.y * 5.0, 5.0, 5.0)
            }
        }
    }
}
