package univ.tuit.telegramclone.controller.contact.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import univ.tuit.telegramclone.cache.repository.CacheService;
import univ.tuit.telegramclone.controller.contact.ContactService;
import univ.tuit.telegramclone.persistant.model.Contact;
import univ.tuit.telegramclone.persistant.model.User;
import univ.tuit.telegramclone.repository.ContactRepository;
import univ.tuit.telegramclone.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactServiceImpl.class);
    private final ContactRepository contactRepository;
    private final CacheService cacheService;
    private final UserRepository userRepository;

    public ContactServiceImpl(ContactRepository contactRepository, CacheService cacheService, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.cacheService = cacheService;
        this.userRepository = userRepository;
    }

    @Override
    public void saveContact(Long userId, String phoneNumber) {

        LOGGER.info(phoneNumber + " is saved by: " + userId + " userId");
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            LOGGER.info("Number not found");
        } else {
            contactRepository.save(Contact.builder()
                    .userId(userId)
                    .contactUserId(user.getUserId())
                    .build());
        }
    }

    @Override
    public List<Contact> getAll(String accessToken) {

        List<Contact> contacts = new ArrayList<>();
        String userIdByAccessToken = cacheService.getUserIdByAccessToken(accessToken);
        if (userIdByAccessToken != null) {

            List<Contact> all = contactRepository.findAll();
            for (Contact contact : all) {
                if (contact.getUserId().equals(Long.valueOf(userIdByAccessToken))) {
                    contacts.add(contact);
                }
            }
        } else {
            LOGGER.info("Token not found");
            return null;
        }
        return contacts;
    }
}
