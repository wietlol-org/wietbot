// hash: #f1ae63e7
// data: serializationKey:96101127-f06a-42cd-8068-b7a6360edc35
// @formatter:off

package me.wietlol.wietbot.data.messages.models.models

import java.util.UUID
import me.wietlol.bitblock.api.serialization.BitSerializable
import me.wietlol.utils.common.Jsonable
import me.wietlol.utils.common.emptyHashCode
import me.wietlol.utils.common.toJsonString
import me.wietlol.utils.common.with

// Generated by BitBlock version 1.0.0

// @formatter:on
// @tomplot:customCode:start:gAeCSq
// @tomplot:customCode:end
// @formatter:off

interface MessageEventList : BitSerializable, Jsonable
{
	companion object
	{
		val serializationKey: UUID
			get() = UUID.fromString("serializationKey")
	}
	
	override val serializationKey: UUID
		get() = Companion.serializationKey
	
	val messageId: String
	
	val events: List<ChatEvent>
	
	fun isEqualTo(other: Any?): Boolean
	{
		if (this === other) return true
		if (other == null) return false
		if (other !is MessageEventList) return false
		
		if (messageId != other.messageId) return false
		if (events != other.events) return false
		
		return true
	}
	
	fun computeHashCode(): Int =
		emptyHashCode
			.with(messageId)
			.with(events)
	
	override fun toJson(): String =
		"""{"messageId":${messageId.toJsonString()},"events":${events.toJsonString()}}"""
	
	override fun duplicate(): MessageEventList
	
	// @formatter:on
	// @tomplot:customCode:start:32T3K8
	// @tomplot:customCode:end
	// @formatter:off
}
// @formatter:on
