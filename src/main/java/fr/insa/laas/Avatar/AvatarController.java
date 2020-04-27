package fr.insa.laas.Avatar;

import java.io.IOException;
import java.util.ArrayList;
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
	int cpt=0;
	@Value("${server.port}")
	private int port;
 
@RequestMapping(value="/init/", method=RequestMethod.GET) 
public String getAvatar() 
{
    	 
       avatar=new Avatar(port);
       //avatar.getComManager().initDeleguates(5);
       return ("fin");

        
}
@RequestMapping(value="/initD/") 
public void getAvatarD() 
{
    	System.out.println(port);
       avatar=new Avatar(port);

        
}
	@RequestMapping(value="/discover/") 
	public String discover() {
		this.avatar.cluster();
    	this.avatar.discovery(this.avatar.getGoalsList().get(0).getTasksList());

        return "the discovery is finished";
    }
	@RequestMapping(value="/receiveMembers/") 
	public String getClusterMembers(@RequestBody String request) {
		if(avatar==null) avatar=new Avatar(port);
         avatar.getComManager().getMemberList(request);
         return "Cluster Member recieved";
    }
 
	
	
	@RequestMapping(value="/receiveMembersSelection/") 
	public String getClusterMembersSelection(@RequestBody String request) {
		if(avatar==null) avatar=new Avatar(port);
        return avatar.getComManager().getMemberListSelection(request);
    }
	 
	@RequestMapping(value="/receiveCL/") 
	public String getLOcalConstarint(@RequestBody String request) {
		if(avatar==null) avatar=new Avatar(port);
         System.out.println(request);
      
         return avatar.getComManager().getC().getSelectedAvatars(u.getCls(request)).toString();
    }
	@RequestMapping(value="/receiveExclus/") 
	public String getExclus(@RequestBody String request) {
		if(avatar==null) avatar=new Avatar(port);
         
         ArrayList<String> ex=new ArrayList<String>();
         int nbMembers=Integer.parseInt(u.getXmlElement(request, "membersNumber"));
	     System.out.println("ClusterMember  :  "+nbMembers);
	    	for (int i=0;i<nbMembers;i++)
	    	{
	    		ex.add(u.getXmlElement(request, "avatar"+i));
	    	}
         System.out.println("Exclus "+ex.toString()+" request ="+u.getXmlElement(request,"request"));
         System.out.println("Extend the discovery by th avatar"+port);
         avatar.getComManager().extendedDiscovery(ex,u.getXmlElement(request,"request"));
        return "exclus  recieved";
    }
	 
	
  	
   
  	@RequestMapping(value="/ResponsePropose/")
  	public String propose(@RequestBody String request)
  	{
  		 
  		System.out.println("recevoir la proposition de cluster members");
  		this.avatar.receivePropo(request);
  		System.out.println(request);
  		return "merci pour la proposition";
  	}
  	
	@RequestMapping(value="/receiveFailure/")
  	public String getFailure(@RequestBody String request)
  	{
  		 
  		System.out.println("Fail to find task"+u.getXmlElement(request,"content"));
  		this.avatar.receivePropo(request);
  		System.out.println(request);
  		return " :-( ";
  	}
 

  	@RequestMapping(value="/askmembers/")
    public String TreatHTTPRequests1 (@RequestBody String request, @RequestParam("type") String type) throws IOException{
		if(avatar==null) avatar=new Avatar(port);

 		System.out.println("[CONTROLLER of "+avatar.getName()+"] Received a "+type+" Request : [ ] total requests="+avatar.getComManager().getNbRequest());		
		String res=null;	
		switch (type){
		 
 		case "ask":
			 
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
 		System.out.println("[CONTROLLER of "+avatar.getName()+"] Received a "+type+" Request : [ "+request+"] total requests="+avatar.getComManager().getNbRequest());		
		String res=null;	
		switch (type){
		 
 		case "ask":
			 
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