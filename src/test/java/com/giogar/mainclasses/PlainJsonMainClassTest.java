package com.giogar.mainclasses;

import com.giogar.model.Item;
import com.giogar.model.Order;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter;
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

public class PlainJsonMainClassTest extends JerseyTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        List<Order> orders = getOrders();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        threadPoolExecutor.prestartAllCoreThreads();

        Client client = Client.create();
        client.addFilter(new GZIPContentEncodingFilter(false));
        WebResource resource = client.resource("http://localhost:8080/webservices/services/");

        Thread.sleep(5000L);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Future<String>> futures = new ArrayList<Future<String>>();

        for (Order order : orders) {
            futures.add(threadPoolExecutor.submit(new RestThreadClient(order, resource)));
        }

        for (Future<String> future : futures) {
            assertThat(future.get(), is("OK"));
        }

        stopWatch.stop();

        System.out.println("Elapsed time: " + stopWatch.prettyPrint());

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

    private static class RestThreadClient implements Callable<String> {

        private Order order;

        private WebResource webResource    ;

        public RestThreadClient(Order order, WebResource webResource) {
            this.order = order;
            this.webResource = webResource;
        }

        @Override
        public String call() throws Exception {
            String response = webResource
                    .path("simple-customer/save")
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_PLAIN)
                    .post(String.class, new Gson().toJson(order));
            return response;
        }
    }

}
