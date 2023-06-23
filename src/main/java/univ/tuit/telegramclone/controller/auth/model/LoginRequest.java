package univ.tuit.telegramclone.controller.auth.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String phoneNumber;

    private String activationCode;
}
