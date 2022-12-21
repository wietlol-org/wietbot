package me.wietlol.wietbot.libraries.stackexchange.chatclient.websocketclient

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import me.wietlol.loggo.common.CommonLog
import me.wietlol.loggo.core.loggers.GenericLogger
import me.wietlol.utils.json.JacksonSerializerAdapter
import me.wietlol.wietbot.libraries.stackexchange.chatclient.chatclient.SeCredentials
import me.wietlol.wietbot.libraries.stackexchange.chatclient.websocketclient.models.BulkChatEvent
import me.wietlol.wietbot.libraries.stackexchange.chatclient.websocketclient.models.serializers.BulkChatEventDeserializer

class HttpSeChatClientTest
{
	companion object
	{
		@JvmStatic
		fun main(args: Array<String>)
		{
			val email = args[0]
			val password = args[1]
			
			val events = CommonSeChatEvents()
			val logger = GenericLogger<CommonLog>({ logs ->
				logs.forEach {
					println(it.eventId.name)
				}
			})
			val serializer = JacksonSerializerAdapter(
				ObjectMapper()
					.also { it.registerModule(KotlinModule()) }
					.also { mapper ->
						mapper.registerModule(
							SimpleModule()
								.apply { addDeserializer(BulkChatEvent::class.java, BulkChatEventDeserializer(mapper)) }
//								.apply { addSerializer(CommonLog::class.java, CommonLogSerializer()) }
						)
					}
			)

			val credentials = SeCredentials(email, password)
//			val chatClientFactory = HttpSeChatClientFactory(serializer, logger)
//			val chatClient = chatClientFactory.create(credentials)
			val webSocketClientFactory = HttpSeWebSocketClientFactory(
				SeWebSocketListener(NoOpWebSocketListener(), serializer, events.eventMap),
				events,
				serializer,
				1,
				logger
			)
			
			events.onMessagePosted.register {
				println("${it.userName}: ${it.content}")
			}
			
			val webSocketClient = webSocketClientFactory.create(credentials)
			webSocketClient.joinRoom(7)
			webSocketClient.joinRoom(17)
			webSocketClient.joinRoom(139)
			
//			webSocketClientFactory.create(credentials).use { webSocketClient ->
////				val messageId = webSocketClient.sendMessage(1, "wörk 2")
////				Thread.sleep(2_000)
////				webSocketClient.editMessage(messageId, "wörk 3")
////				Thread.sleep(2_000)
//
//				webSocketClient.joinRoom(1)
//				Thread.sleep(2_000)
//				webSocketClient.leaveRoom(1)
//				Thread.sleep(2_000)
//				webSocketClient.leaveAllRooms()
//				Thread.sleep(2_000)
//				webSocketClient.reconnect(1)
//			}

//			chatClient.sendMessage(1, "You feel an evil presence approaching you.")
		}
	}
}
