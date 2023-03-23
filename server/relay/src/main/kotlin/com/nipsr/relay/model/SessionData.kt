package com.nipsr.relay.model

import javax.websocket.Session

data class SessionData(
    var session: Session,
    var info: SessionInfo
)