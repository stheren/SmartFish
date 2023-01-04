package ChatGPT

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class TextDavinci {
    companion object {
        private const val API_URL = "https://api.openai.com/v1/engines/text-davinci-003/completions"
        private var API_KEY = "YOUR_API_KEY"

        init {
            // Read API key from file ("api_key.json")
            val file = File("api_key.json")
            // use jackson to parse json
            val mapper = jacksonObjectMapper()
            val json = mapper.readTree(file)
            API_KEY = json["api_key"].asText()
        }

        fun getCompletion(prompt: String): String {
            // Créer une nouvelle connexion HTTP
            val connection = URL(API_URL).openConnection() as HttpURLConnection

            // Configurer la connexion
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer $API_KEY")

            // Écrire le corps de la requête avec la prompt de l'utilisateur
            val os = connection.outputStream
            os.write("{\"prompt\":\"$prompt\",\"max_tokens\":1024}".toByteArray())
            os.flush()
            os.close()

            // Lire la réponse de l'API
            val output = connection.inputStream
            val scanner = Scanner(output).useDelimiter("\\A")
            val response = scanner.next()
            scanner.close()
            output.close()

            // Renvoyer la réponse de l'API
            return response
        }

    }
}

