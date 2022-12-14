// hash: #77c3ea33
// @formatter:off

package me.wietlol.wietbot.data.messages.models.serializers

import java.io.InputStream
import java.io.OutputStream
import java.util.UUID
import me.wietlol.bitblock.api.serialization.DeserializationContext
import me.wietlol.bitblock.api.serialization.ModelSerializer
import me.wietlol.bitblock.api.serialization.Schema
import me.wietlol.bitblock.api.serialization.SerializationContext
import me.wietlol.bitblock.api.serialization.deserialize
import me.wietlol.utils.common.streams.readUnsignedVarInt
import me.wietlol.utils.common.streams.writeUnsignedVarInt
import me.wietlol.wietbot.data.messages.models.builders.BoldPartBuilder
import me.wietlol.wietbot.data.messages.models.models.*
import me.wietlol.wietbot.data.messages.models.models.BoldPart

// Generated by BitBlock version 1.0.0

// @formatter:on
// @tomplot:customCode:start:70v0f9
// @tomplot:customCode:end
// @formatter:off

object BoldPartSerializer : ModelSerializer<BoldPart, BoldPart>
{
	private val endOfObject: Int = 0
	
	private val innerIndex: Int = 1
	
	override val modelId: UUID
		get() = BoldPart.serializationKey
	
	override val dataClass: Class<BoldPart>
		get() = BoldPart::class.java
	
	override fun serialize(serializationContext: SerializationContext, stream: OutputStream, schema: Schema, entity: BoldPart)
	{
		stream.writeUnsignedVarInt(innerIndex)
		schema.serialize(serializationContext, stream, entity.inner)
		
		stream.writeUnsignedVarInt(endOfObject)
	}
	
	override fun deserialize(deserializationContext: DeserializationContext, stream: InputStream, schema: Schema): BoldPart
	{
		var inner: ContentPart? = null
		
		while (true)
		{
			when (stream.readUnsignedVarInt())
			{
				endOfObject -> return DefaultBoldPart(
					inner!!,
				)
				innerIndex -> inner = schema.deserialize(deserializationContext, stream)
				else -> schema.deserialize<Any>(deserializationContext, stream)
			}
		}
	}
	
	// @formatter:on
	// @tomplot:customCode:start:5CFs54
	// @tomplot:customCode:end
	// @formatter:off
}
// @formatter:on
