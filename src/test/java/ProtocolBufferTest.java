import com.giogar.model.protocolbuffer.ProtocolBufferOrder;
import com.giogar.parser.ProtobufMessageReader;
import com.giogar.parser.ProtobufMessageWriter;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import javax.ws.rs.core.MediaType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProtocolBufferTest extends JerseyTest {

    @Override
    protected AppDescriptor configure() {
        WebAppDescriptor webAppDescriptor =
                new WebAppDescriptor.Builder()
                        .contextParam("contextConfigLocation", "classpath:testApplicationContext.xml")
//                .contextPath("/")           //<- this messes up.
                        .servletClass(SpringServlet.class)
                        .contextListenerClass(ContextLoaderListener.class)
                        .requestListenerClass(RequestContextListener.class)
                        .build();
        webAppDescriptor.getClientConfig().getClasses().add(ProtobufMessageWriter.class);
        webAppDescriptor.getClientConfig().getClasses().add(ProtobufMessageReader.class);
        return webAppDescriptor;
    }

    @Test
    public void postVersionTest() throws Exception {

        // Delay beginning of test or a concurrent exception will be thrown
        Thread.sleep(5000L);

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

        String response = resource()
                .path("protobuf-customer/save")
                .type("application/x-protobuf")
                .accept(MediaType.TEXT_PLAIN)
                .post(String.class, order);

        assertThat(response, is("OK"));

    }

}
