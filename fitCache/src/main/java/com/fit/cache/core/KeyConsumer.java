package com.fit.cache.core;

import com.fit.cache.config.DispatcherConfig;
import com.fit.cache.keylistener.IKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @author songhao
 */
public class KeyConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(FitCacheStore.class);
private static final String TITLE = "KeyConsumer";
    @Resource
    private IKeyListener iKeyListener;

    public void setKeyListener(IKeyListener iKeyListener) {
        this.iKeyListener = iKeyListener;
    }

    public void beginConsume() {
        while (true) {
            try {
                String key = DispatcherConfig.QUEUE.take();
                LOG.info(TITLE, "new key:{}", key);
                iKeyListener.newKey(key);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
