package net.cornago.bullen.event

abstract class WebSocketEvent(val message: String) : Event() {
    class Receive(message: String): WebSocketEvent(message)
    class Send(message: String): WebSocketEvent(message)
}