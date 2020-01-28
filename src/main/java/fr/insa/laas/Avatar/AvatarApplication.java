package fr.insa.laas.Avatar;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AvatarApplication {
    
	 
  	public static void main(String[] args) {
  		System.out.println("start");
  		 long startTime = System.nanoTime();
  		SpringApplication.run(AvatarApplication.class, args);
  		long elapsedTime = System.nanoTime() - startTime;
	     
        System.out.println("Total execution time in millis: "+ elapsedTime/1000000f);
  		System.out.println("finish");
	}
  	
 
	

}
