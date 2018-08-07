package com.luo.free.tool.test.thread;

import com.luo.free.tool.thread.IListenable;
import com.luo.free.tool.thread.ListenableThreadPoolExecutor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        this.executor = new ListenableThreadPoolExecutor(2, 5,
                60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    @After
    public void after() {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5L));
    }

    @Test
    public void testRunnable() {
        this.executor.setListenable(new IListenable() {
            public void beforeExecute(Thread t, Runnable r, Object arg) {
                System.out.println(t.getName() + " " + r + " " + arg);
            }

            public void callableCallback(Future future, Runnable r, Throwable t) {
                assertTrue(false);
            }
        });
        this.executor.execute(() -> System.out.println("testRunnable"), "test-arg");
        this.executor.execute(() -> System.out.println("testRunnable"), "test-arg");
    }

    @Test
    public void testCallable() {
        this.executor.setListenable(new IListenable() {
            public void runnableCallback(Runnable r, Throwable t) {
                assertTrue(false);
            }
        });
        this.executor.submit(() -> "testCallable");
        this.executor.submit(() -> "testCallable");
    }

    @Test
    public void testNoListenableFuture() throws Exception {
        this.executor.execute(() -> System.out.println("testNoListenableFuture testRunnable"));
        this.executor.execute(() -> System.out.println("testNoListenableFuture testRunnable"));

        this.executor.submit(() -> "testNoListenableFuture testCallable");
        this.executor.submit(() -> "testNoListenableFuture testCallable");
    }
}
