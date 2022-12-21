package me.wietlol.wietbot.data.messages.models

import me.wietlol.wietbot.data.messages.models.models.Content
import me.wietlol.wietbot.data.messages.models.models.ContentPart

val Content.simpleText: String
	get() = SimpleTextExtractor.getSimpleText(this)
val ContentPart.simpleText: String
	get() = SimpleTextExtractor.getSimpleText(this)
