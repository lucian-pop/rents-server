package com.personal.rents.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.personal.rents.model.Address;

public interface AddressDAO {
	
	public static final String INSERT = "insert into address (addressStreetNo, addressStreetName,"
			+ "addressNeighbourhood, addressSublocality, addressLocality, addressAdmAreaL1,"
			+ "addressCountry, addressLatitude, addressLongitude, addressBuilding, addressStaircase,"
			+ "addressFloor, addressAp) values(#{addressStreetNo}, #{addressStreetName},"
			+ "#{addressNeighbourhood}, #{addressSublocality}, #{addressLocality},"
			+ "#{addressAdmAreaL1}, #{addressCountry}, #{addressLatitude}, #{addressLongitude},"
			+ "#{addressBuilding}, #{addressStaircase}, #{addressFloor}, #{addressAp})";
	
	public static final String DELETE_BY_ID = "delete from address where address.addressId="
			+ "#{addressId}";

	@Insert(INSERT)
	@Options(useGeneratedKeys = true, keyProperty="addressId")
	public int insertAddress(Address address);
	
	@Delete(DELETE_BY_ID)
	public int deleteAddress(@Param("addressId") int addressId);
}
