package SpendYourTime

import SpendYourTime.Images.Skins
import SpendYourTime.models.Block
import SpendYourTime.models.Map
import SpendYourTime.models.Point
import SpendYourTime.models.Skin
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.socket.client.IO
import io.socket.client.Socket
import javafx.application.Platform
import org.json.JSONArray
import org.json.JSONObject
import views.SpendYourTime
import java.net.URI


class Connexion private constructor() {
    companion object {
        val instance: Connexion = Connexion()

        const val PORT = 5395
    }

    private var uri: URI = if (WindowsAfk.address != null) {
        URI.create("ws://${WindowsAfk.address}:$PORT")
    } else {
        URI.create("ws://calenpart.com:$PORT")
    }
    private val options: IO.Options = IO.Options.builder().build()
    private var socket: Socket = IO.socket(uri, options)
    private val mapper = ObjectMapper()

    init {
        socket.on("block") { args ->
            val block = mapper.readValue(args[0].toString(), Block::class.java)
            Map.instance.set(block.x, block.y, block.color)
        }

        socket.on("map") { args ->
            val map = mapper.readValue(args[0].toString(), Array<Array<Int>>::class.java)
            Map.loadMap(map)
        }

        socket.on("players") { args ->
            (args[0] as JSONArray).apply {
                for (i in 0 until length()) {
                    val uuid = getJSONObject(i).getString("uuid")
                    val name = getJSONObject(i).getString("name")
                    val pos = mapper.readValue(getJSONObject(i).getJSONObject("pos").toString(), Point::class.java)
                    val skin = mapper.readValue(getJSONObject(i).getJSONObject("skin").toString(), Skin::class.java)

                    Map.instance.addPlayer(uuid, name, pos, skin)
                }
            }
        }

        socket.on("player") { args ->
            val uuid = (args[0] as JSONObject).getString("uuid")
            val name = (args[0] as JSONObject).getString("name")
            val pos = mapper.readValue((args[0] as JSONObject).getJSONObject("pos").toString(), Point::class.java)
            val skin = mapper.readValue((args[0] as JSONObject).getJSONObject("skin").toString(), Skin::class.java)

            Map.instance.addPlayer(uuid, name, pos, skin)
        }

        socket.on("error") { args ->
            val error = mapper.readValue(args[0].toString(), JsonNode::class.java)
            SpendYourTime.instance.displayError(error.get("title").asText(), error.get("message").asText())
        }

        socket.on("gameData") { args ->
            val gameData = mapper.readValue(args[0].toString(), JsonNode::class.java)
            SpendYourTime.instance.scoreBoard[gameData.get("_number")
                .asInt() - 1].setPrice(gameData.get("priceOfNextBlock").asInt())
            SpendYourTime.instance.scoreBoard[gameData.get("_number").asInt() - 1].setCoin(
                gameData.get("money").asInt()
            )
            SpendYourTime.instance.scoreBoard[gameData.get("_number").asInt() - 1].setBlock(
                gameData.get("numberOfBlock").asInt()
            )
        }

        socket.on("Timer") {args ->
            val timer = mapper.readValue(args[0].toString(), JsonNode::class.java)
            val reset = timer.get("reset").asInt()
            val update = timer.get("update").asInt()

            //convert to seconds
            val resetTime = reset / 1000
            val updateTime = update / 1000

            Platform.runLater {
                SpendYourTime.instance.timerReset.text = "Reset in $resetTime s"
                SpendYourTime.instance.timerGain.text = "Gain in $updateTime s"
            }
        }
    }

    fun start() {
        socket.connect()
    }

    fun build(x: Int, y: Int) {
        socket.emit("build", JSONObject().put("x", x).put("y", y))
    }

    fun join(name: String, team: Int) {
        if ((1..4).contains(team)) {
            socket.emit(
                "join",
                JSONObject().put("name", name).put("team", team)
                    .put("body", (0..Skins.instance.getMaxBody()).random())
                    .put("outfit", (0..Skins.instance.getMaxOutfit()).random())
                    .put("hair", (0..Skins.instance.getMaxHair()).random())
                    .put("eyes", (0..Skins.instance.getMaxEye()).random())
                    .put("accessory", (0..Skins.instance.getMaxAccessory()).random())
            )
        }
    }

    fun move(x: Int, y: Int) {
        socket.emit("move", JSONObject().put("x", x).put("y", y))
    }

    fun close() {
        socket.disconnect()
    }
}
