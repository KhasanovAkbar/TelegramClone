package univ.tuit.telegramclone.cache;

import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisFactory {

    private static final String host = "localhost";
    private static final Integer port = 6379;
    private static final Integer timeout = 10000;
    private static final JedisPool jedisPool;

    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);

        jedisPool = new JedisPool(
                poolConfig,
                host,
                port,
                timeout
        );
    }

    public static Jedis getConnection() {
        return jedisPool.getResource();
    }
}
