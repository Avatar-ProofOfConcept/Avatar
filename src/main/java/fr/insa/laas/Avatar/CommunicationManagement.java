package fr.insa.laas.Avatar;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;



public class CommunicationManagement {
	Util u=new Util();
	private final String ORIGINATOR = "admin:admin";
	IExtract kb;
 	private int nbRequestsA1;
	ArrayList<String> ls=new ArrayList<String>();//cluster members
	ClientInterface client=new Client();
  	private SocialNetwork socialNetwork ;

	
	public CommunicationManagement (int port,IExtract kb,MetaAvatar m)
	{
	  this.kb=kb;
	  this.socialNetwork=new SocialNetwork(m);

	}

	public int getNbRequest()
	{
		return nbRequestsA1;
	}
	public void IncrNbRequest()
	{
		nbRequestsA1++;
	}
	public SocialNetwork getSocialNetwork()
	{
		return this.socialNetwork;
	}
 
			
			//Ask Request (about a task) 
			public String AskRequest(String request,String name){
	        	String res = "";

	        	String sender = u.getXmlElement(request,"sender");
	    		String content = u.getXmlElement(request,"content");
	    		 
	    		
	    		String task = content.split("&")[0];
				String taskLabel = content.split("&")[1];
				 
	    		
				//isAble?
				//TBD: URGENT !! USE THE SERVICES MANAGER AS IT CONTAINS SERVICES OF FRIENDS TOO
				if(kb.IsAbleTaskFriend(taskLabel)){
					//PROPOSE
					res = u.addXmlElement(res,"type","propose");
					res = u.addXmlElement(res,"sender",name);
					 
					//TBD: WARNING!!! We have to see data about the Qos and all the info. about the service
					res = u.addXmlElement(res,"content",kb.ExtractServiceFromLabel(taskLabel)+"&"+name) ;	//ServiceX & LabelX & QosX & name
					 
					//cptMessagesHTTP++;
					//System.out.println("["+name+":Proposal Message to "+sender+"]: "+message.getContent()+", conversation: "+message.getConversationId()+", nbMessages="+cptMessages) ;
				}
				else{
					//Answer that he can't 
					/*res = u.addXmlElement(res,"type","failure");
					res = u.addXmlElement(res,"sender",name);
					res = u.addXmlElement(res,"content","No Service available for this task") ;*/
					System.out.println(name + " Ask cluster members  "+"ls= "+ls.toString());
					Response resp=null;
					boolean fail=true;
                     for(int i=1;i<ls.size();i++)
                     {
     					 

                    	 resp=ask(content, sender,ls.get(i)+"askmembers/");
                     	 if (u.getXmlElement(resp.getRepresentation(),"type").equals("propose"))
                    		 {
                     		 fail=false;
                    		 	System.out.println("Eureka !!!");
                    		 try {
								Response resp1=client.request(sender+"ResponsePropose/",ORIGINATOR,resp.getRepresentation());
								System.out.println("response from initiator   "+resp1.getRepresentation());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
                     		 }
                     }
                     if (fail)
                     {
                    	 //construction of SN without cluster member
                    	 socialNetwork.socialNetworkConstruction(3,ls);
                    	 //ask social network
                    	 System.out.println(name + " Ask social network  ");
     					 resp=null;
     					 fail=true;
                          for(int i=0;i<socialNetwork.getSocialNetwork().size();i++)
                          {
          					 

                         	 resp=ask(content, sender,socialNetwork.getSocialNetwork().get(i).getURL()+"askmembers/");
                          	 if (u.getXmlElement(resp.getRepresentation(),"type").equals("propose"))
                         		 {
                          		 fail=false;
                         		 	System.out.println("Eureka !!!");
                         		 try {
     								Response resp1=client.request(sender+"ResponsePropose/",ORIGINATOR,resp.getRepresentation());
     								System.out.println("response from initiator   "+resp1.getRepresentation());
     								} catch (IOException e) {
     									// TODO Auto-generated catch block
     									e.printStackTrace();
     								}
                          		 }
                          }
                      }
					 
					//TBD: WARNING!!! We have to see data about the Qos and all the info. about the service
					//addXmlElement(res,"content",ExtractServiceFromLabel(taskLabel)+"&"+name) ;	//ServiceX & LabelX & QosX & name
					 
					//cptMessagesHTTP++;
					//System.out.println("["+name+": Failure Message to "+sender+"]: "+message.getContent()+", conversation: "+message.getConversationId()+"   "+message.getPerformative()+", nbMessages="+cptMessages) ;
				}
	    		return res;
			}
			public String AskMembers(String request,String name){
	        	String res = "";

	        	String sender = u.getXmlElement(request,"sender");
	    		String content = u.getXmlElement(request,"content");
	    		 
	    		
	    		String task = content.split("&")[0];
				String taskLabel = content.split("&")[1];
				 
	    		
				//isAble?
				//TBD: URGENT !! USE THE SERVICES MANAGER AS IT CONTAINS SERVICES OF FRIENDS TOO
				if(kb.IsAbleTaskFriend(taskLabel)){
					//PROPOSE
					res = u.addXmlElement(res,"type","propose");
					res = u.addXmlElement(res,"sender",name);
					 
					//TBD: WARNING!!! We have to see data about the Qos and all the info. about the service
					res = u.addXmlElement(res,"content",kb.ExtractServiceFromLabel(taskLabel)+"&"+name) ;	//ServiceX & LabelX & QosX & name
					 
					//cptMessagesHTTP++;
					//System.out.println("["+name+":Proposal Message to "+sender+"]: "+message.getContent()+", conversation: "+message.getConversationId()+", nbMessages="+cptMessages) ;
				}
				else{
					//Answer that he can't 
					res = u.addXmlElement(res,"type","failure");
					res = u.addXmlElement(res,"sender",name);
					res = u.addXmlElement(res,"content","No Service available for this task") ;
					 
					//TBD: WARNING!!! We have to see data about the Qos and all the info. about the service
					//addXmlElement(res,"content",ExtractServiceFromLabel(taskLabel)+"&"+name) ;	//ServiceX & LabelX & QosX & name
					 
					//cptMessagesHTTP++;
					//System.out.println("["+name+": Failure Message to "+sender+"]: "+message.getContent()+", conversation: "+message.getConversationId()+"   "+message.getPerformative()+", nbMessages="+cptMessages) ;
				}
	    		return res;
			}
			
			 
			
		 
		
		 
			
			public void sendClusterMember(String urlDelegue,ArrayList<String> clustermembers) throws IOException
			{
				String message=new String();
				message=u.addXmlElement(message,"membersNumber",String.valueOf(clustermembers.size()));
				for(int i=0;i<clustermembers.size();i++)
				{
				message=u.addXmlElement(message,"avatar"+i,clustermembers.get(i));
				}
				 
					Response response2 = client.request(urlDelegue+"receiveMembers/", ORIGINATOR, message);
					System.out.println("AVATAR: HTTP RESPONSE :"+ response2.getRepresentation());		
 

			}
			public Response ask (String taskData,String sender,String reciever)
			{
				String message = "<type>ask</type>";
				Response response2 = null;
				message = u.addXmlElement(message, "content",taskData) ;
				message = u.addXmlElement(message, "sender", sender);
				 try {
					response2 = client.request(reciever+"?type=ask", ORIGINATOR, message);
					System.out.println("AVATAR: HTTP RESPONSE :"+ response2.getRepresentation());
					

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 return response2;
			}
			public void getMemberList(String response)
			{
		    	int nbMembers=Integer.parseInt(u.getXmlElement(response, "membersNumber"));
		    	System.out.println("number of Cluster members "+nbMembers);
		    	for (int i=0;i<nbMembers;i++)
		    	{
		    		ls.add(u.getXmlElement(response, "avatar"+i));
		    	}
		    


			}

}
