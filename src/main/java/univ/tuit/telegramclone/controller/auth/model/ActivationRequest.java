package univ.tuit.telegramclone.controller.auth.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivationRequest {

    private String phoneNumber;
}
