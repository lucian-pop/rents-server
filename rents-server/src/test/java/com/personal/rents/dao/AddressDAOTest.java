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

}
