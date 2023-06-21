package univ.tuit.telegramclone.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import univ.tuit.telegramclone.kafka.constants.KafkaConstants;
import univ.tuit.telegramclone.persistant.model.Message;

@Component
public class MessageListener {

    @Autowired
    SimpMessagingTemplate template;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void listener(Message message) {
        System.out.println("sending via kafka listener..");
        LOGGER.info(message.toString());
        template.convertAndSend("/topic/group", message);
    }
}
