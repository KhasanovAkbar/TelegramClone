package univ.tuit.telegramclone.webSocket;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import univ.tuit.telegramclone.cache.repository.CacheService;
import univ.tuit.telegramclone.message.broker.MessageSender;
import univ.tuit.telegramclone.message.broker.constants.KafkaConstants;
import univ.tuit.telegramclone.persistant.model.User;
import univ.tuit.telegramclone.repository.UserRepository;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);
    private final CacheService cacheService;
    private final UserRepository userRepository;
    private final MessageHandler messageHandler;
    private final MessageSender messageSender;

    public WebSocketHandler(CacheService cacheService, UserRepository userRepository, MessageHandler messageHandler, MessageSender messageSender) {
        this.cacheService = cacheService;
        this.userRepository = userRepository;
        this.messageHandler = messageHandler;
        this.messageSender = messageSender;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String[] parameters = session.getUri().getQuery().split("=");

        LOGGER.info("WS connection requested");

        if (parameters.length == 2 && parameters[0].equals("accessToken")) {
            String accessToken = parameters[1];

            Long senderUserId = 0L;
            String senderId = cacheService.getUserIdByAccessToken(accessToken);

            if (senderId == null) {
                User sender = userRepository.findByToken(accessToken);
                if (sender != null) {
                    senderUserId = sender.getUserId();
                }
            } else {
                senderUserId = Long.valueOf(senderId);
            }

            LOGGER.info("Websocket connected, userId: " + senderUserId);

            if (senderUserId == 0L) {
                LOGGER.info("Websocket connection: user not found for given accessToken");
                return;
            }

            messageHandler.addSessionToPool(senderUserId, session);
        } else {
            LOGGER.info("Websocket connection: accessToken not provided");
            session.close();
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        String[] parameters = session.getUri().getQuery().split("=");

        LOGGER.info("ws session url " + session.getUri().getPath() + " ? " + session.getUri().getQuery());

        if (parameters.length == 2 && parameters[0].equals("accessToken")) {
            String accessToken = parameters[1];

            Long senderUserId = 0L;
            String senderId = cacheService.getUserIdByAccessToken(accessToken);

            if (senderId == null) {
                User sender = userRepository.findByToken(accessToken);
                if (sender != null) {
                    senderUserId = sender.getUserId();
                }
            } else {
                senderUserId = Long.valueOf(senderId);
            }

            LOGGER.info("WS connected, userId: " + senderUserId + " -- " + senderId);

            if (senderUserId == 0L) {
                LOGGER.info("Websocket connection: user not found for given accessToken");
                return;
            }
            messageHandler.removeFromSessionToPool(senderUserId, session);
        }
    }



    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {

        JSONObject jsonObject = new JSONObject(textMessage.getPayload());
        LOGGER.info("handleTextMessage(): "+textMessage.getPayload());
        String topic = jsonObject.getString("topic");
        JSONObject message = jsonObject.getJSONObject("msg");

        if (topic == null && !topic.equals(KafkaConstants.KAFKA_TOPIC)) {
            return;
        }

        messageSender.send(topic, message.toString());
    }

}
