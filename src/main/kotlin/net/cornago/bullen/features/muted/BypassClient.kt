package net.cornago.bullen.features.muted

import meteordevelopment.orbit.EventHandler
import net.cornago.bullen.Bullenbypassmute
import net.cornago.bullen.Bullenbypassmute.logger
import net.cornago.bullen.event.PacketEvent
import net.cornago.bullen.event.WebSocketEvent
import net.cornago.bullen.features.Module
import net.cornago.bullen.utils.CLIENT_CONNECT_STRING
import net.cornago.bullen.utils.WebSocketConnection
import net.cornago.bullen.utils.createClientMessage
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket
import net.minecraft.text.Text

object BypassClient : Module(
    name = "bypassClient"
) {
    private var lastMesageSentTime = System.currentTimeMillis()

    // will send the messages to the servers
    override fun onEnable() {
        logger.info("enabling client")
        super.onEnable()
        if (!WebSocketConnection.connected) WebSocketConnection.connect(
            Bullenbypassmute.okClient,
            if (Bullenbypassmute.debug) "http://localhost:8008/ws" else "https://bullen.cornago.net/ws"
        )
        WebSocketEvent.Send(CLIENT_CONNECT_STRING).postAndCatch()
    }

    override fun onDisable() {
        super.onDisable()
        logger.info("disabling client")
        WebSocketConnection.shutdown()
    }

    @EventHandler()
    fun onMessage(event: PacketEvent.Send) = with(event.packet){
        if (!this@BypassClient.enabled) return
        if (this !is ChatMessageC2SPacket) return
        WebSocketEvent.Send(createClientMessage(this.chatMessage)).postAndCatch()
        if (System.currentTimeMillis() - lastMesageSentTime > 5 * 60 * 1000) {
            warnUser()
        }
        event.cancel()
    }

    private fun warnUser() {
        Bullenbypassmute.mc.player?.sendMessage(Text.literal("[bypass-mute] Redirecting message..."), true)
            ?: return
    }
}