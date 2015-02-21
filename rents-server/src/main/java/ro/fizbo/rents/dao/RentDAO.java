package ro.fizbo.rents.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.view.RentFavoriteView;

public interface RentDAO {

	public static final String INSERT = "insert into rent (accountId, addressId, rentForm,"
			+ " rentPrice, rentCurrency, rentPhone, rentType, rentRooms, rentGuests, rentSingleBeds,"
			+ " rentDoubleBeds, rentBaths, rentArchitecture, rentSurface, rentAge, rentParty, "
			+ " rentDescription, rentPetsAllowed, rentParkingPlace, rentSmokersAllowed, rentAddDate,"
			+ " rentStatus)"
			+ " values(#{accountId}, #{addressId}, #{rentForm}, #{rentPrice}, #{rentCurrency},"
			+ " #{rentPhone}, #{rentType}, #{rentRooms}, #{rentGuests}, #{rentSingleBeds},"
			+ " #{rentDoubleBeds}, #{rentBaths}, #{rentArchitecture}, #{rentSurface}, #{rentAge},"
			+ " #{rentParty}, #{rentDescription}, #{rentPetsAllowed}, #{rentParkingPlace},"
			+ " #{rentSmokersAllowed}, #{rentAddDate}, #{rentStatus})";
	
	public static final String EAGER_SELECT_BY_ID= "select rent.*, address.*,"
			+ " rentImageId, concat(#{appURL}, rentImageURI) as rentImageURI from rent"
			+ " inner join address on rent.addressId=address.addressId"
			+ "	left join rent_image on rent.rentId=rent_image.rentId"
			+ " where rent.rentId = #{rentId}";
	
	public static final String DELETE_BY_ID = "delete from rent where rent.rentId=#{rentId}";
	
	public static final String LIGHT_RENT_PROJECTION = "select rent.rentId, rent.rentForm,"
			+ " rent.rentPrice, rentCurrency, rentSurface, rentRooms, rentBaths, rentType,"
			+ " rentArchitecture, rentAddDate, rentStatus,"
			+ "address.addressId, addressStreetName, addressNeighbourhood, addressLatitude,"
			+ "addressLongitude,"
			+ "concat(#{appURL}, rentImageURI) as rentImageURI, min(rent_image.rentImageId)"
			+ " from rent inner join address on rent.addressId=address.addressId"
			+ "	left join rent_image on rent.rentId=rent_image.rentId";
	
	public static final String DATE_PAGINATION_CONDITION = "rent.rentAddDate <= #{lastRentDate}"
			+ " and (rent.rentId < #{lastRentId} or rent.rentAddDate < #{lastRentDate})";
	
	public static final String DATE_RESTRICTION = " group by rent.rentId"
			+ " order by rent.rentAddDate desc, rent.rentId desc limit #{pageSize}";
	
	public static final String SELECT_COUNT_BY_MAP_BOUNDARIES = "select count(*) from rent "
			+ " inner join address on rent.addressId=address.addressId"
			+ " where address.addressLatitude between #{minLatitude} and #{maxLatitude}"
			+ " and address.addressLongitude between #{minLongitude} and #{maxLongitude}"
			+ " and rentStatus=#{rentStatus} and rentForm=#{rentForm}";
	
	public static final String SELECT_BY_MAP_BOUNDARIES = LIGHT_RENT_PROJECTION
			+ " where address.addressLatitude between #{minLatitude} and #{maxLatitude}"
			+ " and address.addressLongitude between #{minLongitude} and #{maxLongitude}"
			+ " and rentStatus=#{rentStatus} and rentForm=#{rentForm}"
			+ DATE_RESTRICTION;
	
	public static final String SELECT_NEXT_PAGE_BY_MAP_BOUNDARIES = LIGHT_RENT_PROJECTION
			+ " where address.addressLatitude between #{minLatitude} and #{maxLatitude}"
			+ " and address.addressLongitude between #{minLongitude} and #{maxLongitude}"
			+ " and rentStatus=#{rentStatus} and rentForm=#{rentForm}"
			+ " and " + DATE_PAGINATION_CONDITION + DATE_RESTRICTION;
	
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
			+ " and (rentPetsAllowed=#{lowRentPetsAllowed} or rentPetsAllowed=#{highRentPetsAllowed})"
			+ " and rentStatus=#{rentStatus} and rentForm=#{rentForm}";
	
