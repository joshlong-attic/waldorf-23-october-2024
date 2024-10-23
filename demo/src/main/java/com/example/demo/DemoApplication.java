package com.example.demo;

import com.example.library.MyUserExtension;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import java.io.IOException;
import java.lang.annotation.*;


//@ComponentScan
//@Configuration
//@EnableAutoConfiguration
@SpringBootApplication
@Component
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    MyUserExtension myOverride() {
        return () -> System.out.println("override!");
    }

    @Bean
    ApplicationRunner loadSpi(MyUserExtension userExtension) {
        return args -> userExtension.doSomething();
    }
    
    /*
    @Bean
    FilterRegistrationBean <MyFilter> myFilterFilterRegistrationBean (){
      return new FilterRegistrationBean<MyFilter>();
    }
    */

    @Bean
    ServletRegistrationBean<MyServlet> servletRegistrationBean() {
        return new ServletRegistrationBean<>(new MyServlet(), "/hello");
    }

    private static class MyServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            resp.getWriter().write("hello, world!");
        }
    }

}

@SapComponent
class CustomerService {

    CustomerService() {
        System.out.println("hello Sap Component");
    }
}

@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
@interface SapComponent {

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean name in case of an autodetected component.
     *
     * @return the suggested component name, if any (or empty String otherwise)
     */
    String value() default "";

}


@Configuration
class MyConfig {

}
