package univ.tuit.telegramclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import univ.tuit.telegramclone.persistant.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u, AccessToken t WHERE u.userId = t.userId AND t.token =: token")
    User findByToken(@Param("token") String token);
}
