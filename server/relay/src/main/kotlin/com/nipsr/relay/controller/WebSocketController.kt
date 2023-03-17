package com.nipsr.relay.controller

import com.nipsr.payload.ObjectMapperUtils.objectMapper
import com.nipsr.payload.nips.NIP_01
import com.nipsr.payload.nips.NIP_20
import com.nipsr.relay.config.NipsrRelaySettings
import com.nipsr.relay.exeptions.EventErrorException
import com.nipsr.relay.exeptions.MessageException
import com.nipsr.relay.exeptions.TerminateConnectionException
import com.nipsr.relay.filters.messages.MessageFilter
import com.nipsr.relay.handlers.spec.MessageHandler
import com.nipsr.relay.message.Message
import com.nipsr.relay.message.MessageType
import com.nipsr.relay.message.SessionMessageExtension.asNotice
import com.nipsr.relay.message.SessionMessageExtension.send
import com.nipsr.relay.message.SessionMessageExtension.sendResult
import com.nipsr.relay.model.SessionData
import com.nipsr.relay.model.SessionInfo
import com.nipsr.relay.model.SessionsContext
import java.util.concurrent.ConcurrentHashMap
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Instance
import javax.websocket.OnClose
import javax.websocket.OnError
import javax.websocket.OnMessage
import javax.websocket.OnOpen
import javax.websocket.Session
import javax.websocket.server.ServerEndpoint
import kotlinx.coroutines.runBlocking
import org.eclipse.microprofile.metrics.MetricUnits
import org.eclipse.microprofile.metrics.annotation.Counted
import org.eclipse.microprofile.metrics.annotation.Gauge
import org.eclipse.microprofile.metrics.annotation.Timed
import org.slf4j.LoggerFactory

@NIP_01
@ApplicationScoped
@ServerEndpoint("/")
class WebSocketController(
    private val settings: NipsrRelaySettings,
    private val messageFilters: Instance<MessageFilter>,
    messageHandlers: Instance<MessageHandler>,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val messageReader = objectMapper.readerForListOf(Any::class.java)
    private val handlersGroupedByMessageType = messageHandlers.groupBy { it.handlesType() }

    private val sessions = ConcurrentHashMap<Session, SessionInfo>()

    @OnOpen
    fun onOpen(session: Session) {
        if(sessions.size >= settings.maxConnections()){
            session.send(Message(MessageType.NOTICE, "Connections are full, please try again later"))
            session.close()
            return
        }
        sessions[session] = SessionInfo()
    }

    @OnClose
    fun onClose(session: Session) {
        sessions.remove(session)
        session.close()
    }

    @NIP_20
    @OnError
    @Counted(name = "GLOBAL-totalErrors", unit = MetricUnits.HOURS)
    fun onError(session: Session, throwable: Throwable) {
        when(throwable){
            is TerminateConnectionException -> session.close()
            is EventErrorException -> session.sendResult(throwable)
            is MessageException -> session.send(throwable.asNotice())
            else -> {
                logger.error("Error while processing message", throwable)
                session.send(throwable.asNotice())
            }
        }
    }

    @OnMessage
    @Counted(name = "GLOBAL-totalMessages", unit = MetricUnits.HOURS)
    @Timed(name = "GLOBAL-messageProcessingDuration", unit = MetricUnits.MILLISECONDS)
    fun onMessage(session: Session, raw: String) = runBlocking {
        val message = messageReader.readValue<List<Any>>(raw)
        val messageType = MessageType.valueOf(message[0] as String)
        val handlers = handlersGroupedByMessageType[messageType] ?: return@runBlocking
        val context = context(sessions[session]!!, session)
        for(filter in messageFilters){
            if(filter.handlesType(messageType)){
                filter.filter(Message(message), context)
            }
        }
        for(handler in handlers){
            handler.handle(context, message)
        }
    }

    @Gauge(name = "GLOBAL-activeSessions", unit = MetricUnits.NONE)
    fun currentActiveSessions() = sessions.size

    fun context(info: SessionInfo, session: Session) = SessionsContext(
        SessionData(session, info),
        sessions
    )

}