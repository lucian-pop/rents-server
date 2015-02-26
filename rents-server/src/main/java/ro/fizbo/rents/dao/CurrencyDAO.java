package ro.fizbo.rents.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface CurrencyDAO {

	public static final String UPDATE_CONVERSION_RATE = "update currency set"
			+ " currencyConversionRate=#{conversionRate} where currencyName like #{currencyName}";
	
	@Update(UPDATE_CONVERSION_RATE)
	public int updateConversionRate(@Param("conversionRate") BigDecimal conversionRate, 
			@Param("currencyName") String currencyName);
}
