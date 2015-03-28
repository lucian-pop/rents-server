package ro.fizbo.rents.client.rest;

import ro.fizbo.rents.client.rest.CurrenciesClient;
import ro.fizbo.rents.model.rest.ConversionRates;
import ro.fizbo.rents.model.rest.ConversionRates.ConversionRate;
import junit.framework.TestCase;

public class CurrenciesClientTest extends TestCase {
	
	public void testGetConversionRates() {
		ConversionRates result = CurrenciesClient.getConversionRates();
		assertNotNull(result);
		for(ConversionRate conversionRate : result.query.results.rate) {
			assertNotNull(conversionRate.Rate);
		}
	}
}
