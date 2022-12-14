package me.wietlol.wietbot.libraries.stackexchange.chatclient.chatclient

import me.wietlol.loggo.common.CommonLogger
import me.wietlol.loggo.common.EventId
import me.wietlol.loggo.common.ScopedSourceLogger
import me.wietlol.loggo.common.logTrace
import me.wietlol.utils.json.SimpleJsonSerializer
import me.wietlol.utils.json.deserialize
import me.wietlol.wietbot.libraries.stackexchange.chatclient.chatclient.models.SeSendMessageResponse

class HttpSeChatClient(
	private val client: SeClient,
	private val chatSiteUrl: String,
	private val accountFKey: String,
	private val serializer: SimpleJsonSerializer,
	logger: CommonLogger
) : SeChatClient
{
	private val logger = ScopedSourceLogger(logger) { it + "HttpSeChatClient" }
	
	private val sendMessageEventId = EventId(678840335, "sending-message")
	private val sendMessageCompleteEventId = EventId(1107745972, "sending-message-completed")
	private val editMessageEventId = EventId(573251339, "editing-message")
	private val editMessageCompleteEventId = EventId(294011604, "editing-message-completed")
	
	override fun sendMessage(roomId: Int, text: String): Int
	{
		logger.logTrace(
			sendMessageEventId, mapOf(
				"roomId" to roomId,
				"text" to text
			)
		)
		
		val response = client.post("$chatSiteUrl/chats/$roomId/messages/new", listOf(
			"text" to text,
			"fkey" to accountFKey
		))
		
		val json = response.body()
		val sendMessageResponse = serializer.deserialize<SeSendMessageResponse>(json)
		
		logger.logTrace(
			sendMessageCompleteEventId, mapOf(
				"response" to json
			)
		)
		
		return sendMessageResponse.id
	}
	
	override fun editMessage(messageId: Int, text: String)
	{
		logger.logTrace(
			editMessageEventId, mapOf(
				"messageId" to messageId,
				"text" to text
			)
		)
		
		val response = client.post("$chatSiteUrl/messages/$messageId", listOf(
			"text" to text,
			"fkey" to accountFKey
		))
		
		logger.logTrace(
			editMessageCompleteEventId, mapOf(
				"response" to response.body()
			)
		)
	}
}
