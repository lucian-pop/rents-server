package ro.fizbo.rents.logic;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public final class AsyncTaskManager {
	
	private static Logger logger = Logger.getLogger(AsyncTaskManager.class);
	
	private static final int NO_THREADS = 1;
	
	private static final int MAX_TASKS_NO = 100;
	
	private static final int WAIT_TO_FINISH = 20;
	
	private static final ExecutorService executorService;
	
	static {
		BlockingQueue<Runnable> asyncTaskQueue = new ArrayBlockingQueue<Runnable>(MAX_TASKS_NO);
		executorService = new ThreadPoolExecutor(NO_THREADS, NO_THREADS, 0L, TimeUnit.MILLISECONDS,
				asyncTaskQueue);
	}
	
	private AsyncTaskManager() {
	}

	public static void post(Runnable asyncTask) {
		executorService.execute(asyncTask);
	}
	
	public static void stop() {
		executorService.shutdownNow();
		try {
			executorService.awaitTermination(WAIT_TO_FINISH, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.info("Got error while shutting down the AsyncTaskManager.", e);
		}
	}
}
