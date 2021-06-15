/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author lepdou
 * @version $Id: DrmThreadFactory.java, v 0.1 2018年03月08日 下午8:13 lepdou Exp $
 */
public class EMWThreadFactory implements ThreadFactory {

    private static final ThreadGroup   THREAD_GROUP = new ThreadGroup("EMW");
    private final        AtomicInteger threadNumber = new AtomicInteger(1);

    private String  threadNamePrefix;
    private boolean daemon;

    private EMWThreadFactory(String threadNamePrefix, boolean daemon) {
        this.threadNamePrefix = threadNamePrefix;
        this.daemon = daemon;
    }

    public static ThreadFactory create(String threadNamePrefix, boolean daemon) {
        return new EMWThreadFactory(threadNamePrefix, daemon);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(THREAD_GROUP, r, THREAD_GROUP.getName() + "-" + threadNamePrefix + "-" + threadNumber.getAndIncrement());
        t.setDaemon(this.daemon);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}