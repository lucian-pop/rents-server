package ro.fizbo.rents.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ro.fizbo.rents.model.Currency;
import ro.fizbo.rents.model.CurrencyPair;
import ro.fizbo.rents.model.Rent;
import junit.framework.TestCase;

public class CurrencyManagerTest extends TestCase {
	
	private static final int RENT_PRICE = 800;
	
	public void testUpdateConversionRates() {
		CurrencyManager.updateConversionRates();
		Map<String, BigDecimal> conversionRatesMap = CurrencyManager.getConversionRatesMap();
		assertTrue(conversionRatesMap.values().size() == CurrencyPair.values().length);
		for(BigDecimal conversionRate: conversionRatesMap.values()) {
			assertNotNull(conversionRate);
		}
	}
	
	public void testConvertRentPrice() {
		CurrencyManager.updateConversionRates();
		Rent rent = new Rent();
		rent.setRentCurrency(Currency.RON.toString());
		rent.setRentPrice(RENT_PRICE);
		for(Currency currency : Currency.values()) {
			CurrencyManager.convertRentPrice(currency.toString(),rent);
			assertTrue(rent.getRentPrice() > 0);
			assertTrue(rent.getRentCurrency().equals(currency.toString()));
			rent.setRentPrice(RENT_PRICE);
			rent.setRentCurrency(Currency.RON.toString());
		}
	}

	public void testConvertRentsListsPrices() {
		CurrencyManager.updateConversionRates();
		List<Rent> rents = new ArrayList<Rent>();
		Rent rent = null;
		for(int i = 0; i < 6; i++) {
			rent = new Rent();
			if(i == 0) {
				rent.setRentCurrency(Currency.GBP.toString());
			} else if(i == 1) {
				rent.setRentCurrency(Currency.USD.toString());
			} else if(i % 2 == 0) {
				rent.setRentCurrency(Currency.RON.toString());
			} else {
				rent.setRentCurrency(Currency.EUR.toString());
			}
			rent.setRentPrice((i + 1) * 100);
			rents.add(rent);
		}
		
		CurrencyManager.convertRentsListPrices(Currency.EUR.toString(), rents);
		
		for(Rent rentEntry : rents) {
			assertEquals(Currency.EUR.toString(), rentEntry.getRentCurrency());
			assertTrue(rentEntry.getRentPrice() > 0);
		}
	}
}
