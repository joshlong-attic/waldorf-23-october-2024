package com.example.spring_basic_consumer;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


public class SpringBasicConsumerApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(MyConfig.class);


//		SpringApplication.run(SpringBasicConsumerApplication.class, args);
    }

}

@Import(org.springdoc.core.configuration.SpringDocConfiguration.class)
@Configuration
class MyConfig {

    MyConfig() {
        System.out.println("my config");
    }
}