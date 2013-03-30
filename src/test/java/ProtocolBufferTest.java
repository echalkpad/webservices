import com.giogar.model.protocolbuffer.ProtocolBufferOrder;
import com.giogar.parser.ProtobufMessageReader;
import com.giogar.parser.ProtobufMessageWriter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import javax.ws.rs.core.MediaType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProtocolBufferTest extends JerseyTest {

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder("com.giogar")
                .contextParam("contextConfigLocation", "classpath:testApplicationContext.xml")
//                .contextPath("/")           //<- this messes up.
                .servletClass(SpringServlet.class)
                .contextListenerClass(ContextLoaderListener.class)
                .requestListenerClass(RequestContextListener.class)

                .build();
    }

    //    @Test
    public void postVersionTest() throws Exception {

        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(ProtobufMessageReader.class);
        cc.getClasses().add(ProtobufMessageWriter.class);

        Client client = Client.create(cc);

        WebResource resource = client.resource("http://localhost:9998/");

        ProtocolBufferOrder.Order order = ProtocolBufferOrder.Order.newBuilder()
                .setId(1)
                .setShippingAddress(ProtocolBufferOrder.Order.Address.newBuilder()
                        .setAddressLine1("Apt 1 - Dublin Court").build())
                .setCustomer(ProtocolBufferOrder.Order.Customer.newBuilder()
                        .setName("Giovanni")
                        .setLastName("Gargiulo").build())
                .addItems(ProtocolBufferOrder.Order.Item.newBuilder()
                        .setId(1).setDescription("Description")
                        .setQuantity(1).build()).build();

        String response = resource
                .path("protobuf-customer/save")
                .type("application/x-protobuf")
                .accept(MediaType.TEXT_PLAIN)
                .post(String.class, order);

        assertThat(response, is("OK"));

    }

}
