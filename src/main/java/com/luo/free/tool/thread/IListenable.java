package com.luo.free.tool.thread;

import java.util.concurrent.Future;

/**
 * @author xiangnan
 * date 2018/8/8
 */
public interface IListenable {

    default void beforeExecute(Thread t, Runnable r, Object arg) {
    }

    default void callableCallback(Future future, Runnable r, Throwable t) {
    }

    default void runnableCallback(Runnable r, Throwable t) {
    }

}
