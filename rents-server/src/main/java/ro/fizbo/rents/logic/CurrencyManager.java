package ro.fizbo.rents.logic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.view.RentFavoriteView;
import ro.fizbo.rents.rest.client.CurrenciesClient;
import ro.fizbo.rents.rest.model.ConversionRates;
import ro.fizbo.rents.rest.model.ConversionRates.ConversionRate;
import ro.fizbo.rents.task.GetConversionRatesTask;

public final class CurrencyManager {

	/** Conversion rates map.*/
	private static Map<String, BigDecimal> conversionRatesMap = new HashMap<String, BigDecimal>();
	
	public static Map<String, BigDecimal> getConversionRatesMap() {
		return conversionRatesMap;
	}
	
	public static void updateConversionRates() {
		ConversionRates conversionRates = CurrenciesClient.getConversionRates();
		if(conversionRates == null || conversionRates.query.count == 0) {
			return;
		}
		
		for(ConversionRate conversionRate : conversionRates.query.results.rate) {
			conversionRatesMap.put(conversionRate.id, new BigDecimal(conversionRate.Rate));
		}
	}

	public static void scheduleConversionRatesUpdate() {
		SchedulerManager.execute(new GetConversionRatesTask(), 0);
	}
	
	public static void convertRentPrice(String requestedCurrency, Rent rent) {
		if(rent.getRentCurrency().equals(requestedCurrency)) {
			return;
		}
		BigDecimal conversionRate = conversionRatesMap.get(rent.getRentCurrency() 
				+ requestedCurrency);
		int convertedRentPrice = conversionRate.multiply(new BigDecimal(rent.getRentPrice()))
				.intValue();
		rent.setRentPrice(convertedRentPrice);
		rent.setRentCurrency(requestedCurrency);
	}

	public static void convertRentsListPrices(String requestedCurrency, List<Rent> rents) {
		int convertedRentPrice = 0;
		BigDecimal conversionRate = BigDecimal.ZERO;
		for(Rent rent : rents) {
			if(rent.getRentCurrency().equals(requestedCurrency)) {
				continue;
			}
			conversionRate = conversionRatesMap.get(rent.getRentCurrency() 
					+ requestedCurrency);
			convertedRentPrice = conversionRate.multiply(new BigDecimal(rent.getRentPrice()))
					.intValue();
			rent.setRentPrice(convertedRentPrice);
			rent.setRentCurrency(requestedCurrency);
		}
	}
	
	public static void convertRentsFavoritesListPrices(String requestedCurrency, 
			List<RentFavoriteView> rentsFavoritesViews) {
		int convertedRentPrice = 0;
		BigDecimal conversionRate = BigDecimal.ZERO;
		for(RentFavoriteView rentFavoriteView : rentsFavoritesViews) {
			if(rentFavoriteView.getRent().getRentCurrency().equals(requestedCurrency)) {
				continue;
			}
			conversionRate = conversionRatesMap.get(rentFavoriteView.getRent().getRentCurrency() 
					+ requestedCurrency);
			convertedRentPrice = conversionRate.multiply(new BigDecimal(rentFavoriteView.getRent()
					.getRentPrice())).intValue();
			rentFavoriteView.getRent().setRentPrice(convertedRentPrice);
			rentFavoriteView.getRent().setRentCurrency(requestedCurrency);
		}
	}
}
