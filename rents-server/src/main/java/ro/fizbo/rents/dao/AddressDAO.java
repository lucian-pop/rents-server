package ro.fizbo.rents.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import ro.fizbo.rents.model.Address;

public interface AddressDAO {
	
	public static final String INSERT = "insert into address (addressStreetNo, addressStreetName,"
			+ "addressNeighbourhood, addressSublocality, addressLocality, addressAdmAreaL1,"
			+ "addressCountry, addressLatitude, addressLongitude, addressBuilding, addressStaircase,"
			+ "addressFloor, addressAp) values(#{addressStreetNo}, #{addressStreetName},"
			+ "#{addressNeighbourhood}, #{addressSublocality}, #{addressLocality},"
			+ "#{addressAdmAreaL1}, #{addressCountry}, #{addressLatitude}, #{addressLongitude},"
			+ "#{addressBuilding}, #{addressStaircase}, #{addressFloor}, #{addressAp})";
	
	public static final String SELECT_BY_ID = "select * from address where address.addressId="
			+ "#{addressId}";
	
	public static final String DELETE_BY_ID = "delete from address where address.addressId="
			+ "#{addressId}";

	@Insert(INSERT)
	@Options(useGeneratedKeys = true, keyProperty="addressId")
	public int insertAddress(Address address);
	
	@Delete(DELETE_BY_ID)
	public int deleteAddress(@Param("addressId") int addressId);
	
	@Select(SELECT_BY_ID)
	public Address getAddress(@Param("addressId") int addressId);
}
