package univ.tuit.telegramclone.kafka.producer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import univ.tuit.telegramclone.kafka.constants.KafkaConstants;
import univ.tuit.telegramclone.persistant.model.Message;

import java.time.LocalDateTime;

@Service
public class JsonKafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaProducer.class);

    private final KafkaTemplate<String, Message> kafkaTemplate;

    public JsonKafkaProducer(KafkaTemplate<String, Message> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Message data){

        LOGGER.info(data.toString());
        data.setCreatedAt(LocalDateTime.now().toString());

        org.springframework.messaging.Message<Message> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, KafkaConstants.KAFKA_JSON_TOPIC)
                .build();

        kafkaTemplate.send(message);
    }
}
