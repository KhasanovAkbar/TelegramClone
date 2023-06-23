package univ.tuit.telegramclone.controller.auth.service;

import org.springframework.stereotype.Service;
import univ.tuit.telegramclone.cache.repository.CacheService;
import univ.tuit.telegramclone.controller.auth.AccessTokenService;
import univ.tuit.telegramclone.controller.auth.AuthUserService;
import univ.tuit.telegramclone.persistant.model.AccessToken;
import univ.tuit.telegramclone.persistant.model.User;
import univ.tuit.telegramclone.repository.UserRepository;

import java.util.Calendar;
import java.util.UUID;

@Service
public class AuthUserServiceImpl implements AuthUserService {

    private final AccessTokenService accessTokenService;
    private final CacheService cacheService;
    private final UserRepository userRepository;


    public AuthUserServiceImpl(AccessTokenService accessTokenService, CacheService cacheService, UserRepository userRepository) {
        this.accessTokenService = accessTokenService;
        this.cacheService = cacheService;
        this.userRepository = userRepository;
    }

    @Override
    public void putAccessToken(String token, Long userId) {

        accessTokenService.save(token, userId);
    }

    @Override
    public Long loginWithAccessToken(String token) {

        String userIdByAccessToken = cacheService.getUserIdByAccessToken(token);
        if (userIdByAccessToken == null) {
            User user = findByToken(token);
            if (user != null)
                return user.getUserId();
            else return 0L;
        }
        return Long.valueOf(userIdByAccessToken);
    }

    @Override
    public AccessToken getAccessToken(Long userId) {
        return accessTokenService.findByUserId(userId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        //
    }

    @Override
    public String createUser(String phoneNumber) {

        User user = userRepository.save(User.builder()
                .phoneNumber(phoneNumber)
                .createdAt(Calendar.getInstance().getTime())
                .build());

        Long userId = user.getUserId();
        AccessToken accessToken = getAccessToken(userId);
        String token;
        if (accessToken == null) {
            token = UUID.randomUUID().toString();
            putAccessToken(token, userId);
        } else {
            token = accessToken.getToken();
        }

        return token;
    }

    @Override
    public User findByToken(String token) {
        return userRepository.findByToken(token);
    }
}
