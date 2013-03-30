package com.giogar.mainclasses;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class SimpleMainClassTest {

	public static void main(String[] args) {

		Client c = Client.create();

		// plain text
		WebResource r = c
				.resource("http://localhost:8080/webservices/services/timeoftheday/asplaintext/mathew");
		System.out.println("Plain Text=>> " + r.get(String.class));

		// json
		r = c.resource("http://localhost:8080/webservices/services/timeoftheday/asjson/mathew");
		System.out.println("JSON=>> " + r.get(String.class));

		// xml
		r = c.resource("http://localhost:8080/webservices/services/timeoftheday/asxml/mathew");
		System.out.println("XML=>> " + r.get(String.class));

	}

}
