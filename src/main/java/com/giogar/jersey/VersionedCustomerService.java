package com.giogar.jersey;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/customer")
public class VersionedCustomerService {

    private static Logger LOG = Logger.getLogger(VersionedCustomerService.class);

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response save(com.giogar.model.version1_0.Customer customer) {
        LOG.info("Received: " + customer);
        return Response.ok("OK").build();
    }

    @POST
    @Path("save")
    @Consumes("application/vnd.customer+json")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveCustomType(com.giogar.model.version1_0.Customer customer) {
        LOG.info("Received: " + customer);
        return Response.ok("OK").build();
    }


    @POST
    @Path("save")
    @Consumes("application/vnd.com.giogar.customer-v1+json")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveCustomTypeV1(com.giogar.model.version1_0.Customer customer) {
        LOG.info("Received: " + customer);
        return Response.ok("OK").build();
    }

    @POST
    @Path("save")
    @Consumes("application/vnd.com.giogar.customer-v2+json")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveCustomTypeV2(com.giogar.model.version2_0.Customer customer) {
        LOG.info("Received: " + customer);
        return Response.ok("OK").build();
    }

}
