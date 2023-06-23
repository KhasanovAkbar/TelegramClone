package univ.tuit.telegramclone.controller.auth;

import univ.tuit.telegramclone.persistant.model.AccessToken;
import univ.tuit.telegramclone.persistant.model.User;

public interface AuthUserService {

    void putAccessToken(String accessToken, Long userId);

    Long loginWithAccessToken(String token);

    AccessToken getAccessToken(Long userId);

    void deleteByUserId(Long userId);

    String createUser(String phoneNumber);

    User findByToken(String token);


}
