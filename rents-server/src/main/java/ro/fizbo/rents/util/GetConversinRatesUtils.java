package ro.fizbo.rents.util;

import java.util.Calendar;

public final class GetConversinRatesUtils {
	
	private static final int START_HOUR = 5;
	
	private static final int START_MINUTE = 0;
	
	private static final int START_SECOND = 0;
	
	private static final int START_MILISECOND = 0;
	
	private GetConversinRatesUtils() {
	}

	public static long getNextRunTime() {
      Calendar startTime = Calendar.getInstance();
      Calendar now = Calendar.getInstance();
      startTime.set(Calendar.HOUR_OF_DAY, START_HOUR);
      startTime.set(Calendar.MINUTE, START_MINUTE);
      startTime.set(Calendar.SECOND, START_SECOND);
      startTime.set(Calendar.MILLISECOND, START_MILISECOND);

      if(startTime.before(now) || startTime.equals(now)) {
    	  startTime.add(Calendar.DATE, 1);
      }

      return startTime.getTime().getTime();
	}
}
