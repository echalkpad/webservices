package com.giogar.jersey;

import com.giogar.model.Order;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/simple-customer")
public class SimpleCustomerService {

    private static Logger LOG = Logger.getLogger(SimpleCustomerService.class);

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response save(Order order) {
//        LOG.info("Received: " + order);
        System.out.println("Received: " + order);
        return Response.ok("OK").build();
    }

}