package ro.fizbo.rents;

import java.util.logging.Logger;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import ro.fizbo.rents.webservice.jsonprovider.GsonMessageBodyHandler;

public class ResourcesRegistrar extends ResourceConfig {

	public ResourcesRegistrar() {
		register(GsonMessageBodyHandler.class);
		register(MultiPartFeature.class);
		registerInstances(new LoggingFilter(Logger.getLogger(ResourcesRegistrar.class.getName()), true));
	}

}
