package com.personal.rents.dao;

import org.apache.ibatis.session.SqlSession;

import com.personal.rents.model.Address;
import com.personal.rents.util.TestUtil;

import junit.framework.TestCase;

public class AddressDAOTest extends TestCase {

	public void testInsertAddress() {
		Address address = new Address();
		address.setAddressStreetNo("68A");
		address.setAddressStreetName("Observatorului");
		address.setAddressNeighbourhood("Zorilor");
		address.setAddressLocality("Cluj-Napoca");
		address.setAddressAdmAreaL1("Cluj");
		address.setAddressCountry("Romania");
		address.setAddressLatitude(46.7667);
		address.setAddressLongitude(23.5833);
		address.setAddressBuilding("C3");
		address.setAddressStaircase("2A");
		address.setAddressFloor((short) 4);
		address.setAddressAp("12B");
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int result = -1;
		try {
			AddressDAO addressDAO = session.getMapper(AddressDAO.class);
			result = addressDAO.insertAddress(address);
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
		
		// delete the added address
		session = TestUtil.getSqlSessionFactory().openSession();
		try {
			AddressDAO addressDAO = session.getMapper(AddressDAO.class);
			addressDAO.deleteAddress(address.getAddressId());
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void testUpdateAddress() {
		Address address = new Address();
		address.setAddressStreetNo("68A");
		address.setAddressStreetName("Observatorului");
		address.setAddressNeighbourhood("Zorilor");
		address.setAddressLocality("Cluj-Napoca");
		address.setAddressAdmAreaL1("Cluj");
		address.setAddressCountry("Romania");
		address.setAddressLatitude(46.7667);
		address.setAddressLongitude(23.5833);
		address.setAddressBuilding("C3");
		address.setAddressStaircase("2A");
		address.setAddressFloor((short) 4);
		address.setAddressAp("12B");
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int result = -1;
		try {
			AddressDAO addressDAO = session.getMapper(AddressDAO.class);
			result = addressDAO.insertAddress(address);
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
		
		//update the address
		address.setAddressStreetNo("69A");
		address.setAddressStreetName("Clinicilor");
		address.setAddressNeighbourhood("");
		address.setAddressLocality("Cluj-Napoca");
		address.setAddressAdmAreaL1("Cluj");
		address.setAddressLatitude(46.7668);
		address.setAddressLongitude(23.5834);
		address.setAddressBuilding("");
		address.setAddressStaircase("");
		address.setAddressFloor((short) 0);
		address.setAddressAp("");
		
		session = TestUtil.getSqlSessionFactory().openSession();
		int updateCount = -1;
		try {
			updateCount = session.update("AddressMapper.updateAddress", address);
			session.commit();
			
			AddressDAO addressDAO = session.getMapper(AddressDAO.class);
			address = addressDAO.getAddress(address.getAddressId());
		} finally {
			session.close();
		}
		
		assertTrue(updateCount == 1);

		// delete the added address
		session = TestUtil.getSqlSessionFactory().openSession();
		try {
			AddressDAO addressDAO = session.getMapper(AddressDAO.class);
			addressDAO.deleteAddress(address.getAddressId());
			session.commit();
		} finally {
			session.close();
		}
	}
}
