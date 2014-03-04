package com.personal.rents.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.personal.rents.model.RentImage;

public interface RentImageDAO {

	public static final String INSERT = "insert into rent_image(rentId, rentImageURI) values(#{rentId},"
			+ "#{rentImageURI})";
	
	public static final String SELECT_BY_ID = "select * from rent_image where rentImageId=#{rentImageId}";
	
	public static final String DELETE_BY_ID = "delete from rent_image where rentImageId=#{rentImageId}";
	
	public static final String UPDATE_RENT_IMAGE_URI = "update rent_image set rentImageURI=#{rentImageURI}"
			+ " where rentImageId=#{rentImageId}";
	
	@Insert(INSERT)
	@Options(useGeneratedKeys = true, keyProperty="rentImageId")
	public int insertRentImage(RentImage rentImage);
	
	@Select(SELECT_BY_ID)
	public RentImage getRentImage(@Param("rentImageId") int rentImageId);
	
	@Delete(DELETE_BY_ID)
	public int deleteRentImage(@Param("rentImageId") int rentImageId);
	
	@Update(UPDATE_RENT_IMAGE_URI)
	public int updateRentImageURI(@Param("rentImageId") int rentImageId,
			@Param("rentImageURI") String rentImageURI);
}
