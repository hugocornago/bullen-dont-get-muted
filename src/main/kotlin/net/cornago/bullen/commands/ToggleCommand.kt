package net.cornago.bullen.commands

import com.github.stivais.commodore.Commodore
import net.cornago.bullen.Bullenbypassmute
import net.cornago.bullen.features.muted.BypassClient
import net.cornago.bullen.features.muted.BypassServer
import net.minecraft.text.Text

val ToggleCommand = Commodore("muted") {
    literal("client").runs {
        if (BypassServer.enabled) return@runs
        BypassClient.toggle()
        Bullenbypassmute.mc.player?.sendMessage(
            Text.literal("Client ${if (BypassClient.enabled) "enabled" else "disabled"}"),
            false
        )
    }

    literal("server").runs {
        if (BypassClient.enabled) return@runs
        BypassServer.toggle()
        Bullenbypassmute.mc.player?.sendMessage(
            Text.literal("Server ${if (BypassServer.enabled) "enabled" else "disabled"}"),
            false
        )
    }
}