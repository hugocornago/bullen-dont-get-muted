package net.cornago.bullen.event

import net.minecraft.network.packet.Packet

abstract class PacketEvent(val packet: Packet<*>) : CancellableEvent() {

    class Receive(packet: Packet<*>) : PacketEvent(packet)

    class Send(packet: Packet<*>) : PacketEvent(packet)
}