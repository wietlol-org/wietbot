package me.wietlol.wietbot.data.messages.models

import me.wietlol.wietbot.data.messages.models.models.BoldPart
import me.wietlol.wietbot.data.messages.models.models.Content
import me.wietlol.wietbot.data.messages.models.models.ContentPart
import me.wietlol.wietbot.data.messages.models.models.InlineMonospacedPart
import me.wietlol.wietbot.data.messages.models.models.ItalicPart
import me.wietlol.wietbot.data.messages.models.models.MonospacedPart
import me.wietlol.wietbot.data.messages.models.models.MultiPart
import me.wietlol.wietbot.data.messages.models.models.QuotePart
import me.wietlol.wietbot.data.messages.models.models.StrikeoutPart
import me.wietlol.wietbot.data.messages.models.models.TextPart
import me.wietlol.wietbot.data.messages.models.models.UrlPart

object SimpleTextExtractor
{
	fun getSimpleText(content: Content): String =
		getSimpleText(content.part)
	
	fun getSimpleText(part: ContentPart): String =
		when (part)
		{
			is MultiPart -> part.parts.joinToString("", transform = ::getSimpleText)
			is TextPart -> part.text
			is MonospacedPart -> part.text
			is InlineMonospacedPart -> part.text
			is UrlPart -> getSimpleText(part.text)
			is QuotePart -> getSimpleText(part.inner)
			is BoldPart -> getSimpleText(part.inner)
			is ItalicPart -> getSimpleText(part.inner)
			is StrikeoutPart -> getSimpleText(part.inner)
			else -> ""
		}
}
