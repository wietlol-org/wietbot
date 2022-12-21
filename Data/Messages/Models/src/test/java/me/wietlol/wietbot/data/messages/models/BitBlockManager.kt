package me.wietlol.wietbot.data.messages.models

import me.wietlol.bitblock.codegenerator.BitModuleProcessor
import me.wietlol.bitblock.codegenerator.generators.kotlin.BitModuleKotlinGenerator
import me.wietlol.bitblock.core.BitSchemaBuilder
import org.junit.Test
import java.io.File

class BitBlockManager
{
//	@Test
	fun processBitModule()
	{
		// bitblock processBitModule <filepath>
		BitModuleProcessor.processBitModule(
			File("src/main/resources/me/wietlol/wietbot/data/messages/models/WietbotMessages.bitmodule"),
			BitModuleKotlinGenerator(),
		)
	}
	
//	@Test
	fun buildBitSchema()
	{
		BitSchemaBuilder.buildSchema(
			File("src/main/resources/me/wietlol/wietbot/data/messages/models/WietbotMessages.bitschema"),
			WietbotDataMessages.modelSerializers,
		)
	}
	
	@Test
	fun foo2()
	{
//		val schema = BitSchemaBuilder.buildSchema(
//			WietbotDataMessages.javaClass.getResourceAsStream("/me/wietlol/wietbot/data/messages/models/WietbotMessages.bitschema"),
//			listOf(BitBlockBase, WietbotDataMessages)
//		)
//
//		val file = File("D:/Users/Harry/RiderProjects/WietbotDataMessages/WietbotDataMessagesTests/Test1.bit")
//
//		println(schema.deserialize(file.inputStream())) // broken in C#
//		println(schema.deserialize<BitDynamicObject>(file.inputStream())) // broken in Kotlin and C#
//		println(schema.deserialize<MessageEventList>(file.inputStream())) // broken in C#
	}
	
	@Test
	fun foo()
	{
//		val schema = BitSchemaBuilder.buildSchema(
//			WietbotDataMessages.javaClass.getResourceAsStream("/me/wietlol/wietbot/data/messages/models/WietbotMessages.bitschema"),
//			listOf(BitBlockBase, WietbotDataMessages)
//		)
//
//		val messageId = "messageId"
//		val userId = "userId"
//		val userName = "userName"
//
//		val platform = DefaultPlatform.stackOverflow
//		val user = DefaultChatUser(
//			userId,
//			userName,
//			platform,
//			42,
//			userId,
//			userName,
//			null,
//			null,
//			null,
//			null,
//		)
//		val messageSource = DefaultMessageSource(
//			"7",
//			"C#",
//			platform,
//		)
//
//		val events = DefaultMessageEventList(
//			messageId,
//			listOf(
//				DefaultMessagePostedEvent(
//					"eventId1",
//					11111,
//					messageId,
//					ContentBuilder.content {
//						text("Hello World!")
//					},
//					user,
//					messageSource,
//					platform,
//				),
//				DefaultMessageEditedEvent(
//					"eventId2",
//					22222,
//					messageId,
//					ContentBuilder.content {
//						text("Hello, World!")
//					},
//					user,
//					messageSource,
//					platform,
//				),
//				DefaultMessageEditedEvent(
//					"eventId3",
//					33333,
//					messageId,
//					ContentBuilder.content {
//						text("(removed)")
//					},
//					user,
//					messageSource,
//					platform,
//				),
//				DefaultMessageDeletedEvent(
//					"eventId4",
//					44444,
//					messageId,
//					user,
//					messageSource,
//					platform,
//				),
//			),
//		)
//
//		val file = File("D:/Users/Harry/RiderProjects/WietbotDataMessages/WietbotDataMessagesTests/Test1.bit")
//		file.createNewFile()
//		schema.serialize(file.outputStream(), events)
	}
}
