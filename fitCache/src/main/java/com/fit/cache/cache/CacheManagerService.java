package com.fit.cache.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fit.cache.tool.FitCacheStaticConfig;
import com.github.benmanes.caffeine.cache.Cache;

/**
 * @author sh
 */
public class CacheManagerService {

    private static final Logger logger = LoggerFactory.getLogger(CacheManagerService.class);

    private static Cache<String, Object> cache = CaffeineBuilder.cache(FitCacheStaticConfig.getIntConfig("cacheMin"),
        FitCacheStaticConfig.getIntConfig("cacheMax"), FitCacheStaticConfig.getIntConfig("cacheExpire"));

    public static Object getData(String key) {
        try {
            return cache.getIfPresent(key);
        } catch (Exception ex) {
            logger.error("setCacheData error", ex);
            return null;
        }
    }

    public static void put(String key, Object value) {
        try {
            cache.put(key, value);
        } catch (Exception ex) {
            logger.error("setCacheData error", ex);
        }
    }

}
