package com.fit.cache.core;

import java.util.List;

/**
 * @author songhao
 */
public class Consumer {
    private List<KeyConsumer> consumerList;

    public Consumer(List<KeyConsumer> consumerList) {
        this.consumerList = consumerList;
    }

    public KeyConsumer get(int index) {
        return consumerList.get(index);
    }
}
