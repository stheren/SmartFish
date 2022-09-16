import Place.Connexion
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle

class WindowsAfk : Application() {
    companion object {
        lateinit var pStage: Stage

        @JvmStatic
        fun main(args: Array<String>) {
            launch(WindowsAfk::class.java)
        }
    }

    lateinit var controller: AfkController

    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/appTemplate.fxml"))
        val root = fxmlLoader.load<Any>() as BorderPane

        stage.initStyle(StageStyle.TRANSPARENT)
        stage.isAlwaysOnTop = false

        val scene = Scene(root)
        scene.fill = Color.TRANSPARENT
        stage.scene = scene

        controller = fxmlLoader.getController()

        stage.icons.add(Image(javaClass.getResourceAsStream("/icons8_clown_fish_96px.png")))
        stage.title = "Smart Keyboard"
        stage.show()

        pStage = stage
    }

    override fun stop() {
        Connexion.instance.close()
        controller.isOpen = false
        super.stop()
    }
}
