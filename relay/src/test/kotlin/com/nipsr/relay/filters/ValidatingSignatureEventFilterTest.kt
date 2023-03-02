package com.nipsr.relay.filters

import com.nipsr.payload.nips.NIP_01
import com.nipsr.relay.TestEvents
import com.nipsr.relay.filters.system.ValidatingSignatureEventFilter
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ValidatingSignatureEventFilterTest {

    val validatingSignatureEventFilter = spyk<ValidatingSignatureEventFilter>()

    @Test
    @NIP_01
    fun `should allow events with valid signatures`(){
        val toTest = TestEvents.validEvents.first()
        val result = validatingSignatureEventFilter.filter(toTest)
        assertTrue(result)
    }

    @Test
    @NIP_01
    fun `should not allow events with invalid signatures`(){
        val toTest = TestEvents.invalidEvent
        val result = validatingSignatureEventFilter.filter(toTest)
        assertFalse(result)
    }

}