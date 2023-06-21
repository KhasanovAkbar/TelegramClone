package univ.tuit.telegramclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import univ.tuit.telegramclone.persistant.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
