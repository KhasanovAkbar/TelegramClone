package univ.tuit.telegramclone.message;

import univ.tuit.telegramclone.persistant.model.Message;

import java.util.List;

public interface MessageService {

    public void sendMessage(String accessToken, Long sendTo, String message, String topic);

    List<Message> getMessageHistory(Long senderId, Long receiverId);
}
