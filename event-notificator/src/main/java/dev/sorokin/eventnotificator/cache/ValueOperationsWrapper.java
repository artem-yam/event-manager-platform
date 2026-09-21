package dev.sorokin.eventnotificator.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class ValueOperationsWrapper<K, V> implements ValueOperations<K, V> {

    private final ValueOperations<K, V> valueOps;

    @Override
    public void set(K key, V value) {
        try {
            valueOps.set(key, value);
        } catch (Exception e) {
            log.error("Ошибка Redis set: key={}", key, e);
        }
    }

    @Override
    public V setGet(K key, V value, long timeout, TimeUnit unit) {
        try {
            return valueOps.setGet(key, value, timeout, unit);
        } catch (Exception e) {
            log.error("Ошибка Redis setGet: key={}", key, e);
            return null;
        }
    }

    @Override
    public V setGet(K key, V value, Duration duration) {
        try {
            return valueOps.setGet(key, value, duration);
        } catch (Exception e) {
            log.error("Ошибка Redis setGet: key={}", key, e);
            return null;
        }
    }

    @Override
    public void set(K key, V value, long timeout, TimeUnit unit) {
        try {
            valueOps.set(key, value, timeout, unit);
        } catch (Exception e) {
            log.error("Ошибка Redis set с TTL: key={}", key, e);
        }
    }

    @Override
    public void set(K key, V value, Duration timeout) {
        try {
            valueOps.set(key, value, timeout);
        } catch (Exception e) {
            log.error("Ошибка Redis set с Duration: key={}", key, e);
        }
    }

    @Override
    public Boolean setIfAbsent(K key, V value) {
        try {
            return valueOps.setIfAbsent(key, value);
        } catch (Exception e) {
            log.error("Ошибка Redis setIfAbsent: key={}", key, e);
            return false;
        }
    }

    @Override
    public Boolean setIfAbsent(K key, V value, long timeout, TimeUnit unit) {
        try {
            return valueOps.setIfAbsent(key, value, timeout, unit);
        } catch (Exception e) {
            log.error("Ошибка Redis setIfAbsent с TTL: key={}", key, e);
            return false;
        }
    }

    @Override
    public Boolean setIfAbsent(K key, V value, Duration timeout) {
        try {
            return valueOps.setIfAbsent(key, value, timeout);
        } catch (Exception e) {
            log.error("Ошибка Redis setIfAbsent с Duration: key={}", key, e);
            return false;
        }
    }

    @Override
    public Boolean setIfPresent(K key, V value) {
        try {
            return valueOps.setIfPresent(key, value);
        } catch (Exception e) {
            log.error("Ошибка Redis setIfPresent: key={}", key, e);
            return false;
        }
    }

    @Override
    public Boolean setIfPresent(K key, V value, long timeout, TimeUnit unit) {
        try {
            return valueOps.setIfPresent(key, value, timeout, unit);
        } catch (Exception e) {
            log.error("Ошибка Redis setIfPresent с TTL: key={}", key, e);
            return false;
        }
    }

    @Override
    public Boolean setIfPresent(K key, V value, Duration timeout) {
        try {
            return valueOps.setIfPresent(key, value, timeout);
        } catch (Exception e) {
            log.error("Ошибка Redis setIfPresent с Duration: key={}", key, e);
            return false;
        }
    }

    @Override
    public void multiSet(Map<? extends K, ? extends V> map) {
        try {
            valueOps.multiSet(map);
        } catch (Exception e) {
            log.error("Ошибка Redis multiSet: keys={}", map.keySet(), e);
        }
    }

    @Override
    public Boolean multiSetIfAbsent(Map<? extends K, ? extends V> map) {
        try {
            return valueOps.multiSetIfAbsent(map);
        } catch (Exception e) {
            log.error("Ошибка Redis multiSetIfAbsent: keys={}", map.keySet(), e);
            return false;
        }
    }

    @Override
    public V get(Object key) {
        try {
            return valueOps.get(key);
        } catch (Exception e) {
            log.error("Ошибка Redis get: key={}", key, e);
            return null;
        }
    }

    @Override
    public V getAndDelete(K key) {
        try {
            return valueOps.getAndDelete(key);
        } catch (Exception e) {
            log.error("Ошибка Redis getAndDelete: key={}", key, e);
            return null;
        }
    }

    @Override
    public V getAndExpire(K key, long timeout, TimeUnit unit) {
        try {
            return valueOps.getAndExpire(key, timeout, unit);
        } catch (Exception e) {
            log.error("Ошибка Redis getAndExpire: key={}", key, e);
            return null;
        }
    }

    @Override
    public V getAndExpire(K key, Duration timeout) {
        try {
            return valueOps.getAndExpire(key, timeout);
        } catch (Exception e) {
            log.error("Ошибка Redis getAndExpire с Duration: key={}", key, e);
            return null;
        }
    }

    @Override
    public V getAndPersist(K key) {
        try {
            return valueOps.getAndPersist(key);
        } catch (Exception e) {
            log.error("Ошибка Redis getAndPersist: key={}", key, e);
            return null;
        }
    }

    @Override
    public V getAndSet(K key, V value) {
        try {
            return valueOps.getAndSet(key, value);
        } catch (Exception e) {
            log.error("Ошибка Redis getAndSet: key={}", key, e);
            return null;
        }
    }

    @Override
    public List<V> multiGet(Collection<K> keys) {
        try {
            return valueOps.multiGet(keys);
        } catch (Exception e) {
            log.error("Ошибка Redis multiGet: keys={}", keys, e);
            return List.of();
        }
    }

    @Override
    public Long increment(K key) {
        try {
            return valueOps.increment(key);
        } catch (Exception e) {
            log.error("Ошибка Redis increment: key={}", key, e);
            return 0L;
        }
    }

    @Override
    public Long increment(K key, long delta) {
        try {
            return valueOps.increment(key, delta);
        } catch (Exception e) {
            log.error("Ошибка Redis increment с delta: key={}", key, e);
            return 0L;
        }
    }

    @Override
    public Double increment(K key, double delta) {
        try {
            return valueOps.increment(key, delta);
        } catch (Exception e) {
            log.error("Ошибка Redis increment с double: key={}", key, e);
            return 0.0;
        }
    }

    @Override
    public Long decrement(K key) {
        try {
            return valueOps.decrement(key);
        } catch (Exception e) {
            log.error("Ошибка Redis decrement: key={}", key, e);
            return 0L;
        }
    }

    @Override
    public Long decrement(K key, long delta) {
        try {
            return valueOps.decrement(key, delta);
        } catch (Exception e) {
            log.error("Ошибка Redis decrement с delta: key={}", key, e);
            return 0L;
        }
    }

    @Override
    public Integer append(K key, String value) {
        try {
            return valueOps.append(key, value);
        } catch (Exception e) {
            log.error("Ошибка Redis append: key={}", key, e);
            return 0;
        }
    }

    @Override
    public String get(K key, long start, long end) {
        try {
            return valueOps.get(key, start, end);
        } catch (Exception e) {
            log.error("Ошибка Redis get с диапазоном: key={}, [{}, {}]", key, start, end, e);
            return "";
        }
    }

    @Override
    public void set(K key, V value, long offset) {
        try {
            valueOps.set(key, value, offset);
        } catch (Exception e) {
            log.error("Ошибка Redis set с offset: key={}", key, e);
        }
    }

    @Override
    public Long size(K key) {
        try {
            return valueOps.size(key);
        } catch (Exception e) {
            log.error("Ошибка Redis size: key={}", key, e);
            return 0L;
        }
    }

    @Override
    public Boolean setBit(K key, long offset, boolean value) {
        try {
            return valueOps.setBit(key, offset, value);
        } catch (Exception e) {
            log.error("Ошибка Redis setBit: key={}", key, e);
            return false;
        }
    }

    @Override
    public Boolean getBit(K key, long offset) {
        try {
            return valueOps.getBit(key, offset);
        } catch (Exception e) {
            log.error("Ошибка Redis getBit: key={}", key, e);
            return false;
        }
    }

    @Override
    public List<Long> bitField(K key, BitFieldSubCommands subCommands) {
        try {
            return valueOps.bitField(key, subCommands);
        } catch (Exception e) {
            log.error("Ошибка Redis bitField: key={}", key, e);
            return List.of();
        }
    }

    @Override
    public RedisOperations<K, V> getOperations() {
        return valueOps.getOperations();
    }
}
