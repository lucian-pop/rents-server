package ro.fizbo.rents.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface RentStatisticsDAO {
	
	public static final String UPDATE_INSERT = "insert into rent_statistics (rentId, rentViewsNo)"
			+ " values(#{rentId}, 1) on duplicate key update rentViewsNo = rentViewsNo + 1";
	
	@Update(UPDATE_INSERT)
	public int updateInsert(@Param("rentId") int rentId);

}
