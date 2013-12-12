package com.personal.rents.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.personal.rents.model.RentImage;

public interface RentImageDAO {

	public static final String INSERT = "insert into rent_image(rentId, imageURI) values(#{rentId},"
			+ "#{imageURI})";
	
	public static final String DELETE_BY_ID = "delete from rent_image where rent_image.id=#{id}";
	
	@Insert(INSERT)
	@Options(useGeneratedKeys = true)
	public int insertRentImage(RentImage rentImage);
	
	@Delete(DELETE_BY_ID)
	public int deleteRentImage(@Param("id") int id);
}
