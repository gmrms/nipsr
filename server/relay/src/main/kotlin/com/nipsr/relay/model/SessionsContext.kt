package com.nipsr.relay.model

import javax.websocket.Session

data class SessionsContext(
    var currentSessionInfo: SessionInfo,
    var currentSession: Session,
    var sessions: MutableMap<Session, SessionInfo> = mutableMapOf()
)