package com.fit.cache.cache;

/**
 * @author songhao
 */
public class CacheHolder {
    private static final String FIT = "FIT_";
    private static final String WINDOW = "WINDOW_";

    private static final String LRU = "LRU_";

    /**
     * 获取适合缓存
     * 
     * @param key
     * @return
     */
    public static Object getFitCache(String key) {
        return CacheManagerService.getData(FIT + key);
    }

    /**
     * 设置适合缓存
     * 
     * @param key
     * @return
     */
    public static boolean setFitCache(String key, Object v) {
        CacheManagerService.put(FIT + key, v);
        return true;
    }

    /**
     * 设置缓存
     *
     * @param key
     * @return
     */
    public static boolean put(String key, Object value) {
        CacheManagerService.put(FIT + key, value);
        return true;
    }

    /**
     * 获取滑动窗口缓存
     * 
     * @param key
     * @return
     */
    public static Object getWindowCache(String key) {
        return CacheManagerService.getData(WINDOW + key);
    }

    /**
     * 设置滑动窗口缓存
     *
     * @param key
     * @return
     */
    public static boolean setWindowCache(String key, Object v) {
        CacheManagerService.put(WINDOW + key, v);
        return true;
    }

    /**
     * 获取lru缓存
     * 
     * @param key
     * @return
     */
    public static Object getLruCache(String key) {
        return CacheManagerService.getData(LRU + key);
    }

    /**
     * 设置lru缓存
     *
     * @param key
     * @return
     */
    public static boolean setLruCache(String key, Object v) {
        CacheManagerService.put(LRU + key, v);
        return true;
    }

}
