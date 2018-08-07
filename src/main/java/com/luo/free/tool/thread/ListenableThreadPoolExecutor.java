package com.luo.free.tool.thread;

import lombok.Setter;

import java.util.concurrent.*;

/**
 * @author xiangnan
 * date 2018/8/8
 */
public class ListenableThreadPoolExecutor extends ThreadPoolExecutor {

    public ListenableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ListenableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public ListenableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public ListenableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Setter
    private IListenable listenable;

    private ConcurrentHashMap<Runnable, Object> argHashMap = new ConcurrentHashMap<>();

    public void execute(Runnable command, Object arg) {
        argHashMap.put(command, arg);
        execute(command);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        if (this.listenable != null) {
            this.listenable.beforeExecute(t, r, this.argHashMap.remove(r));
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if (this.listenable != null) {
            if (r instanceof Future) {
                this.listenable.callableCallback((Future) r, r, t);
            } else {
                this.listenable.runnableCallback(r, t);
            }
        }
    }
}
