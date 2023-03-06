package com.nipsr.relay.controller

import com.nipsr.payload.ObjectMapperUtils.objectMapper
import com.nipsr.payload.nips.NIP_01
import com.nipsr.payload.nips.NIP_20
import com.nipsr.relay.exeptions.EventErrorException
import com.nipsr.relay.exeptions.RelayException
import com.nipsr.relay.handlers.spec.MessageHandler
import com.nipsr.relay.message.MessageType
import com.nipsr.relay.message.SessionMessageExtension.asNotice
import com.nipsr.relay.message.SessionMessageExtension.send
import com.nipsr.relay.message.SessionMessageExtension.sendResult
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

@NIP_01
@ApplicationScoped
@ServerEndpoint("/")
class WebSocketController(
    messageHandlers: Instance<MessageHandler>
) {

    private val messageReader = objectMapper.readerForListOf(Any::class.java)
    private val handlersGroupedByMessageType = messageHandlers.groupBy { it.handlesType() }

    private val sessions = ConcurrentHashMap<Session, SessionInfo>()

    @OnOpen
    fun onOpen(session: Session) {
        sessions[session] = SessionInfo()
    }

    @OnClose
    fun onClose(session: Session) {
        sessions.remove(session)
    }

    @OnError
    @NIP_20
    fun onError(session: Session, throwable: Throwable) {
        if(throwable is EventErrorException){
            session.sendResult(throwable)
        } else {
            session.send(throwable.asNotice())
        }
    }

    @OnMessage
    @Counted(name = "GLOBAL-totalMessages", unit = MetricUnits.HOURS)
    @Timed(name = "GLOBAL-messageProcessingDuration", unit = MetricUnits.MILLISECONDS)
    fun onMessage(session: Session, message: String) = runBlocking {
        val event = messageReader.readValue<List<Any>>(message)
        val messageType = MessageType.valueOf(event[0] as String)
        val handlers = handlersGroupedByMessageType[messageType] ?: return@runBlocking
        for(handler in handlers){
            handler.handle(
                context(sessions[session]!!, session), event
            )
        }
    }

    @Gauge(name = "GLOBAL-activeSessions", unit = MetricUnits.NONE)
    fun currentActiveSessions() = sessions.size

    fun context(info: SessionInfo, session: Session) = SessionsContext(info, session, sessions)

}
