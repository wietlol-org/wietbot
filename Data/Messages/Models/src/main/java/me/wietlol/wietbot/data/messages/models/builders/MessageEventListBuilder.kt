// hash: #d4e73ae7
// @formatter:off

package me.wietlol.wietbot.data.messages.models.builders

import me.wietlol.wietbot.data.messages.models.models.*
import me.wietlol.wietbot.data.messages.models.models.DefaultMessageEventList

// Generated by BitBlock version 1.0.0

// @formatter:on
// @tomplot:customCode:start:f5k3GB
// @tomplot:customCode:end
// @formatter:off

class MessageEventListBuilder
{
	var messageId: String? = null
	
	var events: MutableList<ChatEvent>? = mutableListOf()
	
	fun build(): MessageEventList =
		DefaultMessageEventList(
			messageId!!,
			events!!.toMutableList(),
		)
	
	// @formatter:on
	// @tomplot:customCode:start:0ETUWm
	// @tomplot:customCode:end
	// @formatter:off
}
// @formatter:on
