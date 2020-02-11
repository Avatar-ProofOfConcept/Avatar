package fr.insa.laas.Avatar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.HashMap;
import java.util.ArrayList;;



public class CommunicationManagement {
	Util u=new Util();
	private final String ORIGINATOR = "admin:admin";
	IExtract kb;
	MetaAvatar metaavatar;
 	private int nbRequestsA1;
	ArrayList<RequestTask> ls=new ArrayList<RequestTask>();//cluster members
	ArrayList<String> ex=new ArrayList<String>();//Exclus 
	ClientInterface client=new Client();
  	private SocialNetwork socialNetwork ;
  	private HashMap<String,ArrayList<String>> propositions ;

	
	public CommunicationManagement (int port,IExtract kb,MetaAvatar m)
	{
	  this.kb=kb;
	  this.metaavatar=m;
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
	    		int id=Integer.parseInt(u.getXmlElement(request,"id"));
	    		
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
					System.out.println(name + " Ask cluster members  "+"ls= "+ls.get(id).getListeMemeber().toString());
					Response resp=null;
					boolean fail=true;
                     for(int i=1;i<ls.get(id).getListeMemeber().size();i++)
                     {
     					 

                    	 resp=ask(content, sender,ls.get(id).getListeMemeber().get(i)+"askmembers/",id);
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
                    	 extendedDiscovery(ls.get(id).getListeMemeber(), request);
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
	    		int id=Integer.parseInt(u.getXmlElement(request,"id")); 
	    		if(updateTTL() < 6)
	    		{
 				String taskLabel = content.split("&")[1];
				ArrayList<String> exclus=new ArrayList<String>();
				 
				//construction of SN without cluster member
           	 	//this.socialNetwork=new SocialNetwork();
				ls.get(id).getSns().setMetaAvatar(kb.ExtractMetaAvatars(ls.get(id).getSns().getMetaAvatar().getInterestsVector().keySet(),ls.get(id).getSns().getMetaAvatar().getName()));
           	    exclus=ls.get(id).getSns().socialNetworkConstruction(3,list);
           	    if(ls.get(id).getSns().getSocialNetwork().size()==0)
           	    {
           	    	sendFailure(sender,content,"there are no avatar to ask SN is null");
           	    	
           	    }
           	    else
           	    {
           	    //ask social network
           	    System.out.println(" Ask social network  ");
           	    Response resp=null;
           	    boolean fail=true;
                 for(int i=0;i<ls.get(id).getSns().getSocialNetwork().size();i++)
                 {
 					 

                	 resp=ask(content, sender,ls.get(id).getSns().getSocialNetwork().get(i).getURL()+"askmembers/",id);
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
           	 
           	 String urlChosen=ls.get(id).getSns().getChosen();
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
	    }
	    else
	    {
	    	sendFailure(sender,content,"SN level = 6 !!");
	    	
	    }
		
	}
            public void sendFailure (String sender,String request,String cause)
            {
            	String res="";
            	res = u.addXmlElement(res,"type","failure");
				res = u.addXmlElement(res,"content","No Service available for this task "+request+cause) ;
				try {
					Response response2 = client.request(sender+"receiveFailure/", ORIGINATOR, res);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
			
			 
			
		 
		
		 
			
			public void sendClusterMember(int nbTask,int idRequest,String urlDelegue,ArrayList<String> clustermembers) throws IOException
			{
				String message=new String();
				message=u.addXmlElement(message,"membersNumber",String.valueOf(clustermembers.size()));
				message=u.addXmlElement(message,"id",String.valueOf(idRequest));
				message=u.addXmlElement(message,"nbTask",String.valueOf(nbTask));


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
			public Response ask (String taskData,String sender,String reciever,int id)
			{
				String message = "<type>ask</type>";
				Response response2 = null;
				message = u.addXmlElement(message, "content",taskData) ;
				message = u.addXmlElement(message, "sender", sender);
				message = u.addXmlElement(message, "id", String.valueOf(id));
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
				 
		    	int nbMembers=Integer.parseInt(u.getXmlElement(response, "membersNumber"));
		    	int nbTask=Integer.parseInt(u.getXmlElement(response, "nbTask"));
				if (this.ls.size()==0) 
				{   for(int i=0;i<nbTask;i++) 
					{
					ls.add(new RequestTask());
					ls.get(i).setSns(new SocialNetwork(metaavatar));
					}
				}
                int id=Integer.parseInt(u.getXmlElement(response, "id"));			
                ls.get(id).getListeMemeber().clear();
		    	System.out.println("number of Cluster members "+nbMembers);
		    	for (int i=0;i<nbMembers;i++)
		    	{
		    		ls.get(id).getListeMemeber().add(u.getXmlElement(response, "avatar"+i));
		    	}
		    
		    	System.out.println("req n "+id+"Liste cluster member= "+this.ls.get(id).getListeMemeber().toString());

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
			//TTL Management
			public void initTTL()
			{
				writeTTL(0);
				
			}
			public int updateTTL()
			{
				Scanner scanner;
				int ttl=0;
				try {
					scanner = new Scanner(new File("TTL.txt"));
					 
					ttl = scanner.nextInt();
					System.out.println("TTL = "+ttl);
					ttl++;
					writeTTL(ttl);
					 
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return ttl;
			}
			public void writeTTL(int i)
			{
				try {
					Writer wr = new FileWriter("TTL.txt");
					wr.write(String.valueOf(i));
					wr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

}
