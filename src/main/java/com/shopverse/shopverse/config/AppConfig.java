package com.shopverse.shopverse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.sql.Time;
import java.util.concurrent.*;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class AppConfig implements AsyncConfigurer {

    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public synchronized Executor getAsyncExecutor() {
        if (Objects.isNull(threadPoolExecutor)) {
            int minPoolSize = 2;
            int maxPoolSize = 4;
            int queueSize = 3;
            threadPoolExecutor = new ThreadPoolExecutor(minPoolSize, maxPoolSize, 1L, TimeUnit.HOURS, new ArrayBlockingQueue<>(queueSize), new CustomThreadFactory());
        }
        return threadPoolExecutor;
    }
    //Another method but for industry above is used
//    @Bean(name = "myThreadPoolExecutor")
//    public Executor taskPoolExecutor() {
//        int minPoolSize = 2;
//        int maxPoolSize = 4;
//        int queueSize = 3;
//        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
//        threadPoolTaskExecutor.setCorePoolSize(minPoolSize);
//        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
//        threadPoolTaskExecutor.setQueueCapacity(queueSize);
//        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
//        threadPoolTaskExecutor.setKeepAliveSeconds(10);
//        threadPoolTaskExecutor.setThreadNamePrefix("MyThread: ");
//        threadPoolTaskExecutor.initialize();
//        return threadPoolTaskExecutor;
//    }
}

class CustomThreadFactory implements ThreadFactory {
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable r) {
        Thread th = new Thread(r);
        th.setName("My Thread-" + threadNumber.getAndIncrement());
        return th;
    }
}
