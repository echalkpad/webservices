import com.google.gson.Gson;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import javax.ws.rs.core.MediaType;

import static com.giogar.model.fixture.CustomerFixture.aCustomerV1;
import static com.giogar.model.fixture.CustomerFixture.aCustomerV2;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VersionTest extends JerseyTest {

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder()
                .contextParam("contextConfigLocation", "classpath:testApplicationContext.xml")
//                .contextPath("/")           //<- this messes up.
                .servletClass(SpringServlet.class)
                .initParam("com.sun.jersey.api.json.POJOMappingFeature", "true")
                .contextListenerClass(ContextLoaderListener.class)
                .requestListenerClass(RequestContextListener.class)
                .build();
    }

    @Test
    public void postVersionTest() throws Exception {

        String response = resource()
                .path("customer/save")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN)
                .post(String.class, new Gson().toJson(aCustomerV1()));

        assertThat(response, is("OK"));

    }

    @Test
    public void postCustomType() throws Exception {

        String response = resource()
                .path("customer/save")
                .type("application/vnd.customer+json")
                .accept(MediaType.TEXT_PLAIN)
                .post(String.class, new Gson().toJson(aCustomerV1()));

        assertThat(response, is("OK"));

    }

    @Test
    public void postCustomTypeVersion1() throws Exception {

        String response = resource()
                .path("customer/save")
                .type("application/vnd.com.giogar.customer-v1+json")
                .accept(MediaType.TEXT_PLAIN)
                .post(String.class, new Gson().toJson(aCustomerV1()));

        assertThat(response, is("OK"));

    }

    @Test
    public void postCustomTypeVersion2() throws Exception {

        String response = resource()
                .path("customer/save")
                .type("application/vnd.com.giogar.customer-v2+json")
                .accept(MediaType.TEXT_PLAIN)
                .post(String.class, new Gson().toJson(aCustomerV2()));

        assertThat(response, is("OK"));

    }

}
