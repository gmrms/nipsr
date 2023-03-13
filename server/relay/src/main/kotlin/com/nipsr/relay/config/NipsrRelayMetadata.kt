package com.nipsr.relay.config

import com.nipsr.relay.model.RelayMetadata
import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "nipsr.relay")
interface NipsrRelayMetadata {

    fun name(): String
    fun description(): String
    fun pubkey(): String
    fun contact(): String
    fun supportedNips(): List<Int>
    fun software(): String
    fun version(): String

    fun getRelayMetadata(): RelayMetadata {
        return RelayMetadata(
            name = name(),
            description = description(),
            pubkey = pubkey(),
            contact = contact(),
            supported_nips = supportedNips(),
            software = software(),
            version = version()
        )
    }

}