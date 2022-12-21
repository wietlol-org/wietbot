package me.wietlol.wietbot.data.messages.models.parsers

import me.wietlol.utils.common.prepend
import me.wietlol.wietbot.data.messages.models.dsl.ContentBuilder
import me.wietlol.wietbot.data.messages.models.models.Content
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

class StackOverflowMessageParser
{
	fun parse(html: String): Content
	{
		val body = Jsoup.parse(html).body().childNodes()
		
		return ContentBuilder.content {
			body.forEach {
				parseNode(it)
			}
		}
	}
	
	private fun ContentBuilder.parseNode(node: Node)
	{
		when (node)
		{
			is TextNode -> text(node.wholeText)
			is Element ->
			{
				when (node.tagName())
				{
					"pre" -> monoBlock(node.ownText())
					"b" -> boldPart {
						node.childNodes().forEach { parseNode(it) }
					}
					
					"i" -> italicPart {
						node.childNodes().forEach { parseNode(it) }
					}
					
					"strike" -> strikeoutPart {
						node.childNodes().forEach { parseNode(it) }
					}
					
					"code" -> mono(node.ownText())
					"br" -> text("\n")
					"a" ->
					{
						if (node.childNodes()
								.singleOrNull()
								?.let { it as? Element }
								?.hasClass("ob-post-tag") == true
						)
						{
							tagPart((node.childNode(0).childNode(0) as TextNode).wholeText)
						}
						else
						{
							val url = node.attr("href")
								.let {
									if (it.startsWith("//"))
										it.prepend("https:")
									else
										it
								}
							urlPart(url,
								{
									node.childNodes().forEach { parseNode(it) }
								},
								node.attr("title")
									.takeIf { node.hasAttr("title") }
									?.let {
										{
											text(it)
										}
									})
						}
					}
					
					"div" ->
					{
						when (node.className())
						{
							"full" -> node.childNodes().forEach { parseNode(it) }
							"quote" -> quotePart {
								node.childNodes().forEach { parseNode(it) }
							}
							
							"onebox ob-message" -> text("https://chat.stackoverflow.com${(node.childNode(0) as Element).attr("href")}")
							"onebox ob-youtube" -> text((node.childNode(0) as Element).attr("href"))
							else -> node.childNodes().forEach { parseNode(it) }
						}
					}
					
					else -> node.childNodes().forEach { parseNode(it) }
				}
			}
			
			else -> node.childNodes().forEach { parseNode(it) }
		}
	}
}
