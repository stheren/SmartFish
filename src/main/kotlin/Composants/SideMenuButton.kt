package Composants


import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import kotlin.properties.Delegates

class SideMenuButton : Group()
{
    companion object
    {
        private val other: ArrayList<SideMenuButton> = ArrayList()
    }

    val BACKGROUND_COLOR_SET = "-fx-background-color: #"

    lateinit var texte: String
    lateinit var icon: String

    var active by Delegates.notNull<Boolean>()

    init
    {
        other.add(this)

        Platform.runLater {
            children.add(HBox().apply {
                style     = BACKGROUND_COLOR_SET + if (active) "2d2f30" else "3c3f41"
                rotate    = -90.0
                alignment = Pos.CENTER
                spacing   = 5.0
                padding   = Insets(0.0, 20.0, 0.0, 20.0)
                children.add(ImageView().apply {
                    fitWidth        = 20.0
                    isPreserveRatio = true
                    image           = Image(icon)
                })
                children.add(Label().apply {
                    textFill = Color.web("#afb1b3")
                    text     = this@SideMenuButton.texte
                })
            })
        }

        onMouseEntered = EventHandler {
            (children[0] as HBox).style = BACKGROUND_COLOR_SET + if (active) "2d2f30" else "353739"
        }

        onMouseExited = EventHandler {
            (children[0] as HBox).style = BACKGROUND_COLOR_SET + if (active) "2d2f30" else "3c3f41"
        }

        setOnAction { println("NEED TO BE OVERRIDE") }
    }

    fun setOnAction(action: () -> Unit)
    {
        onMouseClicked = EventHandler {

            other.forEach {
                it.active = false
                (it.children[0] as HBox).style = it.BACKGROUND_COLOR_SET + "3c3f41"
            }

            active = true

            (children[0] as HBox).style = BACKGROUND_COLOR_SET + "2d2f30"
            action()
        }
    }
}

