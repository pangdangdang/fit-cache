package com.fit.cache.tool;

/**
 * @author songhao
 */
public class FitCacheTime {

    /**
     * 加权递减求和算法，计算数据的评分
     *
     * @param frequency
     * @param lastTime
     * @return
     */
    private static double calculateScore(double frequency, long lastTime) {
        // 根据业务需求和数据的重要性，给访问频率和最近访问时间分配不同的权重
        // 这里从配置中心拿
        double frequencyWeight = 0.7;
        double timeWeight = 0.3;
        // 计算访问频率和最近访问时间的值
        double time = (System.currentTimeMillis() - lastTime) / 1000.0;
        // 使用递减函数计算时间权重，越近访问的时间权重越高
        double timeDecay = Math.exp(-time);
        // 加权求和，得到评分
        double score = frequencyWeight * frequency + timeWeight * timeDecay;
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
        int storageTime = (int) Math.ceil(score);
        return storageTime;
    }

}
