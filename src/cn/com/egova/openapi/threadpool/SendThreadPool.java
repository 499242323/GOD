package cn.com.egova.openapi.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import org.apache.log4j.Logger;

import cn.com.egova.openapi.constant.ThreadPoolConst;






public class SendThreadPool {
	

	private static int POOL_SIZE_MIN = ThreadPoolConst.POOL_SIZE_MIN;
	private static int POOL_SIZE_MAX = ThreadPoolConst.POOL_SIZE_MAX;
	private static Logger log = Logger.getLogger(SendThreadPool.class);

	private static long THREAD_KEEP_ALIVE_TIME = ThreadPoolConst.THREAD_KEEP_ALIVE_TIME;
	private static int TASK_QUEUE_SIZE = ThreadPoolConst.TASK_QUEUE_SIZE;

	private static ThreadPoolExecutor pool = null;

	public static void startup() {
		pool = new ThreadPoolExecutor(POOL_SIZE_MIN, POOL_SIZE_MAX,
				THREAD_KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue(TASK_QUEUE_SIZE),
				new ThreadPoolExecutor.DiscardOldestPolicy());
	}

	public static void shutdownGraceful(long timeout) {
		if ((pool != null) && (!pool.isShutdown())) {
			try {
				pool.awaitTermination(timeout, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			pool.shutdown();
		}
	}

	public static void shutdown() {
		if ((pool != null) && (!pool.isShutdown())) {
			log.fatal("[shutdown hook] shutting down 'send_task' pool left.");
			pool.shutdown();
		}
	}

	public static void shutdownNow() {
		if ((pool != null) && (!pool.isShutdown())) {
			pool.shutdownNow();
		}
	}

	public static void execute(Runnable task) {
		pool.execute(task);
	}

	public static int getActiveCount() {
		return pool.getActiveCount();
	}

	public static int getCompletedTaskCount() {
		return pool.getActiveCount();
	}

	public static long getTaskCount() {
		return pool.getTaskCount();
	}

	static {
		if (pool == null)
			startup();
	}

}
