package com.nipsr.relay.config

import com.nipsr.payload.nips.NIP_13
import com.nipsr.payload.nips.NIP_22
import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "nipsr.relay.settings")
interface NipsrRelaySettings {

    @NIP_22
    fun maxCreatedAtDriftMinutes(): Int
    @NIP_13
    fun minPowRequired(): Int

    fun requireAuthentication(): Boolean
    fun maxConnections(): Int
    fun grantAccessNip05(): Boolean
    fun private(): NipsrPrivateRelaySettings

    interface NipsrPrivateRelaySettings {
        fun enabled(): Boolean
        fun allowPublicReads(): Boolean
    }

}