	public static final String SELECT_BY_CRITERIA = LIGHT_RENT_PROJECTION 
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
			+ " and (rentPetsAllowed=#{lowRentPetsAllowed} or rentPetsAllowed=#{highRentPetsAllowed})"
			+ " and rentStatus=#{rentStatus} and rentForm=#{rentForm}"
			+ DATE_RESTRICTION;
	
	public static final String SELECT_NEXT_PAGE_BY_CRITERIA = LIGHT_RENT_PROJECTION 
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
			+ " and (rentPetsAllowed=#{lowRentPetsAllowed} or rentPetsAllowed=#{highRentPetsAllowed})"
			+ " and rentStatus=#{rentStatus} and rentForm=#{rentForm}"
			+ " and " + DATE_PAGINATION_CONDITION + DATE_RESTRICTION;
	
	public static final String SELECT_COUNT_USER_ADDED_RENTS = "select count(*) from rent"
			+ " where rent.accountId=#{accountId} and rent.rentStatus=#{rentStatus}";
	
	public static final String SELECT_USER_ADDED_RENTS = LIGHT_RENT_PROJECTION
			+ " where rent.accountId=#{accountId} and rent.rentStatus=#{rentStatus}"
			+ DATE_RESTRICTION;
	
	public static final String SELECT_USER_ADDED_RENTS_NEXT_PAGE = LIGHT_RENT_PROJECTION
			+ " where rent.accountId=#{accountId} and rent.rentStatus=#{rentStatus}"
			+ " and " + DATE_PAGINATION_CONDITION + DATE_RESTRICTION;
	
	public static final String LIGHT_RENT_FAVORITE_VIEW_PROJECTION = "select rent.rentId,"
			+ "rent.rentPrice, rentCurrency, rentSurface, rentRooms, rentBaths, rentType,"
			+ "rentArchitecture,rentAddDate, rentStatus,"
			+ "address.addressId, addressStreetNo, addressStreetName, addressLatitude,"
			+ "addressLongitude,"
			+ "concat(#{appURL}, rentImageURI) as rentImageURI, min(rent_image.rentImageId),"
			+ "rent_favorite.rentFavoriteAddDate"
			+ " from rent inner join rent_favorite on rent.rentId = rent_favorite.rentId" 
			+ " inner join address on rent.addressId=address.addressId"
			+ "	left join rent_image on rent.rentId=rent_image.rentId";
	
	public static final String SELECT_USER_FAVORITE_RENTS = LIGHT_RENT_FAVORITE_VIEW_PROJECTION 
			+ " where rent_favorite.accountId=#{accountId}"
			+ " group by rent.rentId"
			+ " order by rent_favorite.rentFavoriteAddDate desc limit #{pageSize}";
	
	public static final String SELECT_USER_FAVORITE_RENTS_NEXT_PAGE = LIGHT_RENT_FAVORITE_VIEW_PROJECTION 
			+ " where rent_favorite.accountId=#{accountId}"
			+ " and rent_favorite.rentFavoriteAddDate < #{lastRentFavoriteDate}"
			+ " group by rent.rentId"
			+ " order by rent_favorite.rentFavoriteAddDate desc limit #{pageSize}";

	@Insert(INSERT)
	@Options(useGeneratedKeys = true, keyProperty="rentId")
	public int insertRent(Rent rent);
	
	@Select(EAGER_SELECT_BY_ID)
	@ResultMap("RentMapper.FullRentMap")
	public Rent getDetailedRent(@Param("rentId") int rentId, @Param("appURL") String appURL);
	
	@Delete(DELETE_BY_ID)
	public int deleteRent(@Param("rentId") int rentId);
	
	@Select(SELECT_COUNT_BY_MAP_BOUNDARIES)
	public int getNoOfRentsByMapBoundaries(@Param("minLatitude") double minLatitude,
			@Param("maxLatitude") double maxLatitude, @Param("minLongitude") double minLongitude,
			@Param("maxLongitude") double maxLongitude, @Param("rentStatus") byte rentStatus,
			@Param("rentForm") byte rentForm);
	
