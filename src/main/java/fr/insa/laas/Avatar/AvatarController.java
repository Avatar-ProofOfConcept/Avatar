package fr.insa.laas.Avatar;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

 
@RestController
public class AvatarController implements ErrorController  {
	Avatar avatar;
	Util u=new Util();
	@Value("${server.port}")
	private int port;
	
 
 
    

     
	@RequestMapping(value="/init/", method=RequestMethod.GET) 
	public String getAllAvatar() {
    	System.out.println(port);
       avatar=new Avatar(port);

        return "init";
    }
	@RequestMapping(value="/discover/") 
	public String discover() {
		this.avatar.cluster();
    	this.avatar.discovery();

        return "the discovery is finished";
    }
	@RequestMapping(value="/receiveMembers/") 
	public String getClusterMembers(@RequestBody String request) {
		if(avatar==null) avatar=new Avatar(port);
         avatar.getComManager().getMemberList(request);
         //System.out.println("Cluster membrers "+avatar.getComManager().ls.toString());
        return "Cluster Member recieved";
    }
	@RequestMapping(value="/receiveExclus/") 
	public String getExclus(@RequestBody String request) {
		if(avatar==null) avatar=new Avatar(port);
         avatar.getComManager().getExclusList(request);
         System.out.println("Exclus "+avatar.getComManager().ex.toString()+" request ="+u.getXmlElement(request,"request"));
         System.out.println("Extend the discovery by th avatar"+port);
         avatar.getComManager().extendedDiscovery(this.avatar.getComManager().ex,u.getXmlElement(request,"request"));
        return "exclus  recieved";
    }
	 
	
  	
   
  	@RequestMapping(value="/ResponsePropose/")
  	public String propose(@RequestBody String request)
  	{
  		if (this.avatar==null) System.out.println("nullllll");
  		System.out.println("recevoir la proposition de cluster members");
  		this.avatar.receivePropo(request);
  		System.out.println(request);
  		return "merci pour la proposition";
  	}
  	
	@RequestMapping(value="/receiveFailure/")
  	public String getFailure(@RequestBody String request)
  	{
  		if (this.avatar==null) System.out.println("nullllll");
  		System.out.println("Fail to find task"+u.getXmlElement(request,"content"));
  		this.avatar.receivePropo(request);
  		System.out.println(request);
  		return " :( ";
  	}
 

  	@RequestMapping(value="/askmembers/")
    public String TreatHTTPRequests1 (@RequestBody String request, @RequestParam("type") String type) throws IOException{
		if(avatar==null) avatar=new Avatar(port);

		String sender = u.getXmlElement(request, "sender");
		//avatar.getComManager().IncrNbRequest();
		System.out.println("[CONTROLLER of "+avatar.getName()+"] Received a "+type+" Request from "+sender+" total requests="+avatar.getComManager().getNbRequest());		
		String res=null;	
		switch (type){
		 
		//Ask Request (about a certain Task)
		case "ask":
			//System.out.println("			RECEIVED ASK REQUEST");		
			//res="OK REQ";
			res=avatar.getComManager().AskMembers(request,avatar.getName());
			break;
			
	 
			
		default:
			System.out.println("DEBUG NOTYPE ERROR, type= "+type);		
    		res = u.addXmlElement(res,"type","NoType");
			res = u.addXmlElement(res,"conversationId","NoConv");
			res = u.addXmlElement(res,"date","Nodate");
			res = u.addXmlElement(res,"sender",avatar.getName());
			res = u.addXmlElement(res,"content","noContent");

			break;
		}	

		return res;
	}
  	@RequestMapping(value="/delegu/")
    public String TreatHTTPRequests(@RequestBody String request, @RequestParam("type") String type) throws IOException{
		if(avatar==null) avatar=new Avatar(port);

		String sender = u.getXmlElement(request, "sender");
		//avatar.getComManager().IncrNbRequest();
		System.out.println("[CONTROLLER of "+avatar.getName()+"] Received a "+type+" Request from "+sender+" total requests="+avatar.getComManager().getNbRequest());		
		String res=null;	
		switch (type){
		 
		//Ask Request (about a certain Task)
		case "ask":
			//System.out.println("			RECEIVED ASK REQUEST");		
			//res="OK REQ";
			res=avatar.getComManager().AskRequest(request, avatar.getName());
			break;
			
	 
			
		default:
			System.out.println("DEBUG NOTYPE ERROR, type= "+type);		
    		res = u.addXmlElement(res,"type","NoType");
			res = u.addXmlElement(res,"conversationId","NoConv");
			res = u.addXmlElement(res,"date","Nodate");
			res = u.addXmlElement(res,"sender",avatar.getName());
			res = u.addXmlElement(res,"content","noContent");

			break;
		}	

		return res;
	}

  
  	@Override
  	public String getErrorPath() {
  		return "ERROR GETERRORPATH";
  	}

    
  
}