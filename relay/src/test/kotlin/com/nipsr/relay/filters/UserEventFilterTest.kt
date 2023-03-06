package com.nipsr.relay.filters

import com.nipsr.payload.model.Filter
import com.nipsr.payload.nips.NIP_01
import com.nipsr.relay.TestEvents
import com.nipsr.relay.filters.user.UserEventFilter
import org.junit.jupiter.api.Test

@NIP_01
class UserEventFilterTest {

    val events = TestEvents.validEvents

    @Test
    fun `should allow filters of pubkey and id by prefix`() {
        val ids = events.map { it.id.substring(0, 24) }
        val authors = events.map { it.pubkey.substring(0, 24) }
        val filter = Filter(
            ids = ids.toList(),
            authors = authors.toList(),
        )
        for(event in events) {
            assert(UserEventFilter(filter).filter(event).first)
        }
    }

    @Test
    fun `should not allow filters of pubkey and id by prefixes that do not exist`() {
        val ids = events.map { it.id.substring(24) }
        val authors = events.map { it.pubkey.substring(24) }
        val filter = Filter(
            ids = ids.toList(),
            authors = authors.toList(),
        )
        for(event in events) {
            assert(!UserEventFilter(filter).filter(event).first)
        }
    }

    @Test
    fun `should filter events by kinds`() {
        val kinds = arrayListOf(1, 2, 3)
        for(kind in kinds) {
            val filter = Filter(
                kinds = listOf(kind)
            )
            for(event in events) {
                if(event.kind == kind) {
                    assert(UserEventFilter(filter).filter(event).first)
                } else {
                    assert(!UserEventFilter(filter).filter(event).first)
                }
            }
        }
    }

    @Test
    fun `should filter events by tags`(){
        val filter = Filter(
            tags = arrayListOf(
                arrayListOf("p", "13409737c571a3fc923faa407935287e440b1d5a1f64a75c87bed37a4f1b74ab"),
                arrayListOf("t", "#03", "#test")
            )
        )
        var count = 0
        for(event in events) {
            if(UserEventFilter(filter).filter(event).first) {
                count++
            }
        }
        assert(count == 2)
    }

    @Test
    fun `should filter events with multiple filters`(){
        val filters = arrayListOf(
            Filter(
                since = 10000,
                until = Long.MAX_VALUE,
            ),
            Filter(
                ids = arrayListOf("non existent")
            )
        )
        var count = 0
        for(event in events) {
            if(UserEventFilter(filters).filter(event).first) {
                count++
            }
        }
        assert(count == events.size)
    }

}