package com.personal.rents;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.personal.rents.webservice.jsonprovider.GsonMessageBodyHandler;

public class ResourcesRegistrar extends ResourceConfig {

	public ResourcesRegistrar() {
		register(GsonMessageBodyHandler.class);
		register(MultiPartFeature.class);
	}

}
