package com.fit.cache.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fit.cache.cache.CacheHolder;
import com.fit.cache.config.DispatcherConfig;
import com.fit.cache.tool.FitCacheTime;
import com.fit.cache.tool.SlidingWindow;

/**
 * @author songhao
 */
public class FitCacheStore {

    private static final Logger LOG = LoggerFactory.getLogger(FitCacheStore.class);

    private static final String TITLE = "FitCacheStore";

    /**
     * 判断是否适合缓存
     */
    public static boolean isFitCache(String key) {
        LOG.info(TITLE, "isFitCache key:{}", key);
        try {
            // 先看滑动窗口的热度，判断适不适合缓存
            boolean fit = CacheHolder.getFitCache(key) != null;
            if (!fit) {
                fit = CacheHolder.getLruCache(key) != null;
            }
            DispatcherConfig.QUEUE.put(key);
            LOG.info(TITLE, "isFitCache fit:{}", fit);
            return fit;
        } catch (Exception e) {
            return false;
        }
    }

    public static int fitCacheTime(String key) {
        LOG.info(TITLE, "fitCacheTime key:{}", key);
        try {
            // 先看滑动窗口的热度，判断适不适合缓存
            SlidingWindow window = (SlidingWindow)CacheHolder.getWindowCache(key);
            long lastTime = (long)CacheHolder.getLruCache(key);
            if (window == null && lastTime == 0) {
                return 0;
            }
            if (window == null && lastTime != 0) {
                return FitCacheTime.calculateStorageTime(0, lastTime);
            }
            if (window != null && lastTime == 0) {
                return FitCacheTime.calculateStorageTime(window.getCount(), 0);
            }
            int res = FitCacheTime.calculateStorageTime(window.getCount(), lastTime);
            DispatcherConfig.QUEUE.put(key);
            LOG.info(TITLE, "fitCacheTime res:{}", res);
            return res;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 设置缓存
     */
    public static boolean put(String key, Object v) {
        LOG.info(TITLE, "fitCache set key:{},value:{}", key, v);
        CacheHolder.put(key, v);
        return true;
    }

}
