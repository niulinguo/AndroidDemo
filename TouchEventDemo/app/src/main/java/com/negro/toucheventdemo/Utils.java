package com.negro.toucheventdemo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Negro
 * Date 2017/9/22
 * Email niulinguo@163.com
 */

public final class Utils {

    private Utils() {
    }

    private static AtomicInteger sViewCount = new AtomicInteger(0);

    public static int createViewCount() {
        return sViewCount.incrementAndGet();
    }

}
