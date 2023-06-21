package univ.tuit.telegramclone.cache;

import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisFactory {

    @Value("${cache.redis.host}")
    private static String host = "localhost";

    @Value("${cache.redis.port}")
    private static Integer port = 6379;

    @Value("${cache.redis.timeout}")
    private static Integer timeout = 5000;

    @Value("${cache.redis.password}")
    private static String password = "1234";

    public RedisFactory() {
    }

    private static final JedisPool jedisPool;

    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);

        jedisPool = new JedisPool(
                poolConfig,
                host,
                port,
                timeout,
                password
        );
    }

    public static Jedis getConnection(){
        return jedisPool.getResource();
    }
}
