package com.nipsr.relay.filters

import com.nipsr.payload.model.events.Event
import com.nipsr.payload.nips.NIP_22
import com.nipsr.relay.config.NipsrRelaySettings
import com.nipsr.relay.filters.system.CreatedAtEventFilter
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@NIP_22
class CreatedAtEventFilterTest {

    val maxCreatedAtDriftMinutes = 5

    val event = mockk<Event<*>>(relaxed = true)
    val settings = mockk<NipsrRelaySettings>(relaxed = true)

    val createdAtEventFilter = spyk(CreatedAtEventFilter(settings))

    @Test
    fun `should allow events with created_at within params`(){
        every { event.created_at } returns System.currentTimeMillis()
        val (result, _) = createdAtEventFilter.filter(event)
        assertTrue(result)
    }

    @Test
    fun `should not allow events with created_at outside params`(){
        every { event.created_at } returns System.currentTimeMillis() - 1000 * 60 * (maxCreatedAtDriftMinutes + 1)
        val (result, _) = createdAtEventFilter.filter(event)
        assertFalse(result)
    }

}