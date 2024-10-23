package com.example.library;

import jakarta.servlet.Servlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@ConditionalOnClass (Servlet.class)
@ConditionalOnProperty (value = "sap.important" ,havingValue = "true",
        matchIfMissing = false
)
@Configuration
@ImportResource("/foo.xml")
class MyExtensionConfig {
 
    @Bean
    @ConditionalOnMissingBean
    MyUserExtension myUserExtension (){
        return () -> System.out.println("default!");
    }
}

class VeryImportantStartupThing {

    VeryImportantStartupThing() {
        System.out.println("doing something important!");
    }
}