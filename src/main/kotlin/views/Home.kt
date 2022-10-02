package views

import WindowsAfk
import com.fasterxml.jackson.databind.ObjectMapper
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


class Home private constructor() : VBox() {
    companion object {
        val instance = Home()

        private const val PATCH_STYLE = "-fx-text-fill: #93baba; -fx-font-size: 10px;"
        private const val NEW_VERSION_STYLE = "-fx-text-fill: #7f7f7f; -fx-font-size: 10px; -fx-font-weight: bold;"
        private const val VERSION = "v2.0.1"
    }

    init {
        alignment = Pos.CENTER
        children.add(Label("SmartFish").apply {
            style =
                "-fx-font-size: 60px; -fx-text-fill: linear-gradient(from 25% 25% to 75% 75%,-fx-discord-red, -fx-discord-yellow, -fx-discord-blue); -fx-font-weight: bold;"
        })
        style = "-fx-background-color: #2C2F33;"
        setVgrow(this, Priority.ALWAYS)

        children.add(Label("Version $VERSION").apply {
            style = PATCH_STYLE
        })

        children.add(Pane().apply {
            setPrefSize(0.0, 50.0)
        })

        Thread {
            val client = HttpClient.newBuilder().build()
            val request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/repos/stheren/smartFish/releases/latest"))
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            val json = ObjectMapper().readTree(response.body())

            //compare versions
            val latestVersion = json.get("tag_name").asText().substring(1).split(".").map { it.toInt() }
            val currentVersion = VERSION.substring(1).split(".").map { it.toInt() }
            val latestVersionInt = latestVersion[0] * 10000 + latestVersion[1] * 100 + latestVersion[2]
            val currentVersionInt = currentVersion[0] * 10000 + currentVersion[1] * 100 + currentVersion[2]

            if (latestVersionInt > currentVersionInt) {
                Platform.runLater {
                    children.add(Label("New version available! (${json.get("tag_name").asText()})").apply {
                        style = NEW_VERSION_STYLE
                    })
                    children.add(Hyperlink("Download").apply {
                        style = NEW_VERSION_STYLE
                        setOnAction {
                            WindowsAfk.hostServices.showDocument(json.get("html_url").asText())
                        }
                    })
                }
            }
        }.start()


    }

}
