package com.fit.cache.cache;

import com.fit.cache.tool.LruCache;
import com.github.benmanes.caffeine.cache.Cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author songhao
 */
public class CaffeineCacheHolder {
    /**
     * key是appName，value是caffeine
     */
    private static final ConcurrentHashMap<String, Object> CACHE_MAP = new ConcurrentHashMap<>();

    private static final String FIT = "fit";
    private static final String WINDOW = "window";

    private static final String LRU = "lru";
    public static Cache<String, Object> getFitCache() {
        if (CACHE_MAP.get(FIT) == null) {
            // todo 这里要从配置中心拿
            CACHE_MAP.put(FIT, CaffeineBuilder.cache(60, 100, 60));
        }
        return (Cache<String, Object>) CACHE_MAP.get(FIT);
    }

    public static Cache<String, Object> getWindowCache() {
        if (CACHE_MAP.get(WINDOW) == null) {
            // todo 这里要从配置中心拿
            CACHE_MAP.put(WINDOW, CaffeineBuilder.cache(60, 100, 60));
        }
        return (Cache<String, Object>) CACHE_MAP.get(WINDOW);
    }

    public static LruCache getLruCache() {
        if (CACHE_MAP.get(LRU) == null) {
            // todo 这里要从配置中心拿
            CACHE_MAP.put(LRU, new LruCache(1));
        }
        return (LruCache) CACHE_MAP.get(LRU);
    }

}
