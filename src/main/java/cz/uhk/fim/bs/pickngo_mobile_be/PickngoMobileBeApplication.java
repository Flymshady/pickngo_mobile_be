package cz.uhk.fim.bs.pickngo_mobile_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
        //(exclude = { SecurityAutoConfiguration.class })
public class PickngoMobileBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PickngoMobileBeApplication.class, args);
    }

}
