package dev.hroberts.fileshare.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
public class RedisConfiguration {
    private final String password;
    private final String server;
    private final int port = 6379;

    public RedisConfiguration(@Value("{redis.password}") String password, @Value("{redis.server}") String server) {
        this.password = password;
        this.server = server;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        var config = new RedisStandaloneConfiguration("localhost", 6379);
        config.setHostName(server);
        config.setPort(port);
        config.setPassword(password);
        return new JedisConnectionFactory(config);
    }


}
