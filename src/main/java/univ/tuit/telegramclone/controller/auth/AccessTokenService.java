package univ.tuit.telegramclone.controller.auth;

import univ.tuit.telegramclone.persistant.model.AccessToken;

public interface AccessTokenService {

    void save(String token, Long userId);

    AccessToken findByUserId(Long userId);
}
