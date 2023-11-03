package com.fit.cache.keylistener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fit.cache.cache.CacheHolder;
import com.fit.cache.rule.KeyRule;
import com.fit.cache.rule.KeyRuleHolder;
import com.fit.cache.tool.SlidingWindow;

/**
 * key的监听处理
 *
 * @author songhao
 */
@Component
public class KeyListener implements IKeyListener {
    private static final Logger LOG = LoggerFactory.getLogger(KeyListener.class);
    private static final String TITLE = "KeyListener";

    @Override
    public void newKey(String key) {
        SlidingWindow slidingWindow = checkWindow(key);
        if (slidingWindow == null) {
            LOG.info(TITLE, "key:{} not handle", key);
            return;
        }
        CacheHolder.setLruCache(key, System.currentTimeMillis());
        // 看看hot没
        boolean hot = slidingWindow.addCount(1);
        LOG.info(TITLE, "key:{},hot:{}", key, hot);
        CacheHolder.setWindowCache(key, slidingWindow);
        if (hot && CacheHolder.getFitCache(key) == null) {
            LOG.info(TITLE, "key:{} fit cache", key);
            // 数据变热，适合缓存
            CacheHolder.setFitCache(key, System.currentTimeMillis());
        }
    }

    /**
     * 生成或返回该key的滑窗
     */
    private SlidingWindow checkWindow(String key) {
        Object window = CacheHolder.getWindowCache(key);
        if (window != null) {
            LOG.info(TITLE, "key:{} has window", key);
            return (SlidingWindow)window;
        }
        LOG.info(TITLE, "key:{} new window", key);
        // 是个新key，获取它的规则
        KeyRule keyRule = KeyRuleHolder.findRule(key);
        return new SlidingWindow(keyRule.getInterval(), keyRule.getThreshold());
    }

}
