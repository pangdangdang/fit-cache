package com.fit.cache.core;


import com.fit.cache.cache.CaffeineCacheHolder;
import com.fit.cache.config.DispatcherConfig;
import com.fit.cache.tool.FitCacheTime;
import com.fit.cache.tool.SlidingWindow;

/**
 * @author songhao
 */
public class FitCacheStore {

    /**
     * 判断是否适合缓存
     */
    public static boolean isFitCache(String key) {
        try {
            // 先看滑动窗口的热度，判断适不适合缓存
            boolean fit = CaffeineCacheHolder.getFitCache().getIfPresent(key) != null;
            if (!fit) {
                fit = CaffeineCacheHolder.getLruCache().get(key) != null;
            }
            DispatcherConfig.QUEUE.put(key);
            return fit;
        } catch (Exception e) {
            return false;
        }
    }


    public static int fitCacheTime(String key) {
        try {
            // 先看滑动窗口的热度，判断适不适合缓存
            SlidingWindow window = (SlidingWindow) CaffeineCacheHolder.getWindowCache().getIfPresent(key);
            long lastTime = (long) CaffeineCacheHolder.getLruCache().get(key);
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
            return res;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 从本地caffeine取值
     */
    public static Object get(String key) {
        return CaffeineCacheHolder.getFitCache().getIfPresent(key);
    }

    /**
     * 设置缓存
     */
    public static boolean set(String key, Object value) {
        Object object = CaffeineCacheHolder.getFitCache().getIfPresent(key);
        Object lru = CaffeineCacheHolder.getLruCache().get(key);
        if (object == null && lru == null) {
            return false;
        }
        CaffeineCacheHolder.getFitCache().put(key, value);
        return true;
    }
//
//    private static ExecutorService threadPoolExecutor = new ThreadPoolExecutor(1,
//            2,
//            5,
//            TimeUnit.SECONDS,
//            new ArrayBlockingQueue<>(100),
//            new ThreadPoolExecutor.DiscardOldestPolicy());
//    public static void main (String[] args) throws InterruptedException {
//        KeyRule rule = new KeyRule("test", true, 2,5);
//        KeyRuleHolder.KEY_RULES.add(rule);
//        IKeyListener iKeyListener = new KeyListener();
//        KeyConsumer keyConsumer = new KeyConsumer();
//        keyConsumer.setKeyListener(iKeyListener);
//
//        threadPoolExecutor.submit(keyConsumer::beginConsume);
//        boolean fit = isFitCache("test");
//        System.out.println("第一次访问test是否适合" + fit);
//        Thread.sleep(1000);
//        fit = isFitCache("test");
//        System.out.println("第2次访问test是否适合" + fit);
//        Thread.sleep(1000);
//        fit = isFitCache("test666");
//        System.out.println("第一次访问test666是否适合" + fit);
//        Thread.sleep(1000);
//        fit = isFitCache("test");
//        System.out.println("第3次访问test是否适合" + fit);
//        Thread.sleep(1000);
//        int time = fitCacheTime("test");
//        System.out.println("第1次访问test适合时间" + time);
//    }

}
