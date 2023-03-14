package com.nipsr.relay.validation

import com.nipsr.payload.model.events.DeletionEvent
import com.nipsr.payload.model.events.Tag
import com.nipsr.payload.nips.NIP_09
import com.nipsr.relay.validation.validators.DeletionValidator
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

@NIP_09
class DeletionValidatorTest {

    val deletionValidator = spyk<DeletionValidator>()

    val validDeletionEvent = mockk<DeletionEvent>(relaxed = true) {
        every { tags } returns arrayListOf(
            Tag.fromList(arrayListOf("e", "id1")),
            Tag.fromList(arrayListOf("e", "id2")),
        )
    }

    val invalidDeletionEvent = mockk<DeletionEvent>(relaxed = true) {
        every { tags } returns arrayListOf()
    }

    @Test
    fun `should allow deletion event with event ids`(){
        val result = deletionValidator.validate(validDeletionEvent)
        assertTrue(result)
    }

    @Test
    fun `should not allow deletion event with no event id`(){
        val result = deletionValidator.validate(invalidDeletionEvent)
        assertFalse(result)
    }

}