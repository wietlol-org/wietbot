package me.wietlol.wietbot.libraries.stackexchange.chatclient.websocketclient

import me.wietlol.loggo.common.CommonLogger
import me.wietlol.loggo.common.EventId
import me.wietlol.loggo.common.ScopedSourceLogger
import me.wietlol.loggo.common.logInformation
import me.wietlol.utils.json.SimpleJsonSerializer
import me.wietlol.wietbot.libraries.stackexchange.chatclient.chatclient.HttpSeChatClient
import me.wietlol.wietbot.libraries.stackexchange.chatclient.chatclient.HttpSeClient
import me.wietlol.wietbot.libraries.stackexchange.chatclient.chatclient.HttpSeClient.Companion.chatSiteUrl
import me.wietlol.wietbot.libraries.stackexchange.chatclient.chatclient.SeCredentials

class HttpSeWebSocketClientFactory(
	val listener: WebSocketListener,
	val chatEvents: SeChatEvents,
	val serializer: SimpleJsonSerializer,
	val initialRoom: Int,
	val logger: CommonLogger,
) : SeWebSocketClientFactory
{
	private val loginEventId = EventId(2145437065, "logging-in")
	
	private val selfLogger = ScopedSourceLogger(logger) { it + "HttpSeWebSocketClientFactory" }
	
	override fun create(credentials: SeCredentials): HttpSeWebSocketClient
	{
		selfLogger.logInformation(loginEventId, mapOf(
			"username" to credentials.emailAddress
		))
		
		val seClient = HttpSeClient()
		val accountFKey = seClient.login(credentials)
		
		val client = HttpSeChatClient(seClient, chatSiteUrl, accountFKey, serializer, logger)
		return HttpSeWebSocketClient(seClient, client, chatSiteUrl, accountFKey, listener, chatEvents, serializer, initialRoom, logger)
	}
}
