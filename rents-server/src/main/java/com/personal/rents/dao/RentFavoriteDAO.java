package com.personal.rents.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.personal.rents.model.RentFavorite;

public interface RentFavoriteDAO {

	public static final String INSERT = "insert into rent_favorite"
			+ " values(#{accountId}, #{rentId}, #{rentFavoriteAddDate})";
	
	public static final String DELETE_BY_IDS = 
			"delete from rent_favorite where accountId=#{accountId} and rentId=#{rentId}";
	
	public static final String SELECT_BY_IDS =
			"select * from rent_favorite where accountId=#{accountId} and rentId=#{rentId}";
	
	public static final String DATE_RESTRICTION = " order by rentFavoriteAddDate desc, rentId desc"
			+ " limit #{pageSize}";
	
	public static final String SELECT_COUNT_USER_FAVORITES = "select count(*) from rent_favorite"
			+ " where accountId=#{accountId}";
	
	@Insert(INSERT)
	public int addEntry(@Param("accountId") int accountId, @Param("rentId") int rentId, 
			@Param("rentFavoriteAddDate") Date rentFavoriteAddDate);
	
	@Delete(DELETE_BY_IDS)
	public int deleteEntry(@Param("accountId") int accountId, @Param("rentId") int rentId);
	
	@Select(SELECT_BY_IDS)
	public RentFavorite getEntry(@Param("accountId") int accountId, @Param("rentId") int rentId);
	
	@Select(SELECT_COUNT_USER_FAVORITES)
	public int getNoOfUserFavoriteRents(@Param("accountId") int accountId);

}
