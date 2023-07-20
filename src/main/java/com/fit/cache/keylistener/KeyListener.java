package com.fit.cache.keylistener;

import com.fit.cache.cache.CaffeineCacheHolder;
import com.fit.cache.rule.KeyRule;
import com.fit.cache.rule.KeyRuleHolder;
import com.fit.cache.tool.SlidingWindow;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * key的新增、删除处理
 *
 * @author songhao
 */
@Component
public class KeyListener implements IKeyListener {

    @Override
    public void newKey(String key) {
        SlidingWindow slidingWindow = checkWindow(key);
        CaffeineCacheHolder.getLruCache().put(key, System.currentTimeMillis());
        //看看hot没
        boolean hot = slidingWindow.addCount(1);

        CaffeineCacheHolder.getWindowCache().put(key, slidingWindow);
        if (hot && CaffeineCacheHolder.getFitCache().getIfPresent(key) == null) {
            //数据变热，适合缓存
            CaffeineCacheHolder.getFitCache().put(key, System.currentTimeMillis());
        }
    }

    /**
     * 生成或返回该key的滑窗
     */
    private SlidingWindow checkWindow(String key) {
        // 取该key的滑窗
        return (SlidingWindow) CaffeineCacheHolder.getWindowCache().get(key, (Function<String, SlidingWindow>) s -> {
            // 是个新key，获取它的规则
            KeyRule keyRule = KeyRuleHolder.findRule(key);
            return new SlidingWindow(keyRule.getInterval(), keyRule.getThreshold());
        });
    }

}
