package univ.tuit.telegramclone.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univ.tuit.telegramclone.cache.repository.CacheService;
import univ.tuit.telegramclone.persistant.model.AccessToken;
import univ.tuit.telegramclone.persistant.model.User;
import univ.tuit.telegramclone.repository.AccessTokenRepository;
import univ.tuit.telegramclone.repository.UserRepository;

import java.util.Calendar;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccessTokenRepository accessTokenRepository;

    @Autowired
    CacheService cacheService;

    @Override
    public void putAccessToken(String token, Long userId) {

        //store token in the cache
        cacheService.putAccessToken(token, String.valueOf(userId));

        AccessToken accessToken = AccessToken.builder()
                .token(token)
                .userId(userId)
                .createdAt(Calendar.getInstance().getTime())
                .build();

        accessTokenRepository.save(accessToken);
    }

    @Override
    public Long loginWithAccessToken(String token) {

        String userIdByAccessToken = cacheService.getUserIdByAccessToken(token);
        if (userIdByAccessToken == null){
            User user = userRepository.findByToken(token);
            if (user !=null)
                return user.getUserId();
            else return 0L;
        }
        return Long.valueOf(userIdByAccessToken);
    }

    @Override
    public AccessToken getAccessToken(Long userId) {
        return accessTokenRepository.findByUserId(userId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        //
    }
}
