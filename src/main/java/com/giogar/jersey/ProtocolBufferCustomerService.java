package com.giogar.jersey;

import com.giogar.model.protocolbuffer.ProtocolBufferOrder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/protobuf-customer")
public class ProtocolBufferCustomerService {

    private static Logger LOG = Logger.getLogger(ProtocolBufferCustomerService.class);

    @POST
    @Path("save")
    @Consumes("application/x-protobuf")
    @Produces(MediaType.TEXT_PLAIN)
    public Response save(ProtocolBufferOrder.Order order) {
        LOG.info("Received: " + order);
        return Response.ok("OK").build();
    }

}
