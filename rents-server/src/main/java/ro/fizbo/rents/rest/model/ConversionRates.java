package ro.fizbo.rents.rest.model;

import java.util.Date;
import java.util.List;

public class ConversionRates {
	
	public Query query;
	
	public class Query {
		
		public int count;
		
		public Date date;
		
		public Result results;
	
	}
	
	public class Result {
		
		public List<ConversionRate> rate;
		
	}
	
	public class ConversionRate {
		
		public String id;
		
		public String Rate;
	}

}
