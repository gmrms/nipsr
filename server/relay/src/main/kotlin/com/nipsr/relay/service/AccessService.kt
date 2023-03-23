package com.nipsr.relay.service

import com.nipsr.relay.model.Access
import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.time.Duration
import javax.enterprise.context.ApplicationScoped
import org.slf4j.LoggerFactory

@ApplicationScoped
class AccessService(
    redisDataSource: ReactiveRedisDataSource
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val keyCommands = redisDataSource.key()
    private val valueCommands = redisDataSource.value(String::class.java)

    suspend fun findAll() = Access.listAll().awaitSuspending()

    suspend fun hasAccess(pubkey: String) = existsOnCache(pubkey) || findAndValidate(pubkey)

    suspend fun revoke(pubkey: String) {
        Access.deleteById(pubkey).awaitSuspending()
        keyCommands.del(pubkey).awaitSuspending()
    }

    suspend fun grantAccess(pubkey: String, expiration: Long) {
        grantAccess(
            Access().apply {
                this.pubkey = pubkey
                this.expiration = expiration
            }
        )
    }

    suspend fun grantAccess(access: Access) {
        access.persistOrUpdate<Access>().awaitSuspending()
        addsToCache(access.pubkey)
    }

    private suspend fun findAndValidate(pubkey: String): Boolean {
        val access = Access.findById(pubkey).awaitSuspending()
        if(access != null){
            if(access.expiration < System.currentTimeMillis() / 1000){
                revoke(pubkey)
                return false
            }
            addsToCache(pubkey)
            return true
        }
        return false
    }

    private suspend fun addsToCache(pubkey: String) {
        valueCommands.set(pubkey, "").awaitSuspending()
        keyCommands.pexpire(pubkey, Duration.ofHours(1)).awaitSuspending()
        logger.debug("Added $pubkey to cache")
    }

    private suspend fun existsOnCache(pubkey: String): Boolean {
        return keyCommands.exists(pubkey).awaitSuspending().also {
            if(it){
                logger.debug("Cache hit for $pubkey")
            } else {
                logger.debug("Cache miss for $pubkey")
            }
        }
    }

}