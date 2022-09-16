import Place.Connexion
import Place.models.Color
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox
import javafx.scene.text.Font
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AfkController {

    companion object {
        var adminMode: Boolean = false

        const val BORDER_BACKGROUND_0 = "-fx-border-radius: 0; -fx-background-radius: 0;"
        const val BORDER_BACKGROUND_100 = "-fx-border-radius: 100; -fx-background-radius: 100;"
    }

    @FXML
    lateinit var AreaToTape: TextField
    lateinit var ConsoleZone: TextArea
    lateinit var btnClose: Button
    lateinit var btnOnTop: Button
    lateinit var btnCollapse: Button
    lateinit var UpBarre: HBox
    lateinit var SmartPlace: Canvas

    lateinit var btnWhite: Button
    lateinit var btnLightGrey: Button
    lateinit var btnGrey: Button
    lateinit var btnBlack: Button
    lateinit var btnPink: Button
    lateinit var btnRed: Button
    lateinit var btnOrange: Button
    lateinit var btnBrown: Button
    lateinit var btnYellow: Button
    lateinit var btnGreen: Button
    lateinit var btnLightGreen: Button
    lateinit var btnCyan: Button
    lateinit var btnBlue: Button
    lateinit var btnDarkBlue: Button
    lateinit var btnPurple: Button
    lateinit var btnMagenta: Button

    lateinit var selecteColor: Color

    lateinit var progress: ProgressBar

    var isOpen = true


    fun initialize() {
        selecteColor = Color()
        selecteColor.red = 255
        selecteColor.green = 255
        selecteColor.blue = 255

        progress.progress = 1.0
        progress.style = "-fx-accent: GREEN;"

        SmartPlace.scaleX -= 0.1
        SmartPlace.scaleY -= 0.1

        Connexion.afkController = this
        ConsoleZone.font = Font.font("Monospace", 6.0)

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
                SmartPlace.scaleX += 0.1
                SmartPlace.scaleY += 0.1
            } else {
                SmartPlace.scaleX -= 0.1
                SmartPlace.scaleY -= 0.1
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
                            progress.style = "-fx-accent: DARKRED;"
                            for (t in 0..(waitBeetweenPixel * 100)) {
                                Thread.sleep(10)
                                Platform.runLater {
                                    progress.progress = t.toDouble() / (waitBeetweenPixel * 100)
                                    if (t == 250) {
                                        progress.style = "-fx-accent: RED;"
                                    } else if (t == 500) {
                                        progress.style = "-fx-accent: ORANGE;"
                                    } else if (t == 750) {
                                        progress.style = "-fx-accent: LIGHTGREEN;"
                                    }

                                }
                            }
                            Platform.runLater {
                                progress.progress = 1.0
                                progress.style = "-fx-accent: GREEN;"
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
            }
        }

        btnWhite.onAction = EventHandler {
            selecteColor.red = 255
            selecteColor.green = 255
            selecteColor.blue = 255
            raduisAllBtn()
            btnWhite.style += BORDER_BACKGROUND_0
        }

        btnLightGrey.onAction = EventHandler {
            selecteColor.red = 228
            selecteColor.green = 228
            selecteColor.blue = 228
            raduisAllBtn()
            btnLightGrey.style += BORDER_BACKGROUND_0

        }

        btnGrey.onAction = EventHandler {
            selecteColor.red = 136
            selecteColor.green = 136
            selecteColor.blue = 136
            raduisAllBtn()
            btnGrey.style += BORDER_BACKGROUND_0
        }

        btnBlack.onAction = EventHandler {
            selecteColor.red = 34
            selecteColor.green = 34
            selecteColor.blue = 34
            raduisAllBtn()
            btnBlack.style += BORDER_BACKGROUND_0
        }

        btnPink.onAction = EventHandler {
            selecteColor.red = 255
            selecteColor.green = 167
            selecteColor.blue = 209
            raduisAllBtn()
            btnPink.style += BORDER_BACKGROUND_0
        }

        btnRed.onAction = EventHandler {
            selecteColor.red = 229
            selecteColor.green = 0
            selecteColor.blue = 0
            raduisAllBtn()
            btnRed.style += BORDER_BACKGROUND_0
        }

        btnOrange.onAction = EventHandler {
            selecteColor.red = 229
            selecteColor.green = 149
            selecteColor.blue = 0
            raduisAllBtn()
            btnOrange.style += BORDER_BACKGROUND_0
        }

        btnBrown.onAction = EventHandler {
            selecteColor.red = 160
            selecteColor.green = 106
            selecteColor.blue = 66
            raduisAllBtn()
            btnBrown.style += BORDER_BACKGROUND_0
        }

        btnYellow.onAction = EventHandler {
            selecteColor.red = 229
            selecteColor.green = 217
            selecteColor.blue = 0
            raduisAllBtn()
            btnYellow.style += BORDER_BACKGROUND_0
        }

        btnGreen.onAction = EventHandler {
            selecteColor.red = 94
            selecteColor.green = 224
            selecteColor.blue = 68
            raduisAllBtn()
            btnGreen.style += BORDER_BACKGROUND_0
        }

        btnLightGreen.onAction = EventHandler {
            selecteColor.red = 2
            selecteColor.green = 190
            selecteColor.blue = 1
            raduisAllBtn()
            btnLightGreen.style += BORDER_BACKGROUND_0
        }

        btnCyan.onAction = EventHandler {
            selecteColor.red = 0
            selecteColor.green = 211
            selecteColor.blue = 221
            raduisAllBtn()
            btnCyan.style += BORDER_BACKGROUND_0
        }

        btnBlue.onAction = EventHandler {
            selecteColor.red = 0
            selecteColor.green = 131
            selecteColor.blue = 199
            raduisAllBtn()
            btnBlue.style += BORDER_BACKGROUND_0
        }

        btnDarkBlue.onAction = EventHandler {
            selecteColor.red = 0
            selecteColor.green = 0
            selecteColor.blue = 234
            raduisAllBtn()
            btnDarkBlue.style += BORDER_BACKGROUND_0
        }

        btnPurple.onAction = EventHandler {
            selecteColor.red = 207
            selecteColor.green = 110
            selecteColor.blue = 228
            raduisAllBtn()
            btnPurple.style += BORDER_BACKGROUND_0
        }

        btnMagenta.onAction = EventHandler {
            selecteColor.red = 130
            selecteColor.green = 0
            selecteColor.blue = 128
            raduisAllBtn()
            btnMagenta.style += BORDER_BACKGROUND_0
        }

        Thread(KeyBoarding(this)).start()


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

    fun raduisAllBtn() {
        btnWhite.style += BORDER_BACKGROUND_100
        btnLightGrey.style += BORDER_BACKGROUND_100
        btnGrey.style += BORDER_BACKGROUND_100
        btnBlack.style += BORDER_BACKGROUND_100
        btnPink.style += BORDER_BACKGROUND_100
        btnRed.style += BORDER_BACKGROUND_100
        btnOrange.style += BORDER_BACKGROUND_100
        btnBrown.style += BORDER_BACKGROUND_100
        btnYellow.style += BORDER_BACKGROUND_100
        btnGreen.style += BORDER_BACKGROUND_100
        btnLightGreen.style += BORDER_BACKGROUND_100
        btnCyan.style += BORDER_BACKGROUND_100
        btnBlue.style += BORDER_BACKGROUND_100
        btnDarkBlue.style += BORDER_BACKGROUND_100
        btnPurple.style += BORDER_BACKGROUND_100
        btnMagenta.style += BORDER_BACKGROUND_100
    }

    fun log(s: String) {
        Platform.runLater {
            ConsoleZone.text += "\n[${LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))}] $s"
            ConsoleZone.scrollTop = Double.MAX_VALUE
        }
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
