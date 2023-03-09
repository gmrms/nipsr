package com.nipsr.relay.config

import com.nipsr.payload.nips.NIP_22
import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "nipsr.relay.settings")
interface NipsrRelaySettings {

    fun maxConnections(): Int

    @NIP_22
    fun maxCreatedAtDriftMinutes(): Int

}