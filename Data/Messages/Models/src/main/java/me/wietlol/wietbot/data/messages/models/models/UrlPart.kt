// hash: #cdf819c1
// data: serializationKey:15f66d2e-407b-4360-8bb7-4a7ec297664a
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

interface UrlPart : BitSerializable, ContentPart, Jsonable
{
	companion object
	{
		val serializationKey: UUID
			get() = UUID.fromString("serializationKey")
	}
	
	override val serializationKey: UUID
		get() = Companion.serializationKey
	
	val href: String
	
	val text: ContentPart
	
	val title: ContentPart?
	
	fun isEqualTo(other: Any?): Boolean
	{
		if (this === other) return true
		if (other == null) return false
		if (other !is UrlPart) return false
		
		if (href != other.href) return false
		if (text != other.text) return false
		if (title != other.title) return false
		
		return true
	}
	
	fun computeHashCode(): Int =
		emptyHashCode
			.with(href)
			.with(text)
			.with(title)
	
	override fun toJson(): String =
		"""{"href":${href.toJsonString()},"text":${text.toJsonString()},"title":${title.toJsonString()}}"""
	
	override fun duplicate(): UrlPart
	
	// @formatter:on
	// @tomplot:customCode:start:32T3K8
	// @tomplot:customCode:end
	// @formatter:off
}
// @formatter:on
