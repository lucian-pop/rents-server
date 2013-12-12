package com.personal.rents.dao;

import org.apache.ibatis.session.SqlSession;

import com.personal.rents.model.Address;
import com.personal.rents.util.TestUtil;

import junit.framework.TestCase;

public class AddressDAOTest extends TestCase {

	public void testInsertAddress() {
		
		Address address = new Address();
		address.setStreetNo("68A");
		address.setStreetName("Observatorului");
		address.setNeighbourhood("Zorilor");
		address.setLocality("Cluj-Napoca");
		address.setAdmAreaL1("Cluj");
		address.setCountry("Romania");
		address.setLatitude(46.7667);
		address.setLongitude(23.5833);
		address.setBuilding("C3");
		address.setStaircase("2A");
		address.setFloor((short) 4);
		address.setAp("12B");
		
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
			addressDAO.deleteAddress(address.getId());
			session.commit();
		} finally {
			session.close();
		}
	}

}
