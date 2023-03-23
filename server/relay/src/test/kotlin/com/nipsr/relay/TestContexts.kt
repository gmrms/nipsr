package com.nipsr.relay

import com.nipsr.payload.model.Filter
import com.nipsr.relay.model.SessionData
import com.nipsr.relay.model.SessionInfo
import com.nipsr.relay.model.SessionsContext
import com.nipsr.relay.model.Subscription
import io.mockk.mockk
import java.util.UUID
import javax.websocket.Session

object TestContexts {

    val defaultSessionsContext = SessionsContext(
        currentSession = SessionData(
            session = mockk(relaxed = true),
            info = SessionInfo(
                hashSetOf(
                    Subscription(UUID.randomUUID().toString(), listOf(Filter(kinds = listOf(1, 2)))),
                )
            )
        ),
    )

}