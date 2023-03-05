package com.nipsr.relay

import com.nipsr.payload.model.Filter
import com.nipsr.relay.model.SessionInfo
import com.nipsr.relay.model.SessionsContext
import com.nipsr.relay.model.Subscription
import io.mockk.mockk
import java.util.UUID

object TestContexts {

    val defaultSessionsContext = SessionsContext(
        currentSessionInfo = SessionInfo(
            hashSetOf(
                Subscription(UUID.randomUUID().toString(), listOf(Filter(kinds = listOf(1, 2)))),
            )
        ),
        currentSession = mockk(relaxed = true)
    )

}