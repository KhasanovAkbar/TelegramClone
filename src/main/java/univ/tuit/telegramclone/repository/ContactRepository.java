package univ.tuit.telegramclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import univ.tuit.telegramclone.persistant.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
