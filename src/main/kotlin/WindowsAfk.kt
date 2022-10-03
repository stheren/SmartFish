import Place.Connexion
import javafx.application.Application
import javafx.application.HostServices
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
        lateinit var hostServices: HostServices
        lateinit var instance: WindowsAfk
        lateinit var controller: AfkController

        @JvmStatic
        fun main(args: Array<String>) {
            launch(WindowsAfk::class.java)
        }
    }

    override fun start(stage: Stage) {
        instance = this

        val fxmlLoader = FXMLLoader(javaClass.getResource("/appTemplate.fxml"))
        val root = fxmlLoader.load<Any>() as BorderPane

        stage.initStyle(StageStyle.DECORATED)
        stage.isAlwaysOnTop = false

        val scene = Scene(root, 510.0, 553.0)
        scene.fill = Color.TRANSPARENT
        stage.scene = scene

        controller = fxmlLoader.getController()

        stage.icons.add(Image(javaClass.getResourceAsStream("/icons8_clown_fish_96px.png")))
        stage.title = "Smart Keyboard"
        stage.show()


        WindowsAfk.hostServices = this.hostServices
        pStage = stage
    }

    override fun stop() {
        Connexion.instance.close()
        controller.keyBoarding?.stop()
        controller.isOpen = false
        super.stop()
    }
}
