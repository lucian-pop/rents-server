package com.personal.rents;

import org.glassfish.jersey.server.ResourceConfig;

import com.personal.rents.webservice.jsonprovider.GsonMessageBodyHandler;

public class MyApp extends ResourceConfig {

	public MyApp() {
		register(GsonMessageBodyHandler.class);
	}

}
