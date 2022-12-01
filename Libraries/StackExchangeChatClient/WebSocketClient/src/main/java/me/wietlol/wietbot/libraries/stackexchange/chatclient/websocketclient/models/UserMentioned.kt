package me.wietlol.wietbot.libraries.stackexchange.chatclient.websocketclient.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserMentioned(
	val timeStamp: Long,
	override val content: String,
	override val id: Int,
	override val userId: Int,
	val targetUserId: Int,
	override val userName: String,
	override val roomId: Int,
	override val roomName: String,
	val messageId: Int,
	val parentId: Int?,
	val showParent: Boolean?
) : MessageEvent
{
	override val eventType: EventType
		get() = EventType.UserMentioned
}
