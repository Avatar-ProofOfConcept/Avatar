package fr.insa.laas.Avatar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

 
@RestController
public class AvatarController {
	
	@Value(value="${server.port}") 
	int port;
     
    @GetMapping
    public Avatar getAllAvatar() {
    	System.out.println(port);
       Avatar avatar=new Avatar(port);

        return avatar;
    }
   /* @GetMapping
    public Service getService() {
        return avatar.getServicesList().get(0);
    }*/
    
  
}