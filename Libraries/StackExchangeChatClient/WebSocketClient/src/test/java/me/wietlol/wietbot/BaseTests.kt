package me.wietlol.wietbot

import me.wietlol.unittest.loggo.assertThatAllEventIdsAreUnique
import org.junit.Test

class BaseTests : LocalTestModule()
{
	@Test
	fun `assert that all event ids in the code are unique`() = unitTest {
		assertThatAllEventIdsAreUnique()
	}
}
