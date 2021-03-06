import com.giogar.model.Item;
import com.giogar.model.Order;
import com.google.gson.Gson;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Test;
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

public class PerformanceTestCompressedJson extends JerseyTest {

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder()
                .contextParam("contextConfigLocation", "classpath:testApplicationContext.xml")
//                .contextPath("/")           //<- this messes up.
                .servletClass(SpringServlet.class)
                .initParam("com.sun.jersey.api.json.POJOMappingFeature", "true")
                .initParam("com.sun.jersey.spi.container.ContainerRequestFilters", "com.sun.jersey.api.container.filter.GZIPContentEncodingFilter")
                .initParam("com.sun.jersey.spi.container.ContainerResponseFilters", "com.sun.jersey.api.container.filter.GZIPContentEncodingFilter")
                .contextListenerClass(ContextLoaderListener.class)
                .requestListenerClass(RequestContextListener.class)
                .build();
    }

    @Test
    public void postVersionTest() throws Exception {

        List<Order> orders = getOrders();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        threadPoolExecutor.prestartAllCoreThreads();

        Thread.sleep(5000L);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Future<String>> futures = new ArrayList<Future<String>>();

        for (Order order : orders) {
            futures.add(threadPoolExecutor.submit(new RestThreadClient(order)));
        }

        for (Future<String> future : futures) {
            assertThat(future.get(), is("OK"));
        }

        stopWatch.stop();

        System.out.println("Elapsed time: " + stopWatch.prettyPrint());
    }

    private List<Order> getOrders() {
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

    private class RestThreadClient implements Callable<String> {

        private Order order;

        public RestThreadClient(Order order) {
            this.order = order;
        }

        @Override
        public String call() throws Exception {
            String response = resource()
                    .path("simple-customer/save")
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_PLAIN)
                    .post(String.class, new Gson().toJson(order));
            return response;
        }
    }

}
