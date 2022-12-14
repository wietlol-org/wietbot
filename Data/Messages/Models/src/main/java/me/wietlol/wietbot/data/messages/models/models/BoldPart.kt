// hash: #9a9940ff
// data: serializationKey:cb66e5ab-30be-44b7-974d-c2b9f2a68974
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

interface BoldPart : BitSerializable, ContentPart, Jsonable
{
	companion object
	{
		val serializationKey: UUID
			get() = UUID.fromString("serializationKey")
	}
	
	override val serializationKey: UUID
		get() = Companion.serializationKey
	
	val inner: ContentPart
	
	fun isEqualTo(other: Any?): Boolean
	{
		if (this === other) return true
		if (other == null) return false
		if (other !is BoldPart) return false
		
		if (inner != other.inner) return false
		
		return true
	}
	
	fun computeHashCode(): Int =
		emptyHashCode
			.with(inner)
	
	override fun toJson(): String =
		"""{"inner":${inner.toJsonString()}}"""
	
	override fun duplicate(): BoldPart
	
	// @formatter:on
	// @tomplot:customCode:start:32T3K8
	// @tomplot:customCode:end
	// @formatter:off
}
// @formatter:on
