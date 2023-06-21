package univ.tuit.telegramclone.auth;

import univ.tuit.telegramclone.persistant.model.AccessToken;

public interface AuthService {

    void putAccessToken(String accessToken, Long userId);

    Long loginWithAccessToken(String token);

    AccessToken getAccessToken(Long userId);

    void deleteByUserId(Long userId);
}
