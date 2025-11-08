package net.cornago.bullen.features

import net.cornago.bullen.Bullenbypassmute

/**
 * Class that represents a module. And handles all the settings.
 * @author Aton
 */
abstract class Module(
    val name: String,
    toggled: Boolean = false,
) {

    /**
     * Reference for if the module is enabled
     *
     * When it is enabled, it is registered to the Forge Eventbus,
     * otherwise it's unregistered unless it has the annotation [@AlwaysActive][AlwaysActive]
     */
    var enabled: Boolean = toggled
        private set

    protected inline val mc get() = Bullenbypassmute.mc

    /**
     * Gets toggled when module is enabled
     */
    open fun onEnable() {
        Bullenbypassmute.EVENT_BUS.subscribe(this)
    }

    /**
     * Gets toggled when module is disabled
     */
    open fun onDisable() {
        Bullenbypassmute.EVENT_BUS.unsubscribe(this)
    }

    fun toggle() {
        enabled = !enabled
        if (enabled) onEnable()
        else onDisable()
    }
}