package univ.tuit.telegramclone.controller.auth.service;

import org.springframework.stereotype.Service;
import univ.tuit.telegramclone.cache.repository.CacheService;
import univ.tuit.telegramclone.controller.auth.AccessTokenService;
import univ.tuit.telegramclone.persistant.model.AccessToken;
import univ.tuit.telegramclone.repository.AccessTokenRepository;

import java.util.Calendar;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

    private final AccessTokenRepository accessTokenRepository;
    private final CacheService cacheService;


    public AccessTokenServiceImpl(AccessTokenRepository accessTokenRepository, CacheService cacheService) {
        this.accessTokenRepository = accessTokenRepository;
        this.cacheService = cacheService;
    }

    @Override
    public void save(String token, Long userId) {

        AccessToken accessToken = AccessToken.builder()
                .token(token)
                .userId(userId)
                .createdAt(Calendar.getInstance().getTime())
                .build();

        cacheService.putAccessToken(token, String.valueOf(userId));

        //store token in the cache
        accessTokenRepository.save(accessToken);
    }

    @Override
    public AccessToken findByUserId(Long userId) {
        return null;
    }
}
