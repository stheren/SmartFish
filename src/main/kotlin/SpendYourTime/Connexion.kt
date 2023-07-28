package SpendYourTime

import SpendYourTime.models.Map_Syp
import SpendYourTime.models.Player
import SpendYourTime.models.Point
import WindowsAfk
import com.fasterxml.jackson.databind.ObjectMapper
import io.socket.client.IO
import io.socket.client.Socket
import views.SpendYourTime
import java.net.URI


class Connexion {
    private var uri: URI = if (WindowsAfk.address != null && WindowsAfk.port != null) {
        URI.create("ws://${WindowsAfk.address}:${WindowsAfk.port}")
    } else {
        URI.create("ws://spend.calenpart.com")
    }
    private val options: IO.Options = IO.Options.builder().build()
    private var socket: Socket = IO.socket(uri, options)
    private val mapper = ObjectMapper()

    private var onMapLoad: () -> Unit = {}

    init {
        socket.on("data") {
            val map = mapper.readValue(it[0].toString(), Map_Syp::class.java)
            SpendYourTime.instance.Map_Syp.loadMap(map)
            onMapLoad()
        }

        socket.on("player") {
            val player = mapper.readValue(it[0].toString(), Player::class.java)
            SpendYourTime.instance.players.updatePlayer(player)
        }

        socket.on("me") {
            val player = mapper.readValue(it[0].toString(), Player::class.java)
            SpendYourTime.instance.player = player
        }

        socket.on("error") {
            println(it[0])
        }
    }

    fun setOnLoadMap(func: () -> Unit) {
        onMapLoad = func
    }

    fun start() {
        socket.connect()
    }

    fun change(player: Player) {
        socket.emit(
            "change", mapper.writeValueAsString(
                mapOf(
                    "uuid" to player.uuid,
                    "name" to player.name,
                    "pos" to player.pos,
                    "skin" to player.skin,
                    "direction" to player.direction,
                    "animationState" to player.animationState,
                )
            )
        )
    }

    fun close() {
        socket.disconnect()
    }

    fun join(UUID: String, spawnPoint: Point) {
        socket.emit(
            "join", mapper.writeValueAsString(
                mapOf(
                    "uuid" to UUID,
                    "spawn" to spawnPoint
                )
            )
        )
    }
}
