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
        var address : String? = null
        var port : Int? = null
        var creator = false
        var isExtend = false

        @JvmStatic
        fun main(args: Array<String>) {
            if (args.isNotEmpty()) {
                var i = 0
                while (i < args.size) {
                    when (args[i]) {
                        "-a" -> {
                            address = args[i + 1]
                            i++
                        }
                        "-p" -> {
                            port = args[i + 1].toInt()
                            i++
                        }
                        "--creator" -> creator = true
                        "--extend" -> isExtend = true
                    }
                    i++
                }
            }
            launch(WindowsAfk::class.java)
        }

        fun extend() {
            isExtend = !isExtend
            pStage.height = if (isExtend) 900.0 else 550.0
            pStage.width = if (isExtend) 1440.0 else 550.0
        }
    }

    lateinit var controller: AfkController

    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/appTemplate.fxml"))
        val root = fxmlLoader.load<Any>() as BorderPane

        stage.initStyle(StageStyle.UNDECORATED)
        stage.isAlwaysOnTop = false

        val scene = when {
            isExtend -> Scene(root, 1440.0, 900.0)
            else -> Scene(root, 550.0, 550.0)
        }
        scene.fill = Color.TRANSPARENT
        stage.scene = scene

        controller = fxmlLoader.getController()

        stage.icons.add(Image(javaClass.getResourceAsStream("/icons8_clown_fish_96px.png")))
        stage.title = "Smart Fish"
        stage.show()

        WindowsAfk.hostServices = this.hostServices
        pStage = stage
    }

    override fun stop() {
        Connexion.instance.close()
        controller.keyBoarding?.stop()
        super.stop()
    }
}
