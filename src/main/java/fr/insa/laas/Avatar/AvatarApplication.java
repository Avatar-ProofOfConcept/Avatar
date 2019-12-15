package fr.insa.laas.Avatar;

import javax.annotation.PostConstruct;

 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableEurekaClient
public class AvatarApplication {
    
	 
  	public static void main(String[] args) {
  		 
  		SpringApplication.run(AvatarApplication.class, args);
	}
  	
 
	

}
