package univ.tuit.telegramclone.persistant.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "messages")
public class Message implements Serializable {

    // private Long senderId; //userName
    // private Long receiverId; //userName
    //  private String replyMessageId;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "message_id")
    private Long messageId;
    private String topic;
    private Long senderId;
    private Long receiverId;
    private String message;
    private Date sentAt;
    //   private String updatedAt;
    //  private String deletedAt;
    //  private Status status;




    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", topic='" + topic + '\'' +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", message='" + message + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
