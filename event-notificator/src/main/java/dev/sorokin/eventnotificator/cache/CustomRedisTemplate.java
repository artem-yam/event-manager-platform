package dev.sorokin.eventnotificator.cache;

import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@Primary
public class CustomRedisTemplate<K, V> extends RedisTemplate<K, V> {

    public CustomRedisTemplate(RedisConnectionFactory connectionFactory) {
        super.setConnectionFactory(connectionFactory);
    }

    @Override
    public ValueOperations<K, V> opsForValue() {
        return new ValueOperationsWrapper<>(super.opsForValue());
    }
}
