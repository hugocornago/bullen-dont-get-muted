package net.cornago.bullen.event

import net.cornago.bullen.Bullenbypassmute

abstract class Event {

    fun postAndCatch() {
        runCatching {
            Bullenbypassmute.EVENT_BUS.post(this)
        }.onFailure {
            Bullenbypassmute.logger.error(it.toString())
        }
    }
}