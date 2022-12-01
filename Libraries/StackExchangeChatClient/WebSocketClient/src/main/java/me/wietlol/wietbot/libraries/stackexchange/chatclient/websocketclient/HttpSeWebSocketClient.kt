package me.wietlol.wietbot.libraries.stackexchange.chatclient.websocketclient

import me.wietlol.loggo.common.CommonLogger
import me.wietlol.loggo.common.EventId
import me.wietlol.loggo.common.ScopedSourceLogger
import me.wietlol.loggo.common.logError
import me.wietlol.loggo.common.logTrace
import me.wietlol.utils.json.SimpleJsonSerializer
import me.wietlol.utils.json.deserialize
import me.wietlol.wietbot.libraries.stackexchange.chatclient.chatclient.SeChatClient
import me.wietlol.wietbot.libraries.stackexchange.chatclient.chatclient.SeClient
import me.wietlol.wietbot.libraries.stackexchange.chatclient.chatclient.UnexpectedSituationException
import org.java_websocket.client.WebSocketClient
import java.io.Closeable
import java.net.URI
import java.time.Instant
import java.util.*
import kotlin.collections.HashMap

class HttpSeWebSocketClient(
	private val client: SeClient,
	private val chatClient: SeChatClient,
	private val chatSiteUrl: String,
	private val accountFKey: String,
	private val listener: WebSocketListener,
	private val chatEvents: SeChatEvents,
	private val serializer: SimpleJsonSerializer,
	private val initialRoom: Int,
	logger: CommonLogger,
) : SeWebSocketClient, SeChatClient by chatClient, Closeable
{
	private val logger = ScopedSourceLogger(logger) { it + "HttpSeWebSocketClient" }
	
	private val reconnectEventId = EventId(75490686, "reconnecting")
	private val closeEventId = EventId(1903508527, "closing")
	private val closeInvokedEventId = EventId(2112234893, "close-invoked")
	private val joinRoomEventId = EventId(1919191373, "join-room")
	private val leaveRoomEventId = EventId(705258753, "leave-room")
	private val leaveAllRoomsEventId = EventId(155768371, "leave-all-rooms")
	private val connectEventId = EventId(471177059, "connecting")
	private val connectErrorEventId = EventId(916289901, "connection-error")
	private val connectedEventId = EventId(1688939054, "connected-successfully")
	
	private val rooms: MutableMap<Int, Room> = HashMap()
	
	private val isClosingMap: MutableMap<WebSocketClient, Boolean> = WeakHashMap()
	private var WebSocketClient.isClosingFlag: Boolean
		get() = isClosingMap[this] ?: false
		set(value) = isClosingMap.set(this, value)
	
	private var webSocketClient: WebSocketClient = connect(initialRoom)

	override fun reconnect(roomId: Int)
	{
		logger.logTrace(reconnectEventId, mapOf(
			"roomId" to roomId
		))
		
		// try to close the existing connection if it exists
		runCatching {
			close()
		}
		
		try
		{
			webSocketClient = connect(roomId)
		}
		catch (ex: Throwable)
		{
			throw ex
		}
	}
	
	private fun reconnectIfNotClosing()
	{
		logger.logTrace(closeEventId, mapOf(
			"isClosing" to webSocketClient.isClosingFlag,
		))
		
		if (webSocketClient.isClosingFlag.not())
			reconnect(initialRoom)
	}
	
	override fun joinRoom(roomId: Int): Room
	{
		logger.logTrace(joinRoomEventId, mapOf(
			"roomId" to roomId
		))
		
		val response = client.get("$chatSiteUrl/rooms/$roomId")
		
		if (response.statusCode() == 404)
			throw RoomNotFoundException("Room $roomId does not exist.")
		
		val room = getRoom(roomId)
		reconnect(room.roomId)
		return room
	}
	
	override fun getRoom(roomId: Int): Room =
		rooms.computeIfAbsent(roomId) { Room(this, chatEvents, it) }
	
	override fun leaveRoom(roomId: Int)
	{
		logger.logTrace(leaveRoomEventId, mapOf(
			"roomId" to roomId
		))
		
		val response = client.post("$chatSiteUrl/chats/leave/$roomId", listOf(
			"quiet" to "true",
			"fkey" to accountFKey
		))
		
		if (response.statusCode() == 404)
			throw RoomNotFoundException("Room $roomId does not exist.")
		
		reconnect(initialRoom)
	}
	
	override fun leaveAllRooms()
	{
		logger.logTrace(leaveAllRoomsEventId, emptyMap<Any, Any>())
		
		client.post("$chatSiteUrl/chats/leave/all", listOf(
			"quiet" to "true",
			"fkey" to accountFKey
		))
	}
	
	override fun close()
	{
		while (!webSocketClient.isOpen)
			Thread.sleep(500)
		
		logger.logTrace(closeInvokedEventId, emptyMap<Any, Any>())
		
		webSocketClient.isClosingFlag = true
		webSocketClient.close()
	}
	
	private data class WsAuthResponse(val url: String)
	
	private fun connect(roomId: Int): WebSocketClient
	{
		logger.logTrace(connectEventId, mapOf(
			"roomId" to roomId,
			"url" to "$chatSiteUrl/ws-auth"
		))
		
		try
		{
			val response = client.post("$chatSiteUrl/ws-auth", listOf(
				"roomId" to roomId.toString(),
				"fkey" to accountFKey,
			), mapOf(
				"Origin" to chatSiteUrl,
				"Referer" to "$chatSiteUrl/rooms/$roomId",
			))
			
			if (response.statusCode() == 404)
				throw UnexpectedSituationException("ws-auth not found")
			
			val wssResponse: WsAuthResponse = serializer.deserialize(response.body())
			
			val webSocketUrl = "${wssResponse.url}?l=${Instant.now().toEpochMilli()}"
			
			val listener = ReconnectingWebSocketListener(listener) { reconnectIfNotClosing() }
			
			return JavaWebSocketClientAdapter(URI(webSocketUrl), mapOf("Origin" to chatSiteUrl), listener, logger)
				.apply { connect() }
				.also {
					logger.logTrace(connectedEventId, emptyMap<Any, Any>())
				}
		}
		catch (ex: Throwable)
		{
			logger.logError(connectErrorEventId, mapOf(
				"roomId" to roomId
			), ex)
			
			throw ex
		}
	}
}
