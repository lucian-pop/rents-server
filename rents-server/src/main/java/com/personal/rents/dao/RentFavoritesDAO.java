package com.personal.rents.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.personal.rents.model.RentFavorite;

public interface RentFavoritesDAO {

	public static final String INSERT = "insert into rent_favorites values(#{accountId}, #{rentId})";
	
	public static final String DELETE_BY_IDS = 
			"delete from rent_favorites where accountId=#{accountId} and rentId=#{rentId}";
	
	public static final String SELECT_BY_IDS =
			"select * from rent_favorites where accountId=#{accountId} and rentId=#{rentId}";
	
	@Insert(INSERT)
	public int addEntry(@Param("accountId") int accountId, @Param("rentId") int rentId);
	
	@Delete(DELETE_BY_IDS)
	public int deleteEntry(@Param("accountId") int accountId, @Param("rentId") int rentId);
	
	@Select(SELECT_BY_IDS)
	public RentFavorite getEntry(@Param("accountId") int accountId, @Param("rentId") int rentId);
}