	@Select(SELECT_BY_MAP_BOUNDARIES)
	@ResultMap("RentMapper.LightRentMap")
	public List<Rent> getRentsByMapBoundaries(@Param("minLatitude") double minLatitude,
			@Param("maxLatitude") double maxLatitude, @Param("minLongitude") double minLongitude,
			@Param("maxLongitude") double maxLongitude, @Param("rentStatus") byte rentStatus,
			@Param("rentForm") byte rentForm, @Param("pageSize") int pageSize,
			@Param("appURL") String appURL);
	
	@Select(SELECT_NEXT_PAGE_BY_MAP_BOUNDARIES)
	@ResultMap("RentMapper.LightRentMap")
	public List<Rent> getRentsNextPageByMapBoundaries(@Param("minLatitude") double minLatitude,
			@Param("maxLatitude") double maxLatitude, @Param("minLongitude") double minLongitude,
			@Param("maxLongitude") double maxLongitude, @Param("lastRentDate") Date lastRentDate, 
			@Param("lastRentId") int lastRentId, @Param("rentStatus") byte rentStatus, 
			@Param("rentForm") byte rentForm, @Param("pageSize") int pageSize, 
			@Param("appURL") String appURL);
	
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
			@Param("maxAge") short maxAge, @Param("lowRentPetsAllowed") boolean lowRentPetsAllowed,
			@Param("highRentPetsAllowed") boolean highRentPetsAllowed,
			@Param("rentStatus") byte rentStatus, @Param("rentForm") byte rentForm);
	
	@Select(SELECT_BY_CRITERIA)
	@ResultMap("RentMapper.LightRentMap")
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
			@Param("maxAge") short maxAge, @Param("lowRentPetsAllowed") boolean lowRentPetsAllowed,
			@Param("highRentPetsAllowed") boolean highRentPetsAllowed,
			@Param("rentStatus") byte rentStatus, @Param("rentForm") byte rentForm,
			@Param("pageSize") int pageSize, @Param("appURL") String appURL);
	
	@Select(SELECT_NEXT_PAGE_BY_CRITERIA)
	@ResultMap("RentMapper.LightRentMap")
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
			@Param("maxAge") short maxAge, @Param("lowRentPetsAllowed") boolean lowRentPetsAllowed,
			@Param("highRentPetsAllowed") boolean highRentPetsAllowed,
			@Param("rentStatus") byte rentStatus, @Param("rentForm") byte rentForm,
			@Param("lastRentDate") Date lastRentDate, @Param("lastRentId") int lastRentId,
			@Param("pageSize") int pageSize, @Param("appURL") String appURL);
	
	@Select(SELECT_COUNT_USER_ADDED_RENTS)
	public int getNoOfUserAddedRents(@Param("accountId") int accountId, 
			@Param("rentStatus") byte rentStatus);
	
	@Select(SELECT_USER_ADDED_RENTS)
	@ResultMap("RentMapper.LightRentMap")
	public List<Rent> getUserAddedRents(@Param("accountId") int accountId, 
			@Param("rentStatus") byte rentStatus, @Param("pageSize") int pageSize,
			@Param("appURL") String appURL);
	
	@Select(SELECT_USER_ADDED_RENTS_NEXT_PAGE)
	@ResultMap("RentMapper.LightRentMap")
	public List<Rent> getUserAddedRentsNextPage(@Param("accountId") int accountId, 
			@Param("rentStatus") byte rentStatus, @Param("lastRentDate") Date lastRentDate, 
			@Param("lastRentId") int lastRentId, @Param("pageSize") int pageSize,
			@Param("appURL") String appURL);
	
	@Select(SELECT_USER_FAVORITE_RENTS)
	@ResultMap("RentMapper.LightRentFavoriteViewMap")
	public List<RentFavoriteView> getUserFavoriteRents(@Param("accountId") int accountId,
			@Param("pageSize") int pageSize, @Param("appURL") String appURL);
	
	@Select(SELECT_USER_FAVORITE_RENTS_NEXT_PAGE)
	@ResultMap("RentMapper.LightRentFavoriteViewMap")
	public List<RentFavoriteView> getUserFavoriteRentsNextPage(@Param("accountId") int accountId,
			@Param("lastRentFavoriteDate") Date lastRentFavoriteDate,
			@Param("pageSize") int pageSize, @Param("appURL") String appURL);
} 
