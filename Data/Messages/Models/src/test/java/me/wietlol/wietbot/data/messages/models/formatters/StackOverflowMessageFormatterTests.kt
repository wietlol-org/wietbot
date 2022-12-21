package me.wietlol.wietbot.data.messages.models.formatters

import me.wietlol.unittest.core.models.TestCase
import me.wietlol.wietbot.data.messages.models.LocalTestModule
import me.wietlol.wietbot.data.messages.models.dsl.ContentBuilder
import me.wietlol.wietbot.data.messages.models.models.Content
import org.junit.Test

class StackOverflowMessageFormatterTests : LocalTestModule()
{
	@Test
	fun `assert that formatting works`() = unitTest	{
		execute(sequenceOf(
			ContentBuilder.content {
				text("Hello, World!")
			} to listOf("Hello, World!"),
			
			ContentBuilder.content {
				boldPart {
					text("bold")
				}
			} to listOf("**bold**"),
			
			ContentBuilder.content {
				strikeoutPart {
					boldPart {
						text("bold and strikeout")
					}
				}
			} to listOf("---**bold and strikeout**---"),
			
			ContentBuilder.content {
				strikeoutPart {
					boldPart {
						italicPart {
							text("bold, strikeout and italic")
						}
						quotePart {
							text("quote")
						}
						text("still bold and strikeout")
					}
				}
			} to listOf(
				"---***bold, strikeout and italic***---",
				"> ---**quote**---",
				"---**still bold and strikeout**---",
			),
		))
	}
	
	@Test
	fun `assert that escaping works properly`() = unitTest	{
		execute(sequenceOf(
			ContentBuilder.content {
				text("¯\\_(ツ)_/¯")
			} to listOf("¯\\\\_(ツ)_/¯"),
			
			ContentBuilder.content {
				monoBlock("¯\\_(ツ)_/¯")
			} to listOf("    ¯\\_(ツ)_/¯"),
			
			ContentBuilder.content {
				text("**")
			} to listOf("\\*\\*"),
			
			ContentBuilder.content {
				mono("**")
			} to listOf("`**`"),
		))
	}
	// \\title\\*\\* = \\title\*\*
	
	private fun TestCase.execute(tests: Sequence<Pair<Content, List<String>>>)
	{
		val service = StackOverflowMessageFormatter
		
		tests.forEach { (input, expected) ->
			val actual = service.format(input)
			
			assertThat(actual).isEqualTo(expected)
		}
	}
}
