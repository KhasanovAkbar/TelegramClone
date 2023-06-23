package univ.tuit.telegramclone.message.broker;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import univ.tuit.telegramclone.message.MessageService;
import univ.tuit.telegramclone.message.broker.constants.KafkaConstants;
import univ.tuit.telegramclone.webSocket.WebSocketPool;

@Service
public class MessageReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiver.class);
    private final MessageService messageService;

    public MessageReceiver(MessageService messageService) {
        this.messageService = messageService;
    }

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void messagesSendToUser(@Payload String message, @Headers MessageHeaders headers) {

        JSONObject jsonObject = new JSONObject(message);
        LOGGER.info("Websocket message will be sent if corresponding destination websocket session is found");
        if (jsonObject.get("receiverId") != null
                && WebSocketPool.websockets.get(jsonObject.getLong("receiverId")) != null
                && WebSocketPool.websockets.get(jsonObject.getLong("receiverId")).size() > 0) {

            String accessToken = jsonObject.getString("accessToken");
            long receiverId = jsonObject.getLong("receiverId");
            String msg = jsonObject.getString("msg");

            LOGGER.info("Websocket message is sent to " + receiverId);

            messageService.sendMessage(accessToken, receiverId, msg, KafkaConstants.KAFKA_TOPIC);
        } else {
            LOGGER.info("Websocket session not found for given receiverId");
        }
    }
}
