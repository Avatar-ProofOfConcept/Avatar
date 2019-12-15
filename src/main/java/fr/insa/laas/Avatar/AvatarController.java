package fr.insa.laas.Avatar;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.PathVariable;
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
	//GET Services Requests
	@RequestMapping(value="/Services/{serviceX}", method=RequestMethod.GET)
	public String GetService(@PathVariable String serviceX){
		System.out.println("Someone wants the Service "+serviceX);
		String res = avatar.getComManager().GETService(serviceX,avatar.getSm());
		return res;
	}
 	@RequestMapping(value="/Services/", method=RequestMethod.GET)    
 	public Service getService() {
        return avatar.getServicesList().get(0);
    }
  	
  	//GET Ask Request (about a certain a task, not in a scenario of a process)
  	//Exp: GET http://localhost:9701/Avatar1/?type=ask&task=Task2/Label2
  	@RequestMapping(value="", method=RequestMethod.GET)
      public String GETAskRequest (@RequestParam("type") String type, @RequestParam("task") String taskData){
  		
  		//Split the Task and Data and check the URI format
  		String taskName=taskData.split("/")[0];
  		String taskLabel=taskData.split("/")[1];
  		if (!taskName.contains("http")){	//We add the complete address if it's only "TaskX"
  			taskName="http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#"+taskName;
  		}
  		String res=avatar.getComManager().GETAskRequest(taskName, taskLabel,avatar.getName());
   		return res;
  	}
  		
  	//Requests
  	@RequestMapping(value="")
      public String TreatHTTPRequests (@RequestBody String request, @RequestParam("type") String type) throws IOException{
  	
  		String sender = u.getXmlElement(request, "sender");
  		avatar.getComManager().IncrNbRequest();
  		System.out.println("[CONTROLLER of "+avatar.getName()+"] Received a "+type+" Request from "+sender+" total requests="+avatar.getComManager().getNbRequest());		
  		String res=null;	
  		switch (type){
  		
  		//Ask Request (about a certain Task)
  		case "ask":
  			//System.out.println("			RECEIVED ASK REQUEST");		
  			//res="OK REQ";
  			res=avatar.getComManager().AskRequest(request,avatar.getName());
  			break;
  			
  		//Accept Request (about a Service proposition received)
  		case "accept":
  			res=avatar.getComManager().AcceptRequest(request,avatar.getName());
  			break;	
  			
  		//Failure Request (When a delegate failed)
  		case "failure":
  			res=avatar.getComManager().FailureRequest(request,avatar.getSocialNetwork(),avatar.getMetaAvatar(),avatar.getDm(),avatar.getName(),avatar.getClient());
  			break;	
  	
  		//Failure Request (because of TimeOut)
  		case "failuretimeout":
  			String conversationIdTO = u.getXmlElement(request,"conversationId");
  			res="<type>okfailureTO</type><content>okFTO</content><sender>"+avatar.getName()+"</sender><conversationId>"+conversationIdTO+"</conversationId><date>date</date>";
  			//res=avatar.FailureTimeOutRequest(request);
  			break;
  			
  		//Failure Request (because of TimeOut)
  		case "propagate":
  			res=avatar.getComManager().PropagateRequest(request,avatar.getName(),avatar.getSocialNetwork(),avatar.getDm(),avatar.getClient(),avatar.getMetaAvatar());
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