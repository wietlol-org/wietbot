package me.wietlol.wietbot.libraries.stackexchange.chatclient.chatclient

import java.net.CookieManager
import java.net.CookiePolicy
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpClient.Redirect
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublisher
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.nio.charset.StandardCharsets
import java.time.Duration

class HttpSeClient : SeClient
{
	companion object
	{
		val mainSiteUrl = "https://stackoverflow.com"
		val chatSiteUrl = "https://chat.stackoverflow.com"
	}
	
	private val cookieHandler = CookieManager(null, CookiePolicy.ACCEPT_ALL)
	
	private val client: HttpClient = HttpClient.newBuilder()
		.version(HttpClient.Version.HTTP_1_1)
		.followRedirects(Redirect.NORMAL)
		.connectTimeout(Duration.ofSeconds(20))
		.cookieHandler(cookieHandler)
		.build()
	
	/**
	 * @return account FKey
	 */
	override fun login(credentials: SeCredentials): String
	{
		val mainFKey = getMainFKey()
		
		val response = post("$mainSiteUrl/users/login", listOf(
			"fkey" to mainFKey,
			"email" to credentials.emailAddress,
			"password" to credentials.password
		))
		
		response.body()
			.apply {
				if (contains("The email or password is incorrect."))
					throw UnsuccessfulAuthenticationException("The email or password is incorrect.")
			}
			.apply {
				if (contains("<title>Human verification - Stack Overflow</title>"))
				{
					println(this)
					throw LackOfHumanityException("Human verification is required.")
				}
			}
		
		val cookieJar = cookieHandler.cookieStore.get(URI("$mainSiteUrl/"))
			.associateBy { it.name }
		if (cookieJar.containsKey("uauth").not())
			throw UnexpectedSituationException("unable to login for unknown reasons")
		
		return getAccountFKey()
	}
	
	override fun get(url: String, headers: Map<String, String>): HttpResponse<String>
	{
		val request = HttpRequest.newBuilder()
			.GET()
			.uri(URI.create(url))
			.header("Content-Type", "application/x-www-form-urlencoded")
			.apply {
				headers.forEach {
					header(it.key, it.value)
				}
			}
			.build()
		
		return client.send(request, BodyHandlers.ofString())
	}
	
	override fun post(url: String, values: List<Pair<String, String>>, headers: Map<String, String>): HttpResponse<String>
	{
		val request = HttpRequest.newBuilder()
			.POST(formUrlEncoded(values))
			.uri(URI.create(url))
			.header("Content-Type", "application/x-www-form-urlencoded")
			.apply {
				headers.forEach {
					header(it.key, it.value)
				}
			}
			.build()
		
		return client.send(request, BodyHandlers.ofString())
	}
	
	private fun formUrlEncoded(values: List<Pair<String, String>>): BodyPublisher
	{
		val charset = StandardCharsets.UTF_8
		return BodyPublishers.ofString(
			values.joinToString("&") {
				"${URLEncoder.encode(it.first, charset)}=${URLEncoder.encode(it.second, charset)}"
			}
		)
	}
	
	private fun getMainFKey(): String
	{
		val response = get("$mainSiteUrl/users/login")
		
		return response.body().extractFKey()
	}
	
	private fun getAccountFKey(): String
	{
		val response = get(chatSiteUrl)
		
		return response.body().extractFKey()
	}
	
	private fun String.extractFKey(): String =
		sequenceOf(
			"<input type=\"hidden\" name=\"fkey\" value=\"",
			"<input id=\"fkey\" name=\"fkey\" type=\"hidden\" value=\""
		)
			.map {
				val head = indexOf(it)
				if (head > 0)
				{
					val start = head + it.length
					val end = indexOf("\"", start)
					substring(start, end)
				}
				else
					null
			}
			.filterNotNull()
			.firstOrNull()
			?: throw UnexpectedSituationException("there is no fkey for unknown reasons")
}
