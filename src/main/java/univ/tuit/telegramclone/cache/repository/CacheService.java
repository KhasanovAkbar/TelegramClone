package univ.tuit.telegramclone.cache.repository;

public interface CacheService {

    void putAccessToken(String token, String userId);

    String getUserIdByAccessToken(String token);

    void putActivationCode(String phoneNumber, String activationCode);

    String queryMobileActivationCode(String phoneNumber, String activationCode);

    void deleteAny(String key);

}
