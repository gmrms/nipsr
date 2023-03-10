package com.nipsr.relay.filters

import com.nipsr.payload.nips.NIP_01
import com.nipsr.relay.TestEvents
import com.nipsr.relay.filters.global.ValidatingSignatureEventFilter
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@NIP_01
class ValidatingSignatureEventFilterTest {

    val validatingSignatureEventFilter = spyk<ValidatingSignatureEventFilter>()

    @Test
    fun `should allow events with valid signatures`(){
        val toTest = TestEvents.validEvents
        for (event in toTest){
            val (result, _) = validatingSignatureEventFilter.filter(event)
            assertTrue(result)
        }
    }

    @Test
    fun `should not allow events with invalid signatures`(){
        val toTest = TestEvents.invalidEvent
        val (result, _) = validatingSignatureEventFilter.filter(toTest)
        assertFalse(result)
    }

}