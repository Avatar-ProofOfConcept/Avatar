package fr.insa.laas.Avatar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@SpringBootApplication
@EnableEurekaClient
public class AvatarApplication {
	
  	public static void main(String[] args) {
 		SpringApplication.run(AvatarApplication.class, args);
	}

}
