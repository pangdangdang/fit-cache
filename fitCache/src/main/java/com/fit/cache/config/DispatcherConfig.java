package com.fit.cache.config;

import com.fit.cache.core.Consumer;
import com.fit.cache.core.KeyConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author songhao
 */
@Configuration
public class DispatcherConfig {

    private ExecutorService threadPoolExecutor = new ThreadPoolExecutor(1,
            2,
            5,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    /**
     * 队列
     */
    public static BlockingQueue<String> QUEUE = new LinkedBlockingQueue<>(200);

    @Bean
    public Consumer consumer() {
        List<KeyConsumer> consumerList = new ArrayList<>();
        KeyConsumer keyConsumer = new KeyConsumer();
        consumerList.add(keyConsumer);

        threadPoolExecutor.submit(keyConsumer::beginConsume);
        return new Consumer(consumerList);
    }
}
