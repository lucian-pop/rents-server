package com.personal.rents.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.personal.rents.model.Rent;

public interface RentDAO {

	public static final String INSERT = "insert into rent (accountId, addressId, rentPrice,"
			+ "rentSurface, rentRooms, rentBaths, rentParty, rentType, rentArchitecture, rentAge,"
			+ "rentDescription, rentPetsAllowed, rentPhone, rentAddDate, rentStatus)"
			+ " values(#{accountId}, #{addressId}, #{rentPrice}, #{rentSurface}, #{rentRooms},"
			+ "#{rentBaths}, #{rentParty}, #{rentType}, #{rentArchitecture}, #{rentAge},"
			+ "#{rentDescription}, #{rentPetsAllowed}, #{rentPhone}, #{rentAddDate}, #{rentStatus})";
	
	public static final String DELETE_BY_ID = "delete from rent where rent.rentId=#{rentId}";

	public static final String SELECT_BY_MAP_BOUNDARIES = "select rent.rentId, rentPrice,"
			+ "rentSurface, rentRooms, rentBaths, rentParty, rentType, rentArchitecture, rentAge,"
			+ "rentAddDate,"
			+ "address.addressId, addressStreetNo, addressStreetName, addressLatitude,"
			+ "addressLongitude,"
			+ "rentImageURI, min(rent_image.rentImageId)"
			+ " from rent inner join address on rent.addressId=address.addressId"
			+ "	left join rent_image on rent.rentId=rent_image.rentId"
			+ " where address.addressLatitude between #{minLatitude} and #{maxLatitude}"
			+ " and address.addressLongitude between #{minLongitude} and #{maxLongitude}"
			+ " group by rent.rentId order by rent.rentAddDate desc, rent.rentId desc limit #{pageSize}";
	
	public static final String SELECT_NEXT_PAGE_BY_MAP_BOUNDARIES = "select rent.rentId, rentPrice,"
			+ "rentSurface, rentRooms, rentBaths, rentParty, rentType, rentArchitecture, rentAge,"
			+ "rentAddDate,"
			+ "address.addressId, addressStreetNo, addressStreetName, addressLatitude,"
			+ "addressLongitude,"
			+ "rentImageURI, min(rent_image.rentImageId)"
			+ " from rent inner join address on rent.addressId=address.addressId"
			+ "	left join rent_image on rent.rentId=rent_image.rentId"
			+ " where address.addressLatitude between #{minLatitude} and #{maxLatitude}"
			+ " and address.addressLongitude between #{minLongitude} and #{maxLongitude}"
			+ " and rent.rentAddDate <= #{lastRentDate} and (rent.rentId < #{lastRentId}"
			+ " or rent.rentAddDate < #{lastRentDate}) group by rent.rentId"
			+ " order by rent.rentAddDate desc, rent.rentId desc limit #{pageSize}";
	
	public static final String SELECT_COUNT_BY_MAP_BOUNDARIES = "select count(*) from rent "
			+ " inner join address on rent.addressId=address.addressId"
			+ " where address.addressLatitude between #{minLatitude} and #{maxLatitude}"
			+ " and address.addressLongitude between #{minLongitude} and #{maxLongitude}";

	@Insert(INSERT)
	@Options(useGeneratedKeys = true, keyProperty="rentId")
	public int insertRent(Rent rent);
	
	@Delete(DELETE_BY_ID)
	public int deleteRent(@Param("rentId") int rentId);
	
	@Select(SELECT_BY_MAP_BOUNDARIES)
	@ResultMap("RentResultMaps.LightRentMap")
	public List<Rent> getRentsByMapBoundaries(@Param("minLatitude") double minLatitude,
			@Param("maxLatitude") double maxLatitude, @Param("minLongitude") double minLongitude,
			@Param("maxLongitude") double maxLongitude, @Param("pageSize") int pageSize);
	
	@Select(SELECT_NEXT_PAGE_BY_MAP_BOUNDARIES)
	@ResultMap("RentResultMaps.LightRentMap")
	public List<Rent> getRentsNextPageByMapBoundaries(@Param("minLatitude") double minLatitude,
			@Param("maxLatitude") double maxLatitude, @Param("minLongitude") double minLongitude,
			@Param("maxLongitude") double maxLongitude, @Param("lastRentDate") Date lastRentDate, 
			@Param("lastRentId") int lastRentId, @Param("pageSize") int pageSize);
	
	@Select(SELECT_COUNT_BY_MAP_BOUNDARIES)
	public int getNoOfRentsByMapBoundaries(@Param("minLatitude") double minLatitude,
			@Param("maxLatitude") double maxLatitude, @Param("minLongitude") double minLongitude,
			@Param("maxLongitude") double maxLongitude);
} 
