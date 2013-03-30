package com.giogar.mainclasses;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainTest extends JerseyTest {

    public MainTest()throws Exception {
        super("com.giogar.jersey");
    }

    @Test
    public void testHelloWorld() {
        //https://gist.github.com/seratch/1080390
        WebResource webResource = resource();
        String responseMsg = webResource.path("timeoftheday/asplaintext/gio").get(String.class);
        System.out.println(responseMsg);
//        assertEquals("Hello World", responseMsg);
    }

}