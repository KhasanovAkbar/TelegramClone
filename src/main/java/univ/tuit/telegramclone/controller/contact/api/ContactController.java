package univ.tuit.telegramclone.controller.contact.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import univ.tuit.telegramclone.controller.contact.model.ContactRequest;
import univ.tuit.telegramclone.controller.contact.model.SaveContactRequest;
import univ.tuit.telegramclone.controller.contact.ContactService;
import univ.tuit.telegramclone.persistant.model.Contact;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveContact(@RequestBody SaveContactRequest contactRequest) {

        contactService.saveContact(contactRequest.getUserId(), contactRequest.getContactNumber());

        return new ResponseEntity<>(
                "Contact saved",
                HttpStatus.OK
        );
    }


    @RequestMapping(value = "/getContacts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getContacts(@RequestBody ContactRequest contactRequest) {

        List<Contact> contacts = contactService.getAll(contactRequest.getAccessToken());

        return new ResponseEntity<>(
                contacts,
                HttpStatus.OK
        );
    }
}
