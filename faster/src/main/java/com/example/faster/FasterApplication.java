package com.example.faster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClient;

@Controller
@ResponseBody
@SpringBootApplication
public class FasterApplication {

    @GetMapping("/hi")
    String hi() {
        return "hello, world!";
    }

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(FasterApplication.class, args);
    }

}

// cora iberkleid 
// @coraiberkleid

@Controller
@ResponseBody
class Demo {

    private final RestClient http;

    Demo(RestClient http) {
        this.http = http;
    }

    @GetMapping("/delay")
    String delay() {
        return this.http
                .get()
                .uri("https://httpbin.org/delay/5")
                .retrieve()
                .body(String.class);
    }
}


