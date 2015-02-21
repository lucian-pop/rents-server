package ro.fizbo.rents.logic;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class SchedulerManager {
	
	private static Logger logger = Logger.getLogger(SchedulerManager.class);
	
	private static final int NO_THREADS = 1;
	
	private static final int WAIT_TO_FINISH = 5;
	
	private static final ScheduledExecutorService scheduledExecutorService;
	
	static {
		scheduledExecutorService = Executors.newScheduledThreadPool(NO_THREADS);
	}
	
	private SchedulerManager(){
	}

	public static void execute(Runnable task, long delay) {
		scheduledExecutorService.schedule(task, delay, TimeUnit.MILLISECONDS);
	}
	
	public static void stop() {
		scheduledExecutorService.shutdownNow();
		try {
			scheduledExecutorService.awaitTermination(WAIT_TO_FINISH, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.info("Got error while shutting down the scheduler manager.", e);
		}
	}
}
