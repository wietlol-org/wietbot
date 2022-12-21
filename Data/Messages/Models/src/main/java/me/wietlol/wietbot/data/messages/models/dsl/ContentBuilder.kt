package me.wietlol.wietbot.data.messages.models.dsl

import me.wietlol.wietbot.data.messages.models.models.Content
import me.wietlol.wietbot.data.messages.models.models.ContentPart
import me.wietlol.wietbot.data.messages.models.models.DefaultBoldPart
import me.wietlol.wietbot.data.messages.models.models.DefaultContent
import me.wietlol.wietbot.data.messages.models.models.DefaultInlineMonospacedPart
import me.wietlol.wietbot.data.messages.models.models.DefaultItalicPart
import me.wietlol.wietbot.data.messages.models.models.DefaultMonospacedPart
import me.wietlol.wietbot.data.messages.models.models.DefaultMultiPart
import me.wietlol.wietbot.data.messages.models.models.DefaultQuotePart
import me.wietlol.wietbot.data.messages.models.models.DefaultStrikeoutPart
import me.wietlol.wietbot.data.messages.models.models.DefaultTagPart
import me.wietlol.wietbot.data.messages.models.models.DefaultTextPart
import me.wietlol.wietbot.data.messages.models.models.DefaultUrlPart
import me.wietlol.wietbot.data.messages.models.models.MultiPart

@Suppress("unused")
class ContentBuilder
{
	private val parts: MutableList<ContentPart> = mutableListOf()
	
	fun toSinglePart(): ContentPart =
		parts.singleOrNull() ?: DefaultMultiPart(parts)
	
	fun text(text: String): ContentBuilder
	{
		parts.add(DefaultTextPart(text))
		return this
	}
	
	fun mono(text: String): ContentBuilder
	{
		parts.add(DefaultInlineMonospacedPart(text))
		return this
	}
	
	fun monoBlock(text: String): ContentBuilder
	{
		parts.add(DefaultMonospacedPart(text))
		return this
	}
	
	fun urlPart(href: String, text: ContentBuilder.() -> Unit, title: (ContentBuilder.() -> Unit)? = null): ContentBuilder
	{
		val textPart = ContentBuilder().apply(text).toSinglePart()
		
		if (textPart is MultiPart && textPart.parts.isEmpty())
			parts.add(DefaultTextPart(href))
		else
			parts.add(
				DefaultUrlPart(
				href,
				textPart,
				if (title != null)
					ContentBuilder().apply(title).toSinglePart()
				else
					null
			)
			)
		return this
	}
	
	fun quotePart(content: ContentBuilder.() -> Unit): ContentBuilder
	{
		parts.add(
			DefaultQuotePart(
			ContentBuilder().apply(content).toSinglePart()
		)
		)
		return this
	}
	
	fun boldPart(content: ContentBuilder.() -> Unit): ContentBuilder
	{
		parts.add(
			DefaultBoldPart(
			ContentBuilder().apply(content).toSinglePart()
		)
		)
		return this
	}
	
	fun italicPart(content: ContentBuilder.() -> Unit): ContentBuilder
	{
		parts.add(
			DefaultItalicPart(
			ContentBuilder().apply(content).toSinglePart()
		)
		)
		return this
	}
	
	fun strikeoutPart(content: ContentBuilder.() -> Unit): ContentBuilder
	{
		parts.add(
			DefaultStrikeoutPart(
			ContentBuilder().apply(content).toSinglePart()
		)
		)
		return this
	}
	
	fun tagPart(name: String): ContentBuilder
	{
		parts.add(DefaultTagPart(name))
		return this
	}
	
	companion object
	{
		fun content(handler: ContentBuilder.() -> Unit): Content =
			DefaultContent(
				ContentBuilder().apply(handler).toSinglePart()
			)
	}
}
