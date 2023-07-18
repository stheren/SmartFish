package SpendYourTime

import WindowsAfk
import com.fasterxml.jackson.databind.ObjectMapper
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URI


class Connexion private constructor()
{
    companion object
    {
        val instance: Connexion = Connexion()
    }

    private var uri: URI = if (WindowsAfk.address != null && WindowsAfk.port != null)
    {
        URI.create("ws://${WindowsAfk.address}:${WindowsAfk.port}")
    }
    else
    {
        URI.create("ws://spend.calenpart.com")
    }

    private val options: IO.Options = IO.Options.builder().build()
    private var socket: Socket      = IO.socket(uri, options)
    private val mapper              = ObjectMapper()

    init
    {
        // INIT THE SOCKET
    }

    fun start()
    {
        socket.connect()
    }

    fun close()
    {
        socket.disconnect()
    }
}
