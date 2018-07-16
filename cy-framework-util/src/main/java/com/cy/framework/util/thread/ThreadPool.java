/**
 * copyright to Huihe Intelligence
 *
 * @author zhufei 2016-5-25  上午9:15:11
 * <p>
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\  =  /O
 * ____/`---'\____
 * .'  \\|     |//  `.
 * /  \\|||  :  |||//  \
 * /  _||||| -:- |||||-  \
 * |   | \\\  -  /// |   |
 * | \_|  ''\---/''  |   |
 * \  .-\__  `-`  ___/-. /
 * ___`. .'  /--.--\  `. . __
 * ."" '<  `.___\_<|>_/___.'  >'"".
 * | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ====`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 佛祖保佑       不再单身
 * 佛曰：
 * 写字楼里写字间，写字间里程序员；
 * 程序人员写程序，写完程序换酒钱；
 */
/**
 *                 _ooOoo_
 *                o8888888o
 *                88" . "88
 *                (| -_- |)
 *                O\  =  /O
 *             ____/`---'\____
 *           .'  \\|     |//  `.
 *          /  \\|||  :  |||//  \
 *         /  _||||| -:- |||||-  \
 *         |   | \\\  -  /// |   |
 *         | \_|  ''\---/''  |   |
 *         \  .-\__  `-`  ___/-. /
 *       ___`. .'  /--.--\  `. . __
 *    ."" '<  `.___\_<|>_/___.'  >'"".
 *   | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *   \  \ `-.   \_ __\ /__ _/   .-` /  /
 *====`-.____`-.___\_____/___.-`____.-'======
 *                 `=---='
 *^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *             佛祖保佑       不再单身
 *佛曰：
 *写字楼里写字间，写字间里程序员；
 *程序人员写程序，写完程序换酒钱；
 */
package com.cy.framework.util.thread;

import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Administrator
 *
 */
public class ThreadPool {
    private static ThreadPool instance = null;
    private Queue<Runnable> threadQueue = null;
    private int max_running_thread = 5;// 最大运行的线程数
    private WorkRunnable[] workers = null;// 工作线程
    private Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * 方法描述：获取线程池对象
     *
     * @return
     * @author zhufei 2016-5-25 上午9:27:42
     */
    public static ThreadPool getInstance() {
        if (instance == null) {
            synchronized (ThreadPool.class) {
                if (instance == null) {
                    instance = new ThreadPool();
                }
            }
        }
        return instance;
    }

    /**
     * 方法描述：执行线程
     *
     * @param thread
     * @author zhufei 2016-5-25 上午11:13:03
     */
    public void execute(Runnable thread) {
        synchronized (threadQueue) {
            threadQueue.offer(thread);
            threadQueue.notify();
        }
    }

    /**
     * 方法描述：销毁线程池
     *
     * @author zhufei 2016-5-25 上午11:44:51
     */
    public void destroy() {
        while (!threadQueue.isEmpty()) {// 如果还有任务没执行完成，就先睡会吧
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.warn("Failed to wake up the thread,error:" + e.toString() + ",detail:" + e.getMessage());
            }
        }
        // 工作线程停止工作，且置为null
        for (int i = 0; i < max_running_thread; i++) {
            workers[i].stopWork();
            workers[i] = null;
        }
        instance = null;
        threadQueue.clear();// 清空任务队列
    }

    /**
     * 类描述：默认构造器
     *
     * @author zhufei 2016-5-25 上午11:12:50
     */
    private ThreadPool() {
        this(5);
    }

    /**
     * 类描述：线程池私有化构造器
     *
     * @author zhufei 2016-5-25 上午9:27:59
     */
    private ThreadPool(int max_thread) {
        if (threadQueue == null) {
            threadQueue = new LinkedList<Runnable>();
        }
        if (max_thread > 0) {
            max_running_thread = max_thread;
        }
        workers = new WorkRunnable[max_running_thread];
        for (int i = 0; i < max_running_thread; i++) {
            workers[i] = new WorkRunnable();
            Thread thread = new Thread(workers[i]);
            thread.start();
        }
    }

    /**
     * 含义：工作线程内部类
     *
     * @author zhufei 2016-5-25 上午11:10:09
     */
    private class WorkRunnable implements Runnable {
        private boolean running = true;

        /**
         * 功能描述：
         *
         * @see Runnable#run()
         * @author zhufei 2016-5-25 上午10:10:50
         */

        public void run() {
            Runnable run = null;
            while (running) {
                synchronized (threadQueue) {
                    while (running && threadQueue.isEmpty()) {
                        try {
                            threadQueue.wait(5000);// 若无线程待处理，休眠5秒
                        } catch (Exception e) {
                            log.warn(
                                    "Failed to wake up the thread,error:" + e.toString() + ",detail:" + e.getMessage());
                        }
                    }
                    if (!threadQueue.isEmpty()) {
                        run = threadQueue.poll();
                    }
                }
                if (run != null) {
                    run.run();
                }
                run = null;
            }
        }

        /**
         * 方法描述：停止工作线程
         *
         * @author zhufei 2016-5-25 上午11:37:36
         */
        public void stopWork() {
            running = false;
        }
    }
}
