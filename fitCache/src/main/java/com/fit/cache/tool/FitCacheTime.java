package com.fit.cache.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author songhao
 */
public class FitCacheTime {

    private static final Logger LOG = LoggerFactory.getLogger(FitCacheTime.class);
    private static final String TITLE = "FitCacheTime";

    /**
     * 加权求和算法，计算数据的评分
     *
     * @param frequency
     * @param lastTime
     * @return
     */
    private static double calculateScore(double frequency, long lastTime) {
        LOG.info(TITLE, "timeDecay:{}", frequency);
        // 根据业务需求和数据的重要性，给访问频率和最近访问时间分配不同的权重
        double frequencyWeight = com.fit.cache.tool.FitCacheStaticConfig.getStaticConfig("frequencyWeight") == null
            ? 0.7 : Double.parseDouble(com.fit.cache.tool.FitCacheStaticConfig.getStaticConfig("frequencyWeight"));
        double timeWeight = com.fit.cache.tool.FitCacheStaticConfig.getStaticConfig("timeWeight") == null ? 0.7
            : Double.parseDouble(com.fit.cache.tool.FitCacheStaticConfig.getStaticConfig("timeWeight"));

        // 计算访问频率和最近访问时间的值
        double time = (System.currentTimeMillis() - lastTime) / 1000.0;
        // 使用递减函数计算时间权重，越近访问的时间权重越高
        double timeDecay = Math.exp(-time);
        LOG.info(TITLE, "timeDecay:{}", timeDecay);
        // 加权求和，得到评分
        double score = frequencyWeight * frequency + timeWeight * timeDecay;
        LOG.info(TITLE, "score:{}", score);
        return score;
    }

    /**
     * 计算数据适合被存储的时间
     *
     * @param frequency
     * @param lastTime
     * @return
     */
    public static int calculateStorageTime(double frequency, long lastTime) {
        // 根据评分确定数据适合被存储的时间
        double score = calculateScore(frequency, lastTime);
        int storageTime = (int)Math.ceil(score);
        LOG.info(TITLE, "storageTime:{}", storageTime);
        return storageTime;
    }

}
