package net.cornago.bullen.utils

import meteordevelopment.orbit.EventHandler
import net.cornago.bullen.Bullenbypassmute
import net.cornago.bullen.Bullenbypassmute.logger
import net.cornago.bullen.event.WebSocketEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString


const val CLIENT_CONNECT_STRING = """{"type": "connect", "data": "client"}"""
const val SERVER_CONNECT_STRING = """{"type": "connect", "data": "server"}"""

fun createClientMessage(message: String): String {
    return """{"type": "message", "data": "$message"}"""
}

object WebSocketConnection {
    private var webSocket: WebSocket? = null
    val connected get() = webSocket != null


    init {
        Bullenbypassmute.EVENT_BUS.subscribe(this)
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    @EventHandler()
    fun onWebSocketSend(event: WebSocketEvent.Send) {
        webSocket?.send(event.message) ?: return
    }

    fun connect(okClient: OkHttpClient, url: String) {
        shutdown()
        val request = Request.Builder().url(url).build()
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                logger.info("WebSocket connected to $url")
                this@WebSocketConnection.webSocket = webSocket
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                WebSocketEvent.Receive(text).postAndCatch()
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                onMessage(webSocket, bytes.utf8())
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(code, reason)
                logger.info("WebSocket closing: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                logger.error("WebSocket error: ${t.message}", t)
            }
        }

        webSocket = okClient.newWebSocket(request, listener)
    }

    fun shutdown() {
        webSocket?.close(1000, "Client shutdown")
        webSocket = null
    }
}
