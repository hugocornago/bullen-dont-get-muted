package net.cornago.bullen.features.muted

import meteordevelopment.orbit.EventHandler
import net.cornago.bullen.Bullenbypassmute
import net.cornago.bullen.Bullenbypassmute.logger
import net.cornago.bullen.event.WebSocketEvent
import net.cornago.bullen.features.Module
import net.cornago.bullen.utils.CLIENT_CONNECT_STRING
import net.cornago.bullen.utils.SERVER_CONNECT_STRING
import net.cornago.bullen.utils.WebSocketConnection
import net.cornago.bullen.utils.createClientMessage
import net.minecraft.text.Text

object BypassServer : Module(
    name = "bypassServer",
) {
    private var lastMesageSentTime = System.currentTimeMillis()

    // will send the messages to the servers
    override fun onEnable() {
        super.onEnable()
        logger.info("enabling server")
        if (!WebSocketConnection.connected) WebSocketConnection.connect(
            Bullenbypassmute.okClient,
            if (Bullenbypassmute.debug) "http://localhost:8008/ws" else "https://bullen.cornago.net/ws"
        )
        WebSocketEvent.Send(SERVER_CONNECT_STRING).postAndCatch()
    }

    override fun onDisable() {
        super.onDisable()
        logger.info("disabling server")
        WebSocketConnection.shutdown()
    }

    @EventHandler()
    fun onServerMessage(event: WebSocketEvent.Receive) = with(event) {
        mc.player?.networkHandler?.sendChatMessage("Bullen_ware > " + this.message) ?: return
    }
}