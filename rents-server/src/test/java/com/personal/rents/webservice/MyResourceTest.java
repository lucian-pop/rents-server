//package com.personal.rents.webservice;
//
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.MediaType;
//
//import junit.framework.TestCase;
//
//import org.junit.Before;
//
//import com.personal.rents.util.TestUtil;
//
//public class MyResourceTest extends TestCase {
//
//    private WebTarget target;
//
//    @Before
//    public void setUp() throws Exception {
//        target = TestUtil.buildWebTarget();
//    }
//
//    public void testGetIt() {
//        String responseMsg = target.path("myresource").request(MediaType.APPLICATION_JSON)
//        		.get(String.class);
//
//        assertEquals("Got it!", responseMsg);
//    }
//}
