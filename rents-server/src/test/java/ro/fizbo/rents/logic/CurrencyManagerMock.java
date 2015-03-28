package ro.fizbo.rents.logic;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import ro.fizbo.rents.client.rest.CurrenciesClient;
import ro.fizbo.rents.dao.CurrencyDAO;
import ro.fizbo.rents.listener.ApplicationManager;
import ro.fizbo.rents.model.Currency;
import ro.fizbo.rents.model.CurrencyPair;
import ro.fizbo.rents.model.rest.ConversionRates;
import ro.fizbo.rents.model.rest.ConversionRates.ConversionRate;
import ro.fizbo.rents.util.TestUtil;

public class CurrencyManagerMock extends CurrencyManager {
	
	public static void updateConversionRates() {
		ConversionRates conversionRates = CurrenciesClient.getConversionRates();
		if(conversionRates == null || conversionRates.query.count == 0) {
			return;
		}
		Map<String, BigDecimal> conversionRatesMap = ApplicationManager.getConversionRatesMap();
		for(ConversionRate conversionRate : conversionRates.query.results.rate) {
			conversionRatesMap.put(conversionRate.id, new BigDecimal(conversionRate.Rate));
		}
		updateDatabaseConversionRates();
	}
	
	private static void updateDatabaseConversionRates() {
		Map<String, BigDecimal> conversionRates= ApplicationManager.getConversionRatesMap();
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			CurrencyDAO currencyDAO = session.getMapper(CurrencyDAO.class);
			currencyDAO.updateConversionRate(conversionRates.get(CurrencyPair.GBPEUR.toString()), 
					Currency.GBP.toString());
			currencyDAO.updateConversionRate(conversionRates.get(CurrencyPair.RONEUR.toString()), 
					Currency.RON.toString());
			currencyDAO.updateConversionRate(conversionRates.get(CurrencyPair.USDEUR.toString()), 
					Currency.USD.toString());
			session.commit();
		} finally {
			session.close();
		}
	}

}
