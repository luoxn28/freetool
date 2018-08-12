package com.luo.free.tool.thread;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xiangnan
 * date 2018/8/8
 */
public interface IListenable {

    /**
     * 任务执行前置回调，该方法禁止抛异常，否则影响任务正常执行
     */
    default void beforeExecute(Thread t, Runnable r, Object arg) {
    }

    default void callableCallback(Future future, Runnable r, Throwable t) {
    }

    default void runnableCallback(Runnable r, Throwable t) {
    }

    /**
     * 任务执行后置回调，该方法可以访问executor及任务arg，这里会暴露executor，可用于任务再次添加操作
     */
    default void afterCallback(ListenableThreadPoolExecutor executor, Object arg, Runnable r, Throwable t) {
    }

}
