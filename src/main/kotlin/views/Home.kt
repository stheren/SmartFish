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
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class Home private constructor() : VBox() {
    companion object {
        val instance = Home()

        private const val PATCH_STYLE = "-fx-text-fill: #93baba; -fx-font-size: 10px;"
        private const val NEW_VERSION_STYLE = "-fx-text-fill: #7f7f7f; -fx-font-size: 10px; -fx-font-weight: bold;"
        private const val VERSION = "v1.0.1"
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
            try {
                val url = URL("https://api.github.com/repos/stheren/smartFish/releases/latest")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val responseBody = reader.readText()
                    reader.close()

                    val objectMapper = ObjectMapper()
                    val json = objectMapper.readTree(responseBody)

                    // Compare versions
                    val latestVersionStr = json.get("tag_name").asText().substring(1)
                    val latestVersionArr = latestVersionStr.split(".")
                    val latestVersion = IntArray(3) { latestVersionArr[it].toInt() }

                    val currentVersionArr = VERSION.substring(1).split(".")
                    val currentVersion = IntArray(3) { currentVersionArr[it].toInt() }

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
                }

                connection.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

}
