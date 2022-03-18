package top.ccyy.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池工厂，负责整个服务端与客户端的线程池创建与关闭
 *
 * @author FriskKiddo
 */
public class ThreadPoolFactory {
    /**
     * 线程池参数
     */

    private static final int CORE_POOL_SIZE = 10;
    private static final int MAXIMUM_POOL_SIZE = 100;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    private final static Logger logger = LoggerFactory.getLogger(ThreadPoolFactory.class);

    private static final Map<String, ExecutorService> threadPoolsMap = new ConcurrentHashMap<>();


    /**
     *
     * @param prefix 线程名前缀
     * @return
     */
    public static ExecutorService createDefaultThreadPool(String prefix) {
        return createDefaultThreadPool(prefix, false);
    }

    public static ExecutorService createDefaultThreadPool(String prefix, boolean isDaemon) {
        ExecutorService pool = threadPoolsMap.computeIfAbsent(prefix, f -> createDefaultThreadPool(prefix, isDaemon));
        if (pool.isShutdown() || pool.isTerminated()) {
            threadPoolsMap.remove(prefix);
            pool = createThreadPool(prefix, isDaemon);
            threadPoolsMap.put(prefix, pool);
        }
        return pool;
    }

    //创建线程池
    public static ExecutorService createThreadPool(String prefix, boolean isDaemon) {
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = createThreadPoolFactory(prefix, isDaemon);
        return new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.MINUTES,
                blockingQueue,
                threadFactory
        );
    }

    public static ThreadFactory createThreadPoolFactory(String prefix, boolean isDaemon) {
        if (prefix != null) {
            if (isDaemon != false) {
                return new ThreadFactoryBuilder().setNameFormat(prefix + "-%d")
                        .setDaemon(true).build();
            } else {
                return new ThreadFactoryBuilder().setNameFormat(prefix + "-%d")
                        .setDaemon(false).build();
            }
        }
        return Executors.defaultThreadFactory();
    }

    public static void shutdownAll() {
        logger.info("关闭所有线程池");
        threadPoolsMap.entrySet().parallelStream().forEach(entry->{
            ExecutorService executorService = entry.getValue();
            executorService.shutdown();
            logger.info("关闭线程池 {} {}", entry.getKey(), executorService.isTerminated());
            try {
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error("关闭线程池失败！");
                executorService.shutdownNow();
            }
        });
    }
}
