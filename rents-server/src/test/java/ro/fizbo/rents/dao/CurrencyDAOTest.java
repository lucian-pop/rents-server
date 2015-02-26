package ro.fizbo.rents.dao;

import java.math.BigDecimal;

import org.apache.ibatis.session.SqlSession;

import ro.fizbo.rents.model.Currency;
import ro.fizbo.rents.util.TestUtil;
import junit.framework.TestCase;

public class CurrencyDAOTest extends TestCase {
	
	public void testUpdateConversionRate() {
		BigDecimal conversionRate = new BigDecimal("0.2224");
		
		int result = -1;
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			CurrencyDAO currencyDAO = session.getMapper(CurrencyDAO.class);
			result = currencyDAO.updateConversionRate(conversionRate, Currency.RON.toString());
		} finally {
			session.close();
		}
		
		assertTrue(result > 0);
	}

}
