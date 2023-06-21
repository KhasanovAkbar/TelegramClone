package univ.tuit.telegramclone.auth.controller.model;

import lombok.*;
import univ.tuit.telegramclone.persistant.model.Contact;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponse {

    private List<Contact> contacts;
}
