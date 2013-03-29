import com.giogar.model.Item;
import com.giogar.model.Order;
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
import org.springframework.util.StopWatch;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static com.giogar.model.fixture.CustomerFixture.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProtocolBufferTest extends JerseyTest {

//    public static void main2(String[] args) throws ExecutionException, InterruptedException {
//
//        ClientConfig cc = new DefaultClientConfig();
//        cc.getClasses().add(ProtobufMessageReader.class);
//        cc.getClasses().add(ProtobufMessageWriter.class);
//
//        Client client = Client.create(cc);
//
//        WebResource resource = client.resource("http://localhost:8080/webservices/services/");
//
//        ProtocolBufferOrder.Order order = ProtocolBufferOrder.Order.newBuilder()
//                .setId(1)
//                .setAddress(ProtocolBufferOrder.Order.Address.newBuilder()
//                        .setAddressLine1("Apt 1 - Dublin Court").build())
//                .setCustomer(ProtocolBufferOrder.Order.Customer.newBuilder()
//                        .setName("Giovanni")
//                        .setLastName("Gargiulo").build())
//                .addItems(ProtocolBufferOrder.Order.Item.newBuilder()
//                        .setId(1).setDescription("Description")
//                        .setQuantity(1).build()).build();
//
//        String response = resource
//                .path("protobuf-customer/save")
//                .type("application/x-protobuf")
//                .accept(MediaType.TEXT_PLAIN)
//                .post(String.class, order);
//
//        assertThat(response, is("OK"));
//
//    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        List<Order> orders = getOrders();

        List<ProtocolBufferOrder.Order> protocolBufferOrders = new ArrayList<ProtocolBufferOrder.Order>();

        for (Order order : orders) {
            ProtocolBufferOrder.Order.Builder builder = ProtocolBufferOrder.Order.newBuilder()
                    .setId(order.getId())
                    .setShippingAddress(ProtocolBufferOrder.Order.Address.newBuilder()
                            .setAddressLine1(order.getShippingAddress().getLineAddress1())
                            .setAddressLine2(order.getShippingAddress().getLineAddress2())
                            .setAddressLine3(order.getShippingAddress().getLineAddress3())
                            .setState(ProtocolBufferOrder.Order.Address.State.IRELAND) //State Missing!!!
                            .build())
                    .setCustomer(ProtocolBufferOrder.Order.Customer.newBuilder()
                            .setName(order.getCustomer().getName())
                            .setLastName(order.getCustomer().getLastName())
                            .setPhoneNumber(order.getCustomer().getPhoneNumber())
//                            .setAddress(ProtocolBufferOrder.Order.Address.newBuilder()
//                                    .setAddressLine1(order.getCustomer().getResidentialAddress().getLineAddress1())
//                                    .setAddressLine2(order.getCustomer().getResidentialAddress().getLineAddress2())
//                                    .setAddressLine3(order.getCustomer().getResidentialAddress().getLineAddress1())
//                                            //State Missing!!!
//                                    .build())
                            .build());

            for (Item item : order.getItems()) {
                builder.addItems(ProtocolBufferOrder.Order.Item.newBuilder()
                        .setId(item.getId())
                        .setDescription(item.getDescription())
                        .setQuantity(item.getQuantity())
                        .build());
            }

            protocolBufferOrders.add(builder.build());

        }


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        threadPoolExecutor.prestartAllCoreThreads();

        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(ProtobufMessageReader.class);
        cc.getClasses().add(ProtobufMessageWriter.class);

        Client client = Client.create(cc);
        WebResource resource = client.resource("http://localhost:8080/webservices/services/");

        Thread.sleep(5000L);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Future<String>> futures = new ArrayList<Future<String>>();

        for (ProtocolBufferOrder.Order order : protocolBufferOrders) {
            futures.add(threadPoolExecutor.submit(new RestThreadClient(order, resource)));
        }

        for (Future<String> future : futures) {
            assertThat(future.get(), is("OK"));
        }

        stopWatch.stop();

        System.out.println("Elapsed time: " + stopWatch.prettyPrint());

        threadPoolExecutor.shutdown();

    }

    private static List<Order> getOrders() {
        List<Order> orders = new ArrayList<Order>();

        for (int i = 0; i < 100; i++) {
            Order order = new Order();
            order.setId(i);
            order.setCustomer(aCustomer());
            order.setShippingAddress(homeAddress());
            order.setItems(new ArrayList<Item>());

            int numItems = new Random().nextInt() % 10;
            numItems = numItems == 0 ? 1 : numItems;
            numItems = numItems < 0 ? -numItems : numItems;

            for (int j = 0; j < numItems; j++) {
                order.getItems().add(anItem());
            }

            orders.add(order);
        }
        return orders;
    }

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

    private static class RestThreadClient implements Callable<String> {

        private ProtocolBufferOrder.Order order;
        private WebResource webResource;

        public RestThreadClient(ProtocolBufferOrder.Order order, WebResource webResource) {
            this.order = order;
            this.webResource = webResource;
        }

        @Override
        public String call() throws Exception {
            String response = webResource
                    .path("protobuf-customer/save")
                    .type("application/x-protobuf")
                    .accept(MediaType.TEXT_PLAIN)
                    .post(String.class, order);
            return response;
        }
    }

}
