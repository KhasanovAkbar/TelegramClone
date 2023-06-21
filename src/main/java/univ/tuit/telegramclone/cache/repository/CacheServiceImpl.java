package univ.tuit.telegramclone.cache.repository;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import univ.tuit.telegramclone.cache.RedisFactory;

@Service
public class CacheServiceImpl implements CacheService {

    @Override
    public void putAccessToken(String token, String userId) {
        try (Jedis jedis = RedisFactory.getConnection()) {
            jedis.set(token, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUserIdByAccessToken(String token) {
        try (Jedis jedis = RedisFactory.getConnection()) {
            return jedis.get(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void putActivationCode(String phoneNumber, String activationCode) {

        try (Jedis jedis = RedisFactory.getConnection()) {
            jedis.hset(phoneNumber, String.valueOf(activationCode), "");
            jedis.expire(phoneNumber, 15 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String queryMobileActivationCode(String phoneNumber, String activationCode) {

        try (Jedis jedis = RedisFactory.getConnection()) {
            return jedis.hget(phoneNumber, activationCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteAny(String key) {

        try (Jedis jedis = RedisFactory.getConnection()) {
            jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
