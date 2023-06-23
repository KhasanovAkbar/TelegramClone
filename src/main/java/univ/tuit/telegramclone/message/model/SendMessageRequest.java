package univ.tuit.telegramclone.message.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {

    private String accessToken;

    private Long receiverId;

    private String msg;
}
