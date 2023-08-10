package com.fit.cache.core;

import com.fit.cache.keylistener.IKeyListener;
import com.fit.cache.config.DispatcherConfig;

import javax.annotation.Resource;

/**
 * @author songhao
 */
public class KeyConsumer {


    @Resource
    private IKeyListener iKeyListener;

    public void setKeyListener(IKeyListener iKeyListener) {
        this.iKeyListener = iKeyListener;
    }

    public void beginConsume() {
        while (true) {
            try {
                String key = DispatcherConfig.QUEUE.take();
                iKeyListener.newKey(key);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
