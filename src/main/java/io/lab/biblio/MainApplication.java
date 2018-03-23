package io.lab.biblio;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.servlet.annotation.WebServlet;

/**
 * Created by amazimpaka on 2018-03-02
 */
@SpringBootApplication(scanBasePackages = {"io.lab.biblio"})
public class MainApplication {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainApplication.class, args);
    }

    @WebServlet(urlPatterns = "/*", name = "ApplicationVServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ApplicationUI.class, productionMode = false)
    public static class ApplicationVServlet extends VaadinServlet {

    }
}

