package univ.tuit.telegramclone.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.tuit.telegramclone.entity.enums.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private Long senderId; //userName
    private Long receiverId; //userName
    private String message;
    private Long messageId;
    private String replyMessageId;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private Status status;

}
