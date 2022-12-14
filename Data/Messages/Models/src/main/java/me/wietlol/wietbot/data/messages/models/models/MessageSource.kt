// hash: #218b7a56
// data: serializationKey:ad364633-b5eb-447c-bb3c-e228a4786cbc
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

interface MessageSource : BitSerializable, Jsonable
{
	companion object
	{
		val serializationKey: UUID
			get() = UUID.fromString("serializationKey")
	}
	
	override val serializationKey: UUID
		get() = Companion.serializationKey
	
	val id: String
	
	val name: String
	
	val platform: Platform
	
	fun isEqualTo(other: Any?): Boolean
	{
		if (this === other) return true
		if (other == null) return false
		if (other !is MessageSource) return false
		
		if (id != other.id) return false
		if (name != other.name) return false
		if (platform != other.platform) return false
		
		return true
	}
	
	fun computeHashCode(): Int =
		emptyHashCode
			.with(id)
			.with(name)
			.with(platform)
	
	override fun toJson(): String =
		"""{"id":${id.toJsonString()},"name":${name.toJsonString()},"platform":${platform.toJsonString()}}"""
	
	override fun duplicate(): MessageSource
	
	// @formatter:on
	// @tomplot:customCode:start:32T3K8
	// @tomplot:customCode:end
	// @formatter:off
}
// @formatter:on
