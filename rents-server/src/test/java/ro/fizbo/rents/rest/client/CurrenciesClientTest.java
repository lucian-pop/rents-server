package ro.fizbo.rents.rest.client;

import ro.fizbo.rents.rest.model.ConversionRates;
import ro.fizbo.rents.rest.model.ConversionRates.ConversionRate;
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
