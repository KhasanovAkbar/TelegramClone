package univ.tuit.telegramclone.controller.contact;

import univ.tuit.telegramclone.persistant.model.Contact;

import java.util.List;

public interface ContactService {

    void saveContact(Long userId,  String phoneNumber);

    List<Contact> getAll(String accessToken);
}
