package com.nipsr.relay.model

import javax.websocket.Session

data class SessionsContext(
    var currentSession: SessionData,
    var sessions: MutableMap<Session, SessionInfo> = mutableMapOf()
)