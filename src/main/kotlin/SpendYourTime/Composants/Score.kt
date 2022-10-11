package SpendYourTime.Composants

import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

class Score(name: String) : VBox() {
    companion object {
        const val STYLE_TEXT = "-fx-text-fill: White; -fx-font-size: 10px;"
    }

    private val coin = Label(name)
    private val price = Label("0")
    private val block = Label("0")

    init {
        alignment = Pos.TOP_LEFT
        children.add(Label(name).apply {
            style = STYLE_TEXT
        })
        children.add(HBox().apply {
            alignment = Pos.CENTER
            children.add(coin.apply {
                style = STYLE_TEXT
            })
            children.add(ImageView(Image(WindowsAfk.Companion::class.java.getResourceAsStream("/assets/coin.png"))).apply {
                fitWidth = 10.0
                fitHeight = 10.0
            })
        })
        children.add(HBox().apply {
            alignment = Pos.CENTER
            children.add(price.apply {
                style = STYLE_TEXT
            })
            children.add(ImageView(Image(WindowsAfk.Companion::class.java.getResourceAsStream("/assets/price.png"))).apply {
                fitWidth = 10.0
                fitHeight = 10.0
            })
        })
        children.add(HBox().apply {
            alignment = Pos.CENTER
            children.add(block.apply {
                style = STYLE_TEXT
            })
            children.add(ImageView(Image(WindowsAfk.Companion::class.java.getResourceAsStream("/assets/block.png"))).apply {
                fitWidth = 10.0
                fitHeight = 10.0
            })
        })
    }

    fun setCoin(coin: Int) {
        Platform.runLater {
            this.coin.text = coin.toString()
        }
    }

    fun setPrice(price: Int) {
        Platform.runLater {
            this.price.text = price.toString()
        }
    }

    fun setBlock(block: Int) {
        Platform.runLater {
            this.block.text = block.toString()
        }
    }

}
