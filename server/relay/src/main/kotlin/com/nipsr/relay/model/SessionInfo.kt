package com.nipsr.relay.model

data class SessionInfo(
    var subscriptions: HashSet<Subscription> = HashSet()
)