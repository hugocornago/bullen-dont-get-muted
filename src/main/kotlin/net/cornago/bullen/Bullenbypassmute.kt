package net.cornago.bullen

import meteordevelopment.orbit.EventBus
import net.cornago.bullen.commands.ToggleCommand
import net.cornago.bullen.utils.WebUtils
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.client.MinecraftClient
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

object Bullenbypassmute : ModInitializer {

	val mc: MinecraftClient
		get() = MinecraftClient.getInstance()

	val EVENT_BUS = EventBus()

	const val MOD_ID = "bullen-bypass-mute"
	val logger = LoggerFactory.getLogger(MOD_ID)

	val okClient = WebUtils.createClient()

	val debug = false



	override fun onInitialize() {
		EVENT_BUS.registerLambdaFactory("net.cornago") { lookupInMethod, klass ->
			lookupInMethod.invoke(null, klass, MethodHandles.lookup()) as MethodHandles.Lookup
		}

		ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
			ToggleCommand.register(dispatcher)
		}
	}
}