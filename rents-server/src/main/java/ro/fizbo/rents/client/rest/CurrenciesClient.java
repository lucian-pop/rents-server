package ro.fizbo.rents.client.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.fizbo.rents.model.CurrencyPair;
import ro.fizbo.rents.model.rest.ConversionRates;
import ro.fizbo.rents.webservice.jsonprovider.GsonMessageBodyHandler;
import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;

public class CurrenciesClient {
	
	private static final String SERVICE_URL = "http://query.yahooapis.com/v1/public";
	
	private static final String CURRENCIES_PATH="yql";
	
	private static final String QUERY_PARAM="q";
	
	private static String QUERY_VALUE = "select * from yahoo.finance.xchange"
			+ " where pair in ('%s')";
	
	private static final String FORMAT_PARAM="format";
	
	private static final String FORMAT_VALUE="json";
	
	private static final String ENV_PARAM="env";
	
	private static final String ENV_VALUE="store://datatables.org/alltableswithkeys";
	
	private static final String CALLBACK_PARAM="callback";
	
	private static final String CALLBACK_VALUE="";

	private static final WebTarget serviceTarget;
	
	static {
		QUERY_VALUE = String.format(QUERY_VALUE, buildQueryValue());
		final Client client = ClientBuilder.newBuilder()
				.register(GsonMessageBodyHandler.class)
				.build();
		
		serviceTarget = client.target(SERVICE_URL);
	}
	
	public static ConversionRates getConversionRates() {
		Response response = serviceTarget.path(CURRENCIES_PATH)
				.queryParam(QUERY_PARAM, QUERY_VALUE)
				.queryParam(FORMAT_PARAM, FORMAT_VALUE)
				.queryParam(ENV_PARAM, ENV_VALUE)
				.queryParam(CALLBACK_PARAM, CALLBACK_VALUE)
				.request(MediaType.APPLICATION_JSON).get();
		if(response.getStatus() == WebserviceResponseStatus.OK.getCode()) {
			return response.readEntity(ConversionRates.class);
		}
		return null;
	}
	
	private static final String buildQueryValue() {
		CurrencyPair[] currenciesPairs = CurrencyPair.values();
		StringBuilder currenciesPairsBuilder = new StringBuilder();
		int i = 0;
		for(CurrencyPair currencyPair : currenciesPairs) {
			currenciesPairsBuilder.append(currencyPair.toString());
			if(i < currenciesPairs.length - 1) {
				currenciesPairsBuilder.append(",");
			}
			i++;
		}
		
		return currenciesPairsBuilder.toString();
	}
}
