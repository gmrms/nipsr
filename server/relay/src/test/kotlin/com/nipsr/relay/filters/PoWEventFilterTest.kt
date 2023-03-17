package com.nipsr.relay.filters

import com.nipsr.payload.Constants.NONCE_TAG
import com.nipsr.payload.model.events.Event
import com.nipsr.payload.model.events.Tag
import com.nipsr.payload.nips.NIP_13
import com.nipsr.relay.filters.events.global.PoWEventFilter
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

@NIP_13
@OptIn(ExperimentalCoroutinesApi::class)
class PoWEventFilterTest {

    val poWEventFilter = spyk<PoWEventFilter>()

    val difficulty = 36

    val poWTag = mockk<Tag>(relaxed = true) {
        every { tag } returns NONCE_TAG
        every { value } returns "0"
        every { options } returns arrayListOf(difficulty.toString())
    }

    val event = mockk<Event<*>>(relaxed = true) {
        every { tags } returns arrayListOf(poWTag)
    }

    @Test
    fun `should allow event that difficulty matches target`() = runTest {
        // given
        every { event.id } returns "000000000e9d97a1ab09fc381030b346cdd7a142ad57e6df0b46dc9bef6c7e2d"
        // when
        val (result, _) = poWEventFilter.filter(event)
        // then
        assertTrue(result)
    }

    @Test
    fun `should not allow event that difficulty is greater than target`() = runTest {
        // given
        every { event.id } returns "00000000009d97a1ab09fc381030b346cdd7a142ad57e6df0b46dc9bef6c7e2d"
        // when
        val (result, _) = poWEventFilter.filter(event)
        // then
        assertFalse(result)
    }

    @Test
    fun `should not allow event that difficulty is lower than target`() = runTest {
        // given
        every { event.id } returns "00000000ee9d97a1ab09fc381030b346cdd7a142ad57e6df0b46dc9bef6c7e2d"
        // when
        val (result, _) = poWEventFilter.filter(event)
        // then
        assertFalse(result)
    }

}