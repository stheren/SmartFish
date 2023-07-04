package Place

import Place.models.Pixel
import com.fasterxml.jackson.databind.ObjectMapper
import io.socket.client.IO
import io.socket.client.Socket
import javafx.application.Platform
import javafx.scene.control.Alert
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import views.SmartPlace
import java.net.URI


class Connexion private constructor(){
    companion object {
        val instance: Connexion = Connexion()
        fun initInstance(smartPlace: SmartPlace) {
            instance.smartPlace = smartPlace
        }
    }

    var place: Place.models.Place? = null
    var smartPlace: SmartPlace? = null

    private var uri: URI = if(WindowsAfk.address != null) {
        URI.create("ws://${WindowsAfk.address}:2345")
    }else{
        URI.create("ws://calenpart.com:2345")
    }
    private val options: IO.Options = IO.Options.builder().build()
    private var socket: Socket = IO.socket(uri, options)
    private val mapper = ObjectMapper()

    init {
        socket.on("response") {
            if(it[0] is String && it[0] != "OK") {
                Platform.runLater {
                    val alert = Alert(Alert.AlertType.WARNING)
                    alert.title = "Smart Place Error !"
                    alert.contentText = it[0] as String
                    alert.buttonTypes.add(ButtonType("FUCK THIS ALERT", ButtonBar.ButtonData.FINISH))
                    val result = alert.showAndWait()
                    if (result.get().buttonData == ButtonBar.ButtonData.FINISH) {
                        alert.close()
                    }
                }
            }
        }

        socket.on("data") {
            place = mapper.readValue(it[0].toString(), Place.models.Place::class.java)
            smartPlace?.drawPlace() ?: return@on
        }

        socket.on("update") {
            place?.updated = mapper.readValue(it[0].toString(), Array<Pixel>::class.java)
            smartPlace?.updatePlace() ?: return@on
        }

        socket.connect()
    }

    fun request(x: Int, y: Int, r: Int, g: Int, b: Int) {
        socket.emit("request", "$x $y $r $g $b")
    }

    fun close() {
        socket.disconnect()
    }
}
