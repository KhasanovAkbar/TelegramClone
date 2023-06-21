package univ.tuit.telegramclone.persistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import univ.tuit.telegramclone.persistant.model.Message;
import univ.tuit.telegramclone.kafka.producer.JsonKafkaProducer;

@RestController
public class ChatController {


    @Autowired
    private JsonKafkaProducer jsonKafkaProducer;

    @PostMapping(value = "api/send", consumes = "application/json", produces = "application/json")
    public String sendMessage(@RequestBody Message message) {
        jsonKafkaProducer.sendMessage(message);

            return "Message sent";
    }


    @MessageMapping("/sendMessage") // /app/message
    @SendTo("/topic/group")
    public Message receivePublicMessage(@Payload Message message) {
        //Sending message to the one subscribe
        return message;
    }

    @MessageMapping("/newUser")
    @SendTo("/topic/group")
    public Message recievePrivateMessage(@Payload Message message,
                                         SimpMessageHeaderAccessor headerAccessor) {
        //Add user in web socket session
        headerAccessor.getSessionAttributes().put("username", message.getSender()); // /user/{name}/private
        return message;
    }


}
