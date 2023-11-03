package com.fit.cache.rule;

/**
 * @author songhao
 */
public class KeyRule {
    /**
     * key的前缀，也可以完全和key相同。为"*"时代表通配符
     */
    private String key;
    /**
     * 是否是前缀，true是前缀
     */
    private boolean prefix;
    /**
     * 间隔时间（秒）
     */
    private int interval;
    /**
     * 累计数量
     */
    private int threshold;

    public KeyRule(String key, boolean prefix, int interval, int threshold) {
        this.key = key;
        this.prefix = prefix;
        this.interval = interval;
        this.threshold = threshold;
    }

    public String getKey() {
        return key;
    }

    public boolean isPrefix() {
        return prefix;
    }

    public int getInterval() {
        return interval;
    }

    public int getThreshold() {
        return threshold;
    }

}
