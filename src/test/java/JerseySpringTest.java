import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import javax.ws.rs.core.MediaType;
import java.io.File;

public class JerseySpringTest extends JerseyTest {

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder("com.giogar.jersey")
                .contextParam("contextConfigLocation", "classpath:testApplicationContext.xml")
//                .contextPath("/")           //<- this mess up.
                .servletClass(SpringServlet.class)
                .contextListenerClass(ContextLoaderListener.class)
                .requestListenerClass(RequestContextListener.class)
                .build();


    }

    @Test
    public void searchAPI() throws Exception {
        System.out.println(new File(".").getAbsolutePath());

        String response = resource()
                .path("timeoftheday/asplaintext/gio")
                .accept(MediaType.TEXT_PLAIN)
                .get(String.class);

        System.out.println(response.toString());
    }

}
