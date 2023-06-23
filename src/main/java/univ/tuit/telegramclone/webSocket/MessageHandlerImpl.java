package univ.tuit.telegramclone.webSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class MessageHandlerImpl implements MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandlerImpl.class);

    @Override
    public void addSessionToPool(Long userId, WebSocketSession session) {

        Set<WebSocketSession> userSessions = WebSocketPool.websockets.get(userId);

        if (userSessions != null) {
            userSessions.add(session);
            WebSocketPool.websockets.put(userId, userSessions);
        } else {
            Set<WebSocketSession> newUserSessions = new HashSet<>();
            newUserSessions.add(session);
            WebSocketPool.websockets.put(userId, newUserSessions);
        }
    }

    @Override
    public void sendMessageToUser(Long userId, String message) throws IOException {

        Set<WebSocketSession> userSessions = WebSocketPool.websockets.get(userId);

        if (userSessions == null) {
            LOGGER.info("No websocket session found for given destination userId");
            return;
        }

        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession session : userSessions) {
            LOGGER.info("Sending websocket message " + textMessage.getPayload());
            session.sendMessage(textMessage);
        }
    }

    @Override
    public void removeFromSessionToPool(Long userId, WebSocketSession session) {

        Set<WebSocketSession> userSessions = WebSocketPool.websockets.get(userId);

        if (userSessions != null) {
            LOGGER.info("Session found for userId " + userId);
            for (WebSocketSession sessionItem : userSessions) {
                if (sessionItem.equals(session)) {
                    LOGGER.info("Removing session for userId " + sessionItem.hashCode());
                    userSessions.remove(session);
                } else {
                    LOGGER.info("This session is not equal: " + sessionItem.hashCode() + " <> " + session.hashCode());
                }
            }
        }
        WebSocketPool.websockets.put(userId, userSessions);
    }
}
