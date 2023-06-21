package univ.tuit.telegramclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import univ.tuit.telegramclone.persistant.model.AccessToken;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    AccessToken findByUserId(Long userId);
}
