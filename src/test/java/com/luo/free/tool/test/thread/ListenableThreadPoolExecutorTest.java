package com.luo.free.tool.test.thread;

import com.luo.free.tool.thread.IListenable;
import com.luo.free.tool.thread.ListenableThreadPoolExecutor;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static org.junit.Assert.assertTrue;

/**
 * @author xiangnan
 * date 2018/8/8
 */
public class ListenableThreadPoolExecutorTest {

    private ListenableThreadPoolExecutor executor;

    @Before
    public void before() {
        executor = new ListenableThreadPoolExecutor(2, 5,
                60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    @After
    public void after() {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5L));
    }

    @Test
    public void testRunnable() {
        executor.setListenable(new IListenable() {
            public void beforeExecute(Thread t, Runnable r, Object arg) {
                System.out.println(t.getName() + " " + r + " " + arg);
            }

            public void callableCallback(Future future, Runnable r, Throwable t) {
                assertTrue(false);
            }
        });
        executor.execute(() -> System.out.println("testRunnable"), "test-arg");
        executor.execute(() -> System.out.println("testRunnable"), "test-arg");
    }

    @Test
    public void testCallable() {
        executor.setListenable(new IListenable() {
            public void runnableCallback(Runnable r, Throwable t) {
                assertTrue(false);
            }
        });
        executor.submit(() -> "testCallable");
        executor.submit(() -> "testCallable");
    }

    @Test
    public void testNoListenableFuture() throws Exception {
        executor.execute(() -> System.out.println("testNoListenableFuture testRunnable"));
        executor.execute(() -> System.out.println("testNoListenableFuture testRunnable"));

        executor.submit(() -> "testNoListenableFuture testCallable");
        executor.submit(() -> "testNoListenableFuture testCallable");
    }

    @Test
    public void testAutoTask() {
        executor.setListenable(new IListenable() {
            @Override
            public void beforeExecute(Thread t, Runnable r, Object arg) {
                System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss") + " beforeExecute: " + arg);
            }

            @Override
            public void callableCallback(Future future, Runnable r, Throwable t) {
            }

            @Override
            public void runnableCallback(Runnable r, Throwable t) {
            }

            @Override
            public void afterCallback(ListenableThreadPoolExecutor executor, Object arg, Runnable r, Throwable t) {
                System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss") + " afterCallback: " + arg);

                executor.submit(() -> "hello world", "test-arg");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        executor.submit(() -> "hello world", "test-arg");
    }
}
