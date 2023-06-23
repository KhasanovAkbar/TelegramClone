package univ.tuit.telegramclone.controller.contact.model;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveContactRequest {

    private Long userId;

    private String contactNumber;
}
