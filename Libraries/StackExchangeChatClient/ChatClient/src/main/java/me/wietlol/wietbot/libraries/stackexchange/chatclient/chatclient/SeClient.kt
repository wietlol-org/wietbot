package me.wietlol.wietbot.libraries.stackexchange.chatclient.chatclient

import java.net.http.HttpResponse

interface SeClient
{
	fun login(credentials: SeCredentials): String
	
	fun get(url: String, headers: Map<String, String> = emptyMap()): HttpResponse<String>
	
	fun post(url: String, values: List<Pair<String, String>>, headers: Map<String, String> = emptyMap()): HttpResponse<String>
}
