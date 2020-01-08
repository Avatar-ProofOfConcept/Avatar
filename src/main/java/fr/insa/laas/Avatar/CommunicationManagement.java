package fr.insa.laas.Avatar;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.HashMap;
import java.util.ArrayList;;



public class CommunicationManagement {
	Util u=new Util();
	private final String ORIGINATOR = "admin:admin";
	IExtract kb;
 	private int nbRequestsA1;
	ArrayList<String> ls=new ArrayList<String>();//cluster members
	ArrayList<String> ex=new ArrayList<String>();//Exclus 
	ClientInterface client=new Client();
  	private SocialNetwork socialNetwork ;
  	private HashMap<String,ArrayList<String>> propositions ;

	
	public CommunicationManagement (int port,IExtract kb,MetaAvatar m)
	{
	  this.kb=kb;
	  this.socialNetwork=new SocialNetwork(m);
	  this.propositions=new HashMap<String, ArrayList<String>>() ;


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
	public HashMap<String, ArrayList<String>> getPropositions()
	{
		return this.propositions;
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
                    			 String proposition =resp.getRepresentation();
                    			 proposition=u.addXmlElement(proposition, "task",taskLabel);
                    			 System.out.println("propositionnn"+proposition);
								Response resp1=client.request(sender+"ResponsePropose/",ORIGINATOR,proposition);
								System.out.println("response from initiator   "+resp1.getRepresentation());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
                     		 }
                     }
                     if (fail)
                     {
                    	// ls.addAll(arg0);
                    	 extendedDiscovery(ls, request);
                     }
                     
         				/*	//Answer that he can't 
         					res = u.addXmlElement(res,"type","failure");
         					res = u.addXmlElement(res,"sender",name);
         					res = u.addXmlElement(res,"content","No Service available for this task") ;
         					 */
         					//TBD: WARNING!!! We have to see data about the Qos and all the info. about the service
         					//addXmlElement(res,"content",ExtractServiceFromLabel(taskLabel)+"&"+name) ;	//ServiceX & LabelX & QosX & name
         					 
         					//cptMessagesHTTP++;
         					//System.out.println("["+name+": Failure Message to "+sender+"]: "+message.getContent()+", conversation: "+message.getConversationId()+"   "+message.getPerformative()+", nbMessages="+cptMessages) ;
         				
                     
					//TBD: WARNING!!! We have to see data about the Qos and all the info. about the service
					//addXmlElement(res,"content",ExtractServiceFromLabel(taskLabel)+"&"+name) ;	//ServiceX & LabelX & QosX & name
					 
					//cptMessagesHTTP++;
					//System.out.println("["+name+": Failure Message to "+sender+"]: "+message.getContent()+", conversation: "+message.getConversationId()+"   "+message.getPerformative()+", nbMessages="+cptMessages) ;
				}
	    		return res;
			}
			public void extendedDiscovery(ArrayList<String> list,String request)
			{
				String sender = u.getXmlElement(request,"sender");
	    		String content = u.getXmlElement(request,"content");
	    		 
	    		
 				String taskLabel = content.split("&")[1];
				ArrayList<String> exclus=new ArrayList<String>();
				//construction of SN without cluster member
           	 	//this.socialNetwork=new SocialNetwork();
           	    exclus=socialNetwork.socialNetworkConstruction(3,list);
           	    //ask social network
           	    System.out.println(" Ask social network  ");
           	    Response resp=null;
           	    boolean fail=true;
                 for(int i=0;i<socialNetwork.getSocialNetwork().size();i++)
                 {
 					 

                	 resp=ask(content, sender,socialNetwork.getSocialNetwork().get(i).getURL()+"askmembers/");
                 	 if (u.getXmlElement(resp.getRepresentation(),"type").equals("propose"))
                		 {
                 		 fail=false;
                		 	System.out.println("Eureka !!!");
                		 try {
                			 String proposition =resp.getRepresentation();
               			 proposition=u.addXmlElement(proposition, "task",taskLabel);
               			 System.out.println("propositionnn"+proposition);
							Response resp1=client.request(sender+"ResponsePropose/",ORIGINATOR,proposition);
							System.out.println("response from initiator   "+resp1.getRepresentation());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                 		 }
                 }
             
            if (fail)//extend the discovery
            {
           	 
           	 String urlChosen=socialNetwork.getChosen();
           	 if (urlChosen.length()>0)
           	 {
           	 try {
           		 //ex.addAll(exclus);///add cluster members to exclus
           		 System.out.println("Send exclus");
           		 if (!exclus.contains(sender))exclus.add(sender);
           		 if(!exclus.contains(urlChosen))exclus.add(urlChosen);
           		 if(!exclus.containsAll(list)) exclus.addAll(list);
				 sendExclusList(urlChosen, exclus,request);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
            }
		}

			public String AskMembers(String request,String name){
	        	String res = "";

 	    		String content = u.getXmlElement(request,"content");
	    		 
	    		
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
			public void sendExclusList(String urlDelegue,ArrayList<String> members, String request) throws IOException
			{
				String message=new String();
				message=u.addXmlElement(message, "request", request);
				message=u.addXmlElement(message,"membersNumber",String.valueOf(members.size()));
				for(int i=0;i<members.size();i++)
				{
				message=u.addXmlElement(message,"avatar"+i,members.get(i));
				}
				 
					Response response2 = client.request(urlDelegue+"receiveExclus/", ORIGINATOR, message);
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
					//savePropositions(response2.getRepresentation(), taskData);
					
					System.out.println("AVATAR: HTTP RESPONSE :"+ response2.getRepresentation());
					

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 return response2;
			}
			public void getMemberList(String response)
			{
				this.ls.clear();
		    	int nbMembers=Integer.parseInt(u.getXmlElement(response, "membersNumber"));
		    	System.out.println("number of Cluster members "+nbMembers);
		    	for (int i=0;i<nbMembers;i++)
		    	{
		    		ls.add(u.getXmlElement(response, "avatar"+i));
		    	}
		    


			}
			public void getExclusList(String response)
			{
				this.ex.clear();
		    	int nbMembers=Integer.parseInt(u.getXmlElement(response, "membersNumber"));
		    	
		    	for (int i=0;i<nbMembers;i++)
		    	{
		    		ex.add(u.getXmlElement(response, "avatar"+i));
		    	}
		    


			}
			public void savePropositions (String response,String taskData)
			{
				if(u.getXmlElement(response,"type").equals("propose"))
				{
					if (!this.propositions.containsKey(taskData.split("&")[1]))
					{
						this.propositions.put(taskData.split("&")[1],new ArrayList<String>());
					}
					this.propositions.get(taskData.split("&")[1]).add(u.getXmlElement(response,"content"));

				}
				
			}
			public void savePropositions (String response)
			{
				if(u.getXmlElement(response,"type").equals("propose"))
				{
					if (!this.propositions.containsKey(u.getXmlElement(response,"task")))
					{
						this.propositions.put(u.getXmlElement(response,"task"),new ArrayList<String>());
					}
					this.propositions.get(u.getXmlElement(response,"task")).add(u.getXmlElement(response,"content"));

				}
				
			}
			public void showPropositions()
			{
				for (Entry<String, ArrayList<String>> entry : propositions.entrySet())
				{
					System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue().toString());
				}
			}

}
