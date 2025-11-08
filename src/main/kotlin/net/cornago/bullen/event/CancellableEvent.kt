package net.cornago.bullen.event

import meteordevelopment.orbit.ICancellable
import net.cornago.bullen.Bullenbypassmute

abstract class CancellableEvent : ICancellable {
    private var cancelled = false

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancelled: Boolean) {
        this.cancelled = cancelled
    }

    fun postAndCatch(): Boolean {
        runCatching {
            Bullenbypassmute.EVENT_BUS.post(this)
        }.onFailure {
            Bullenbypassmute.logger.error(it.toString())
        }

        return cancelled
    }
}