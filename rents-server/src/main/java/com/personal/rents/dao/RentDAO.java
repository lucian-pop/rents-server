package com.personal.rents.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.personal.rents.model.Rent;

public interface RentDAO {

	public static final String INSERT = "insert into rent (accountId, addressId, price,"
			+ "surface, rooms, baths, party, rentType, architecture, age, description, petsAllowed,"
			+ "phone, creationDate, rentStatus) values(#{accountId}, #{addressId}, #{price},"
			+ "#{surface}, #{rooms}, #{baths}, #{party}, #{rentType}, #{architecture}, #{age},"
			+ "#{description},#{petsAllowed}, #{phone}, #{creationDate}, #{rentStatus})";
	
	
	public static final String DELETE_BY_ID = "delete from rent where rent.id=#{rentId}";
	
	@Insert(INSERT)
	@Options(useGeneratedKeys = true)
	public int insertRent(Rent rent);
	
	@Delete(DELETE_BY_ID)
	public int deleteRent(@Param("rentId") int rentId);
			
} 
