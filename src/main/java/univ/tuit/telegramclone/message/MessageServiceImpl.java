package univ.tuit.telegramclone.message;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import univ.tuit.telegramclone.cache.repository.CacheService;
import univ.tuit.telegramclone.persistant.model.Message;
import univ.tuit.telegramclone.persistant.model.User;
import univ.tuit.telegramclone.repository.MessageRepository;
import univ.tuit.telegramclone.repository.UserRepository;
import univ.tuit.telegramclone.webSocket.MessageHandler;

import java.util.Calendar;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final CacheService cacheService;
    private final MessageHandler messageHandler;

    public MessageServiceImpl(UserRepository userRepository, MessageRepository messageRepository, CacheService cacheService, MessageHandler messageHandler) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.cacheService = cacheService;
        this.messageHandler = messageHandler;
    }

    @Override
    public void sendMessage(String accessToken, Long receiverId, String message, String topic) {

        Long senderId = 0L;
        String senderIdByAccessToken = cacheService.getUserIdByAccessToken(accessToken);

        if (senderIdByAccessToken == null) {
            User sender = userRepository.findByToken(accessToken);
            if (sender != null) {
                senderId = sender.getUserId();
            }
        } else {
            senderId = Long.valueOf(senderIdByAccessToken);
        }
        if (senderId == 0L) {
            LOGGER.info("Invalid sender " + senderId);
            return;
        }

        try {
            //enrich message with senderId and topic
            JSONObject msgJson = new JSONObject();
            msgJson.put("msg", message);
            msgJson.put("accessToken", accessToken);
            msgJson.put("senderId", senderId);
            msgJson.put("topic", topic);
            messageHandler.sendMessageToUser(receiverId, msgJson.toString());

            messageRepository.save(Message.builder()
                    .message(message)
                    .sentAt(Calendar.getInstance().getTime())
                    .topic(topic)
                    .receiverId(receiverId)
                    .senderId(senderId)
                    .build());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Message> getMessageHistory(Long senderId, Long receiverId) {
        return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId);
    }
}
