package views

import ChatGPT.TextDavinci
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class ChatGPT private constructor() : BorderPane() {

    private enum class MessageAuthor {
        Davinci, User
    }

    private class varianteColor(private var color1: String, private var color2: String) {
        var isOne = true
        fun getColor(): String {
            isOne = !isOne
            return if (isOne) {
                color1
            } else {
                color2
            }
        }
    }

    companion object {
        var instance: ChatGPT = ChatGPT()
    }

    private val vColor = varianteColor("#2f3136", "#36393f")

    private val content: VBox = VBox()

    private val ConsoleZone: ScrollPane = ScrollPane().apply {
        isFitToWidth = true
        style = "-fx-background-color: #2C2F33;"
        content = this@ChatGPT.content.apply {
            style = "-fx-background-color: #2C2F33;"
            minHeight = 475.0
        }
    }

    private val AreaToTape: TextField = TextField().apply {
        prefWidth = 500.0
        prefHeight = 50.0
        promptText = "Tape here"
        this.style =
            "-fx-background-color: #2C2F33; -fx-text-fill: #FFFFFF; -fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 0px 0px 0px 0px;"

        onAction = EventHandler {
            if (this.text.isNotEmpty()) {
                addMessage(this.text)
                addMessage(TextDavinci.getCompletion(this.text), MessageAuthor.Davinci)
                this.text = ""
            }
        }
    }

    init {
        style = "-fx-background-color: RED;"
        this.prefHeight = 550.0
        this.prefWidth = 500.0

        this.center = ConsoleZone
        this.bottom = AreaToTape

    }


    private fun addMessage(message: String, messageAuthor: MessageAuthor = MessageAuthor.User) {
        val c = vColor.getColor()
        content.children.add(HBox().apply {
            style = "-fx-background-color: ${c};"
            children.add(ImageView().apply {
                fitHeight = 50.0
                fitWidth = 50.0
                style = "-fx-background-color: #2C2F33;"
                image = if (messageAuthor == MessageAuthor.Davinci) {
                    Image("icons8_clown_fish_96px.png")
                } else {
                    Image("icons8_user_96px.png")
                }
            })
            children.add(VBox().apply {
                // Add button to copy in clipboard the message
                HBox.setHgrow(this, javafx.scene.layout.Priority.ALWAYS)
                children.add(HBox().apply {
                    HBox.setHgrow(this, javafx.scene.layout.Priority.ALWAYS)
                    alignment = javafx.geometry.Pos.TOP_RIGHT
                    children.add(Button("Copy").apply {
                        style = "-fx-background-color: ${c}; -fx-text-fill: #AFAFAF; -fx-font-size: 10px; -fx-font-weight: bold; -fx-background-radius: 0px 0px 0px 0px;"
                        onAction = EventHandler {
                            val stringSelection = StringSelection(message)
                            Toolkit.getDefaultToolkit().systemClipboard.setContents(stringSelection, null)
                        }
                    })
                })
                children.add(Label().apply {
                    style = "-fx-text-fill: #FFFFFF; -fx-font-size: 15px; -fx-font-weight: bold;"
                    text = message
                    // Return to the line
                    isWrapText = true
                    // Can select the text
                    isFocusTraversable = true
                })
            })
        })
        this.ConsoleZone.vvalue = 1.0
    }
}
