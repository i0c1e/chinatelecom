/**
 * @author Charles
 * @create 2019/3/29
 * @since 1.0.0
 */

package com.victor.chinatelecom.consumer;

public class ThreadLocalExample {
    public static void main(String[] args) {
        ThreadLocal threadLocal = new ThreadLocal();
        Thread thread1 = new Thread(() -> {
            threadLocal.set(1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(threadLocal.get());
            threadLocal.remove();
        });
        Thread thread2 = new Thread(() -> {
            threadLocal.set(2);
//            threadLocal.remove();
        });
        thread1.start();
        thread2.start();
    }
}