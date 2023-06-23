package univ.tuit.telegramclone.message.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import univ.tuit.telegramclone.message.broker.MessageSender;
import univ.tuit.telegramclone.message.broker.constants.KafkaConstants;
import univ.tuit.telegramclone.message.model.SendMessageRequest;
import univ.tuit.telegramclone.message.model.SendMessageResponse;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageSender messageSender;

    public MessageController(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @RequestMapping(value = "/send-message", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sendMessage(@RequestBody SendMessageRequest sendMessageRequest) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            messageSender.send(KafkaConstants.KAFKA_TOPIC, mapper.writeValueAsString(sendMessageRequest));
            SendMessageResponse.builder()
                    .message("Message sent successfully")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(
                "Message sent to Kafka topic " + KafkaConstants.KAFKA_TOPIC,
                HttpStatus.OK
        );
    }
}
