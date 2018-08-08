package com.luo.free.tool.thread;

import lombok.Setter;

import java.util.concurrent.*;

/**
 * 异步增强型ThreadPoolExecutor
 *   1. 异步非阻塞Future.get()，通过IListenable.callableCallback回调
 *   2. 支持IListenable回调（beforeExecute:任务执行前回调、callableCallback:callable任务执行后回调、runnableCallback:runnable任务执行后回调）
 *   3. 支持execute/submit任务时添加入参，在beforeExecute回调时使用
 *   4. 可对外暴露执行任务对应的线程
 *   5. 更多功能有待挖掘...
 *
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

    private ConcurrentHashMap<Object, Object> argHashMap = new ConcurrentHashMap<>();

    @SuppressWarnings("all")
    public void execute(Runnable command, Object arg) {
        Runnable task = () -> command.run();
        argHashMap.put(task, arg);
        execute(task);
    }

    @SuppressWarnings("all")
    public <T> Future<T> submit(Callable<T> task, Object arg) {
        if (task == null) {
            throw new NullPointerException();
        }

        RunnableFuture<T> ftask = newTaskFor(task);
        argHashMap.put(ftask, arg);
        execute(ftask);
        return ftask;
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        if (this.listenable == null) {
            return;
        }

        this.listenable.beforeExecute(t, r, this.argHashMap.remove(r));
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if (this.listenable == null) {
            return;
        }

        if (r instanceof Future) {
            this.listenable.callableCallback((Future) r, r, t);
        } else {
            this.listenable.runnableCallback(r, t);
        }
    }
}
