package me.wietlol.wietbot.data.messages.models.formatters

import me.wietlol.wietbot.data.messages.models.models.BoldPart
import me.wietlol.wietbot.data.messages.models.models.Content
import me.wietlol.wietbot.data.messages.models.models.ContentPart
import me.wietlol.wietbot.data.messages.models.models.InlineMonospacedPart
import me.wietlol.wietbot.data.messages.models.models.ItalicPart
import me.wietlol.wietbot.data.messages.models.models.MonospacedPart
import me.wietlol.wietbot.data.messages.models.models.MultiPart
import me.wietlol.wietbot.data.messages.models.models.QuotePart
import me.wietlol.wietbot.data.messages.models.models.StrikeoutPart
import me.wietlol.wietbot.data.messages.models.models.TagPart
import me.wietlol.wietbot.data.messages.models.models.TextPart
import me.wietlol.wietbot.data.messages.models.models.UrlPart
import me.wietlol.wietbot.data.messages.models.simpleText

object StackOverflowMessageFormatter
{
	private class MessageFormatData
	{
		private val builder: StringBuilder = StringBuilder()
		private val surroundings: MutableList<Pair<String, String>> = ArrayList()
		private val messages: MutableList<String> = ArrayList()
		
		private var hasContent: Boolean = false
		
		fun withSurrounding(prefix: String, postfix: String, body: () -> Unit)
		{
			surroundings += prefix to postfix
			builder.append(prefix)
			body()
			builder.append(postfix)
			surroundings.removeLast()
		}
		
		fun withQuote(body: () -> Unit)
		{
			if (hasContent)
				messages.add(buildMessage())
			
			body()
			messages.add("> ${buildMessage()}")
		}
		
		fun withMonoBlock(text: String)
		{
			if (hasContent)
				messages.add(buildMessage())
			
			messages.add(
				text
					.lines()
					.joinToString("\n") { "    $it" }
			)
		}
		
		fun append(text: String)
		{
			hasContent = true
			builder.append(text)
		}
		
		private fun clear()
		{
			builder.clear()
			hasContent = false
			surroundings.forEach {
				builder.append(it.first)
			}
		}
		
		private fun buildMessage(): String
		{
			surroundings.reversed().forEach {
				builder.append(it.second)
			}
			val text = builder.toString()
			clear()
			return text
		}
		
		fun toMessages(): List<String>
		{
			if (hasContent)
				messages.add(buildMessage())
			
			return messages
		}
	}
	
	fun format(content: Content): List<String>
	{
		return MessageFormatData()
			.also { it.appendPart(content.part) }
			.toMessages()
	}
	
	private fun MessageFormatData.appendPart(part: ContentPart)
	{
		when (part)
		{
			is BoldPart -> appendPart(part)
			is InlineMonospacedPart -> appendPart(part)
			is ItalicPart -> appendPart(part)
			is MonospacedPart -> appendPart(part)
			is MultiPart -> appendPart(part)
			is QuotePart -> appendPart(part)
			is StrikeoutPart -> appendPart(part)
			is TagPart -> appendPart(part)
			is TextPart -> appendPart(part)
			is UrlPart -> appendPart(part)
			else -> appendText(part.simpleText)
		}
	}
	
	private fun MessageFormatData.appendPart(part: BoldPart)
	{
		withSurrounding("**", "**") {
			appendPart(part.inner)
		}
	}
	
	private fun MessageFormatData.appendPart(part: InlineMonospacedPart)
	{
		withSurrounding("`", "`") {
			appendMonoText(part.text)
		}
	}
	
	private fun MessageFormatData.appendPart(part: ItalicPart)
	{
		withSurrounding("*", "*") {
			appendPart(part.inner)
		}
	}
	
	private fun MessageFormatData.appendPart(part: MonospacedPart)
	{
		withMonoBlock(part.text)
	}
	
	private fun MessageFormatData.appendPart(part: MultiPart)
	{
		part.parts.forEach {
			appendPart(it)
		}
	}
	
	private fun MessageFormatData.appendPart(part: QuotePart)
	{
		withQuote {
			appendPart(part.inner)
		}
	}
	
	private fun MessageFormatData.appendPart(part: StrikeoutPart)
	{
		withSurrounding("---", "---") {
			appendPart(part.inner)
		}
	}
	
	private fun MessageFormatData.appendPart(part: TagPart)
	{
		withSurrounding("[tag:", "]") {
			appendText(part.name) // todo escape properly
		}
	}
	
	private fun MessageFormatData.appendPart(part: TextPart)
	{
		appendText(part.text)
	}
	
	private fun MessageFormatData.appendPart(part: UrlPart)
	{
		// [---google---](https://google.com/ "title")
		withSurrounding("[", "]") {
			appendPart(part.text)
		}
		withSurrounding("(", ")") {
			appendHrefText(part.href)
			
			part.title?.also { title ->
				withSurrounding(" \"", "\"") {
					appendPart(title)
				}
			}
		}
	}
	
	private fun MessageFormatData.appendText(text: String) =
		append(escape(text))
	
	private fun MessageFormatData.appendMonoText(text: String) =
		append(escapeMono(text))
	
	private fun MessageFormatData.appendHrefText(text: String) =
		append(escapeHref(text))
	
	private fun escape(text: String): String =
		text
			.replace("\\", "\\\\")
			.replace("*", "\\*")
	
	private fun escapeMono(text: String): String =
		text
			.replace("`", "\\`")
	
	private fun escapeHref(text: String): String =
		text
			.replace("[", "\\[")
			.replace("]", "\\]")
}
