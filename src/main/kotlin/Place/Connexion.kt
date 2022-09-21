package Place

import com.fasterxml.jackson.databind.ObjectMapper
import io.socket.client.IO
import io.socket.client.Socket
import javafx.application.Platform
import javafx.scene.control.Alert
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

//    private val uri: URI = URI.create("ws://192.168.1.54:2345")
//    private val uri: URI = URI.create("ws://localhost:2345")
    private val uri: URI = URI.create("ws://calenpart.com:2345")
    private val options: IO.Options = IO.Options.builder().build()
    private val socket: Socket = IO.socket(uri, options)
    private val mapper = ObjectMapper()

    init {
        socket.on("response") {
            if (it[0] is String && it[0] != "OK") {
                Platform.runLater {
                    val alert = Alert(Alert.AlertType.WARNING)
                    alert.title = "Erreur"
                    alert.contentText = "Invalid request"
                    alert.showAndWait()
                }
            }
        }
        socket.on("data") {
            place = mapper.readValue(it[0].toString(), Place.models.Place::class.java)
            smartPlace?.drawPlace() ?: return@on
        }

        socket.on("update") {
            place = mapper.readValue(it[0].toString(), Place.models.Place::class.java)
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
