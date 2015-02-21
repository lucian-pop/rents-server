package ro.fizbo.rents.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import ro.fizbo.rents.logic.CurrencyManager;
import ro.fizbo.rents.logic.SchedulerManager;
import ro.fizbo.rents.util.Constants;
import ro.fizbo.rents.util.GetConversinRatesUtils;

public class GetConversionRatesTask implements Runnable {
	
	private static Logger logger = Logger.getLogger(GetConversionRatesTask.class);

	@Override
	public void run() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT);
		logger.info("Updated conversion rates at " + dateFormatter.format(new Date()));

		CurrencyManager.updateConversionRates();
		
		SchedulerManager.execute(this, GetConversinRatesUtils.getNextRunTime() - System.currentTimeMillis());
	}

}
