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
	
	public static final String LIGHT_PROJECTION = "select rent.rentId, rentPrice,"
			+ "rentSurface, rentRooms, rentBaths, rentParty, rentType, rentArchitecture, rentAge,"
			+ "rentPetsAllowed, rentAddDate, rentStatus,"
			+ "address.addressId, addressStreetNo, addressStreetName, addressLatitude,"
			+ "addressLongitude,"
			+ "rentImageURI, min(rent_image.rentImageId)"
			+ " from rent inner join address on rent.addressId=address.addressId"
			+ "	left join rent_image on rent.rentId=rent_image.rentId";
	
	public static final String DATE_PAGINATION_CONDITION = "rent.rentAddDate <= #{lastRentDate}"
			+ " and (rent.rentId < #{lastRentId} or rent.rentAddDate < #{lastRentDate})";
	
	public static final String DATE_RESTRICTION = "group by rent.rentId"
			+ " order by rent.rentAddDate desc, rent.rentId desc limit #{pageSize}";
	
	public static final String SELECT_COUNT_BY_MAP_BOUNDARIES = "select count(*) from rent "
			+ " inner join address on rent.addressId=address.addressId"
			+ " where address.addressLatitude between #{minLatitude} and #{maxLatitude}"
			+ " and address.addressLongitude between #{minLongitude} and #{maxLongitude}"
			+ " and rentStatus=#{rentStatus}";
	
	public static final String SELECT_BY_MAP_BOUNDARIES = LIGHT_PROJECTION
			+ " where address.addressLatitude between #{minLatitude} and #{maxLatitude}"
			+ " and address.addressLongitude between #{minLongitude} and #{maxLongitude}"
			+ " and rentStatus=#{rentStatus}"
			+ " " + DATE_RESTRICTION;
	
	public static final String SELECT_NEXT_PAGE_BY_MAP_BOUNDARIES = LIGHT_PROJECTION
			+ " where address.addressLatitude between #{minLatitude} and #{maxLatitude}"
			+ " and address.addressLongitude between #{minLongitude} and #{maxLongitude}"
			+ " and rentStatus=#{rentStatus}"
			+ " and " + DATE_PAGINATION_CONDITION + " " + DATE_RESTRICTION;
	
	public static final String SELECT_COUNT_BY_CRITERIA = "select count(*) from rent "
			+ " inner join address on rent.addressId=address.addressId"
			+ " where address.addressLatitude between #{minLatitude} and #{maxLatitude}"
			+ " and address.addressLongitude between #{minLongitude} and #{maxLongitude}"
			+ " and rentPrice between #{minPrice} and #{maxPrice}"
			+ " and rentSurface between #{minSurface} and #{maxSurface}"
			+ " and rentRooms between #{minRooms} and #{maxRooms}"
			+ " and rentBaths between #{minBaths} and #{maxBaths}"
			+ " and rentParty between #{minParty} and #{maxParty}"
			+ " and rentType between #{minType} and #{maxType}"
			+ " and rentArchitecture between #{minArchitecture} and #{maxArchitecture}"
			+ " and rentAge between #{minAge} and #{maxAge}"
			+ " and rentPetsAllowed=#{rentPetsAllowed}"
			+ " and rentStatus=#{rentStatus}";
	
	public static final String SELECT_BY_CRITERIA = LIGHT_PROJECTION 
			+ " where address.addressLatitude between #{minLatitude} and #{maxLatitude}"
			+ " and address.addressLongitude between #{minLongitude} and #{maxLongitude}"
			+ " and rentPrice between #{minPrice} and #{maxPrice}"
			+ " and rentSurface between #{minSurface} and #{maxSurface}"
			+ " and rentRooms between #{minRooms} and #{maxRooms}"
			+ " and rentBaths between #{minBaths} and #{maxBaths}"
			+ " and rentParty between #{minParty} and #{maxParty}"
			+ " and rentType between #{minType} and #{maxType}"
			+ " and rentArchitecture between #{minArchitecture} and #{maxArchitecture}"
			+ " and rentAge between #{minAge} and #{maxAge}"
			+ " and rentPetsAllowed=#{rentPetsAllowed}"
			+ " and rentStatus=#{rentStatus}"
			+ " " + DATE_RESTRICTION;
	
	public static final String SELECT_NEXT_PAGE_BY_CRITERIA = LIGHT_PROJECTION 
			+ " where address.addressLatitude between #{minLatitude} and #{maxLatitude}"
			+ " and address.addressLongitude between #{minLongitude} and #{maxLongitude}"
			+ " and rentPrice between #{minPrice} and #{maxPrice}"
			+ " and rentSurface between #{minSurface} and #{maxSurface}"
			+ " and rentRooms between #{minRooms} and #{maxRooms}"
			+ " and rentBaths between #{minBaths} and #{maxBaths}"
			+ " and rentParty between #{minParty} and #{maxParty}"
			+ " and rentType between #{minType} and #{maxType}"
			+ " and rentArchitecture between #{minArchitecture} and #{maxArchitecture}"
			+ " and rentAge between #{minAge} and #{maxAge}"
			+ " and rentPetsAllowed=#{rentPetsAllowed}"
			+ " and rentStatus=#{rentStatus}"
			+ " and " + DATE_PAGINATION_CONDITION + " " + DATE_RESTRICTION;

	@Insert(INSERT)
	@Options(useGeneratedKeys = true, keyProperty="rentId")
	public int insertRent(Rent rent);
	
	@Delete(DELETE_BY_ID)
	public int deleteRent(@Param("rentId") int rentId);
	
	@Select(SELECT_COUNT_BY_MAP_BOUNDARIES)
	public int getNoOfRentsByMapBoundaries(@Param("minLatitude") double minLatitude,
			@Param("maxLatitude") double maxLatitude, @Param("minLongitude") double minLongitude,
			@Param("maxLongitude") double maxLongitude, @Param("rentStatus") int rentStatus);
	
	@Select(SELECT_BY_MAP_BOUNDARIES)
	@ResultMap("RentResultMaps.LightRentMap")
	public List<Rent> getRentsByMapBoundaries(@Param("minLatitude") double minLatitude,
			@Param("maxLatitude") double maxLatitude, @Param("minLongitude") double minLongitude,
			@Param("maxLongitude") double maxLongitude, @Param("rentStatus") int rentStatus,
			@Param("pageSize") int pageSize);
	
	@Select(SELECT_NEXT_PAGE_BY_MAP_BOUNDARIES)
	@ResultMap("RentResultMaps.LightRentMap")
	public List<Rent> getRentsNextPageByMapBoundaries(@Param("minLatitude") double minLatitude,
			@Param("maxLatitude") double maxLatitude, @Param("minLongitude") double minLongitude,
			@Param("maxLongitude") double maxLongitude, @Param("lastRentDate") Date lastRentDate, 
			@Param("lastRentId") int lastRentId, @Param("rentStatus") int rentStatus, 
			@Param("pageSize") int pageSize);
	
	@Select(SELECT_COUNT_BY_CRITERIA)
	public int searchResultSize(@Param("minLatitude") double minLatitude,
			@Param("maxLatitude") double maxLatitude, @Param("minLongitude") double minLongitude,
			@Param("maxLongitude") double maxLongitude, @Param("minPrice") int minPrice, 
			@Param("maxPrice") int maxPrice, @Param("minSurface") int minSurface, 
			@Param("maxSurface") int maxSurface, @Param("minRooms") short minRooms,
			@Param("maxRooms") short maxRooms, @Param("minBaths") short minBaths,
			@Param("maxBaths") short maxBaths, @Param("minParty") byte minParty,
			@Param("maxParty") byte maxParty, @Param("minType") byte minType, 
			@Param("maxType") byte maxType, @Param("minArchitecture") byte minArchitecture,
			@Param("maxArchitecture") byte maxArchitecture, @Param("minAge") short minAge,
			@Param("maxAge") short maxAge, @Param("rentPetsAllowed") boolean rentPetsAllowed,
			@Param("rentStatus") byte rentStatus);
	
	@Select(SELECT_BY_CRITERIA)
	@ResultMap("RentResultMaps.LightRentMap")
	public List<Rent> search(@Param("minLatitude") double minLatitude,
			@Param("maxLatitude") double maxLatitude, @Param("minLongitude") double minLongitude,
			@Param("maxLongitude") double maxLongitude, @Param("minPrice") int minPrice, 
			@Param("maxPrice") int maxPrice, @Param("minSurface") int minSurface, 
			@Param("maxSurface") int maxSurface, @Param("minRooms") short minRooms,
			@Param("maxRooms") short maxRooms, @Param("minBaths") short minBaths,
			@Param("maxBaths") short maxBaths, @Param("minParty") byte minParty,
			@Param("maxParty") byte maxParty, @Param("minType") byte minType, 
			@Param("maxType") byte maxType, @Param("minArchitecture") byte minArchitecture,
			@Param("maxArchitecture") byte maxArchitecture, @Param("minAge") short minAge,
			@Param("maxAge") short maxAge, @Param("rentPetsAllowed") boolean rentPetsAllowed,
			@Param("rentStatus") byte rentStatus, @Param("pageSize") int pageSize);
	
	@Select(SELECT_NEXT_PAGE_BY_CRITERIA)
	@ResultMap("RentResultMaps.LightRentMap")
	public List<Rent> searchNextPage(@Param("minLatitude") double minLatitude,
			@Param("maxLatitude") double maxLatitude, @Param("minLongitude") double minLongitude,
			@Param("maxLongitude") double maxLongitude, @Param("minPrice") int minPrice, 
			@Param("maxPrice") int maxPrice, @Param("minSurface") int minSurface, 
			@Param("maxSurface") int maxSurface, @Param("minRooms") short minRooms,
			@Param("maxRooms") short maxRooms, @Param("minBaths") short minBaths,
			@Param("maxBaths") short maxBaths, @Param("minParty") byte minParty,
			@Param("maxParty") byte maxParty, @Param("minType") byte minType, 
			@Param("maxType") byte maxType, @Param("minArchitecture") byte minArchitecture,
			@Param("maxArchitecture") byte maxArchitecture, @Param("minAge") short minAge,
			@Param("maxAge") short maxAge, @Param("rentPetsAllowed") boolean rentPetsAllowed,
			@Param("rentStatus") byte rentStatus, @Param("lastRentDate") Date lastRentDate, 
			@Param("lastRentId") int lastRentId, @Param("pageSize") int pageSize);
} 
