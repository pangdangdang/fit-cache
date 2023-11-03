package com.fit.cache.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fit.cache.core.Consumer;
import com.fit.cache.core.KeyConsumer;
import com.fit.cache.tool.FitCacheStaticConfig;

/**
 * @author songhao
 */
@Configuration
public class DispatcherConfig {

    private static final Logger LOG = LoggerFactory.getLogger(DispatcherConfig.class);
    private static final String TITLE = "DispatcherConfig";

    /**
     * 抛弃最老的计算
     */
    private ExecutorService threadPoolExecutor =
        new ThreadPoolExecutor(FitCacheStaticConfig.getIntConfig("dispatcherConfigCore"),
            FitCacheStaticConfig.getIntConfig("dispatcherConfigMax"), 10000L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(FitCacheStaticConfig.getIntConfig("dispatcherConfigQueue")),
            new DiscardOldestPolicy());

    static class DiscardOldestPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (!executor.isShutdown()) {
                // 丢弃队列中最老的任务
                Runnable oldestTask = executor.getQueue().poll();
                LOG.error(TITLE, "Discarding oldest task:{}", oldestTask);
                // 提交当前任务
                executor.execute(r);
            }
        }
    }

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
