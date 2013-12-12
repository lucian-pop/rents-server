package com.personal.rents.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.personal.rents.model.Address;

public interface AddressDAO {
	
	public static final String INSERT = "insert into address (streetNo, streetName, neighbourhood,"
			+ "sublocality, locality, admAreaL1, country, latitude, longitude, building, staircase,"
			+ "floor, ap) values(#{streetNo}, #{streetName}, #{neighbourhood}, #{sublocality},"
			+ "#{locality}, #{admAreaL1}, #{country}, #{latitude}, #{longitude}, #{building},"
			+ "#{staircase}, #{floor}, #{ap})";
	
	public static final String DELETE_BY_ID = "delete from address where address.id=#{addressId}";

	@Insert(INSERT)
	@Options(useGeneratedKeys = true)
	public int insertAddress(Address address);
	
	@Delete(DELETE_BY_ID)
	public int deleteAddress(@Param("addressId") int addressId);
}
