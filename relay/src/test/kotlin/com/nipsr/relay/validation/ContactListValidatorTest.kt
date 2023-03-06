package com.nipsr.relay.validation

import com.nipsr.payload.model.events.ContactListEvent
import com.nipsr.payload.model.events.Tag
import com.nipsr.payload.nips.NIP_02
import com.nipsr.relay.validation.validators.ContactListValidator
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

@NIP_02
class ContactListValidatorTest {

    val contactListvalidator = spyk<ContactListValidator>()

    val validContactListEvent = mockk<ContactListEvent>(relaxed = true) {
        every { tags } returns arrayListOf(
            Tag.fromList(arrayListOf("p", "pubkey", "wss://brb.io", "juye")),
        )
    }

    val invalidContactListEvent = mockk<ContactListEvent>(relaxed = true) {
        every { tags } returns arrayListOf()
    }

    @Test
    fun `should allow contact_lists event with contacts`(){
        val result = contactListvalidator.validate(validContactListEvent)
        assertTrue(result)
    }

    @Test
    fun `should not allow contact_lists event with no contacts`(){
        val result = contactListvalidator.validate(invalidContactListEvent)
        assertFalse(result)
    }

}