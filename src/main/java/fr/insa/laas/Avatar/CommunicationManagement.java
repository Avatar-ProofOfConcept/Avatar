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
	
	public CommunicationManagement (int port,IExtract kb)
	{
	  this.kb=kb;

	}

	public int getNbRequest()
	{
		return nbRequestsA1;
	}
	public void IncrNbRequest()
	{
		nbRequestsA1++;
	}
	//Ask Request (about a task, outside the case of a process, so we only receive the Task content, and not the date, conv, etc.) 
			public String GETAskRequest(String taskName, String taskLabel,String name){
				String res = "";

				//isAble?
				//TBD: URGENT !! USE THE SERVICES MANAGER AS IT CONTAINS SERVICES OF FRIENDS TOO
				if(kb.IsAbleTaskFriend(taskLabel)){
					//PROPOSE
					res = u.addXmlElement(res,"type","propose");
					res = u.addXmlElement(res,"sender",name);
					//TBD: WARNING!!! We have to see data about the Qos and all the info. about the service
					res = u.addXmlElement(res,"content",kb.ExtractServiceFromLabel(taskLabel)+"&"+name) ;	//ServiceX & LabelX & QosX & name
					//System.out.println("["+name+":Proposal Message to "+sender+"]: "+message.getContent()+", conversation: "+message.getConversationId()+", nbMessages="+cptMessages) ;
				}
				else{
					//Answer that he can't 
					res = u.addXmlElement(res,"type","failure");
					res = u.addXmlElement(res,"sender",name);
					res = u.addXmlElement(res,"content","No Service available for this task") ;
					//cptMessagesHTTP++;
					//System.out.println("["+name+": Failure Message to "+sender+"]: "+message.getContent()+", conversation: "+message.getConversationId()+"   "+message.getPerformative()+", nbMessages="+cptMessages) ;
				}
	    		return res;
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
                     for(int i=1;i<ls.size();i++)
                     {
     					 

                    	 resp=askDeleguate(content, sender,ls.get(i)+"askmembers/", client);
                    	 if (u.getXmlElement(resp.getRepresentation(),"type").equals("propose"))
                    		 {
                    		 	System.out.println("Eureka !!!");
                    		 	try {
									Response resp1=client.request(sender,ORIGINATOR,resp.getRepresentation());
									System.out.println("response from initiator"+resp1.getRepresentation());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
                    		     break;
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
			
			//Accept Request
			public String AcceptRequest(String request,String name){
	    		String res="<type>confirm</type><sender>"+name+"</sender><content>ok</content>";
	    		//TBD ADD THE SERVICE FROM AVATARX
	    		return res;
			}
			
			//Failure Request (A delegate failed)
			public String FailureRequest(String request,SocialNetwork socialNetwork,MetaAvatar metaAvatar ,DelegationsManager delegationsManager,String name,ClientInterface client) throws IOException{
				
				String sender = u.getXmlElement(request,"sender");
	    		String content = u.getXmlElement(request,"content");
	    		String conversationId = u.getXmlElement(request,"conversationId");
				String date2 = u.getXmlElement(request,"date");
				String res="failureDelegation";
				
	    		//String date = getXmlElement(request,"date");
	    		//System.out.println("["+name+": <-- Failure Reception from "+sender+"]: "+msg.getContent()+", conversation: "+msg.getConversationId()) ;
				
				//Test if he's the delegate of this research
				//if (delegateTasks.contains(msg.getConversationId())){
				
				String msgToSend = delegationsManager.ManageFailureRequest2(request, socialNetwork, metaAvatar);

				//He was the delegate of this conversation
				if (msgToSend != null){
					//It's a failure msg to the delegationSender because there is no time
					if (msgToSend.contains("<type>failure</type>")){
						String friend = u.getXmlElement(msgToSend,"receiver");
						String friendURL = socialNetwork.getFriend(friend).getURL();
						Response response2 = client.request(friendURL+"?type=failure", ORIGINATOR, msgToSend);
						nbRequestsA1++;
						System.out.println("["+name+"] Send a Failure Request to "+friend+", conv="+conversationId+", total requests="+nbRequestsA1);		
						//ResponsesHandler(response2);
					}	
					//There is still more time 
					else if(msgToSend.contains("second")){
						String secondDelegate= u.getXmlElement(msgToSend,"secondDelegate");
						String delegationFrom= u.getXmlElement(msgToSend,"delegationFrom");

						//Test if there is someone able for a 2nd delegation
						if (!secondDelegate.equals("noOne")){
															
							String taskData=conversationId.split("&")[1]+"&"+conversationId.split("&")[2]+"&"+conversationId.split("&")[3]; //Extract the Task Data From the conversationID, Conv.ID = avatarOriginal & taskData
							sendDelegationTask2(secondDelegate, taskData , conversationId, date2,name,client,socialNetwork,metaAvatar,delegationsManager);
							//System.out.println("							NEW DELEG  "+name+":    2nd delegate = "+secondDelegate+" task  "+taskData+"  conv  "+msg.getConversationId());
						}
						//Inform the Avatar that delegate him that it's a failure because he can't 
						else{
							String res2 = "<type>failure</type>";
							res2 = u.addXmlElement(res2,"conversationId",conversationId);
							//res = addXmlElement(res,"date",date);
							res2 = u.addXmlElement(res2,"sender",name);
							res2 = u.addXmlElement(res2,"content","No more time");
							res2 = u.addXmlElement(res2,"receiver",delegationFrom);
							
							String friendURL = socialNetwork.getFriend(delegationFrom).getURL();
							Response response2 = client.request(friendURL+"?type=failure", ORIGINATOR, res2);
							nbRequestsA1++;
							System.out.println("["+name+"] Send a Failure Request to "+delegationFrom+", conv="+conversationId+", total requests="+nbRequestsA1);		
						}
					}
				}
				return res;
			}
		
			public String FailureTimeOutRequest(String request,String name){
	    		String conversationId = u.getXmlElement(request,"conversationId");
				String res="<type>okfailureTO</type><content>okFTO</content><sender>"+name+"</sender><conversationId>"+conversationId+"</conversationId><date>date</date>";
				return res;
			}
			
			public String PropagateRequest(String request,String name,SocialNetwork socialNetwork,DelegationsManager delegationsManager,ClientInterface client,MetaAvatar metaAvatar ) throws IOException{
				String res="";
				
	        	String sender = u.getXmlElement(request,"sender");
	    		String content = u.getXmlElement(request,"content");
	    		String conversationId = u.getXmlElement(request,"conversationId");
				
				String taskP = content.split("&")[0];
				String taskLabelP = content.split("&")[1];
				String taskInterestP = content.split("&")[2];
				String dateP = u.getXmlElement(request,"date");
				
				//isAble?
				//TBD: Use Services Manager to check the services of our friends
				if(kb.IsAbleTaskFriend(taskLabelP)){
					
					//PROPOSE
					res = u.addXmlElement(res,"type","propose");
					res = u.addXmlElement(res,"sender",name);
					res = u.addXmlElement(res,"conversationId",conversationId);
					//TBD: We have to see data about the Qos and all the info. about the service
					res = u.addXmlElement(res,"content",kb.ExtractServiceFromLabel(taskLabelP)+"&"+name) ;	//ServiceX & LabelX & QosX & name
					res = u.addXmlElement(res,"date",dateP);
				}
				else{
					
					//BroadCast to its SN and memorize the nb of people he requested
					int nbRequests=broadcastSN2(name,taskP+"&"+taskLabelP+"&"+taskInterestP,conversationId,dateP,conversationId.split("&")[0], sender,client,socialNetwork,metaAvatar,delegationsManager);
					delegationsManager.AddDelegation(new Delegation(sender, conversationId, nbRequests));
					res="<type>okPropagation</type><content>okPropagation</content><sender>"+name+"</sender><conversationId>"+conversationId+"</conversationId><date>"+dateP+"</date>";
					
					//Timeout of 20 sec
					//TBD URGENT IF 2nd PROPAGATION ==> The 2nd delegate don't have 20 sec as the first delegate, it depends
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						  @Override
						  public void run() {
							//See if the task was treated 20sec after the launching of the Timeout
							String msgToSend=delegationsManager.ManageTimeOut2(request);
							if (msgToSend!= null ){
								
								String friend = u.getXmlElement(msgToSend,"receiver");
								String friendURL = socialNetwork.getFriend(friend).getURL();
								try {
									//System.out.println("		TEST URL TIMEOUT of "+name+"		"+friendURL+"?type=failuretimeout   "+msgToSend);
									Response response2 = client.request(friendURL+"?type=failuretimeout", ORIGINATOR, msgToSend);
									nbRequestsA1++;
									System.out.println("["+name+"] Send a Failure TimeOut Request to "+friend+", conv="+conversationId+", total requests="+nbRequestsA1);		
									ResponsesHandler(response2,name,socialNetwork,client,metaAvatar,delegationsManager);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						  }
						}, 20*1000);									
				}
				return res;
			}
			
			//HTTP Responses Handler
			public void ResponsesHandler(Response resp,String name,SocialNetwork socialNetwork,ClientInterface client,MetaAvatar metaAvatar,DelegationsManager delegationsManager){
				if (resp.getStatusCode() != 201 || resp.getRepresentation().isEmpty()) {
					String res="";
					System.out.println("AVATAR: HTTP RESPONSE to "+name+":"+ resp.getRepresentation());		
					String response=resp.getRepresentation();
					try{
						String type=response.split("<type>")[1].split("</type>")[0];
						//cptMessagesHTTP++;
						//System.out.println("[HTTP RESPONSES of "+name+"]: "+ resp.getRepresentation()+"  "+type+", nbHTTPMsg: "+cptMessagesHTTP);		
						
						String content = u.getXmlElement(response,"content");
						String sender = u.getXmlElement(response,"sender");
						
						switch (type){
						
						//Propose Message
						case "propose":
							String conversationId = u.getXmlElement(response,"conversationId");
							String date = u.getXmlElement(response,"date");
							String serviceLabel=content.split("&")[1];
							String taskLabel=conversationId.split("&")[2];
							
							//System.out.println("["+name+": <-- Proposal Reception from "+sender+"]: "+msg.getContent()+", conversation: "+msg.getConversationId()) ;

							if (u.IsServiceOK(taskLabel,serviceLabel)){
								//Send an Accept Request 
								res = u.addXmlElement(res,"type","accept");
								res = u.addXmlElement(res,"conversationId",conversationId);
								res = u.addXmlElement(res,"date",date);
								res = u.addXmlElement(res,"sender",name);
								res = u.addXmlElement(res,"content","ok");
								
								//Send the request
								String friendURL =  socialNetwork.getFriend(sender).getURL();
								Response resp2 = client.request(friendURL+"?type=accept", ORIGINATOR, res);
								nbRequestsA1++;
								System.out.println("["+name+"] Send an Accept Request to "+sender+", conv="+conversationId+", total requests="+nbRequestsA1);		
								ResponsesHandler(resp2,name,socialNetwork,client,metaAvatar,delegationsManager);
								//cptMessagesHTTP++;
								//System.out.println("["+name+":Accept Message to "+sender+"]: "+message.getContent()+", conversation: "+message.getConversationId()+", nbMessages="+cptMessages) ;
								
							}
							break;
							
						//Confirm Message
						case "confirm" : 
							
							break;
						
						//Failure Message
						case "failure":
							
							String conversationIdF = u.getXmlElement(response,"conversationId");
							String dateF = u.getXmlElement(response,"date");
							
							//System.out.println("["+name+": <-- Failure Reception from "+sender+"]: "+msg.getContent()+", conversation: "+msg.getConversationId()) ;
							
							//Test if he's the delegate of this research
							//if (delegateTasks.contains(msg.getConversationId())){
							String msgToSend = delegationsManager.ManageFailureRequest2(response, socialNetwork, metaAvatar);

							//He was the delegate of this conversation
							if (msgToSend != null){
								//It's a failure msg to the delegationSender because there is no time
								if (msgToSend.contains("<type>failure</type>")){
									String friend = u.getXmlElement(msgToSend,"receiver");
									String friendURL = socialNetwork.getFriend(friend).getURL();
									Response response2 = client.request(friendURL+"?type=failure", ORIGINATOR, msgToSend);
									nbRequestsA1++;
									System.out.println("["+name+"] Send a Failure Request to "+friend+", conv="+conversationIdF+", total requests="+nbRequestsA1);		

									//ResponsesHandler(response2);
								}	
								//There is still more time 
								else if(msgToSend.contains("second")){
									String secondDelegate=msgToSend.split("=")[1];
									//Test if there is someone able for a 2nd delegation
									if (!secondDelegate.equals("noOne")){
																		
										String taskData=conversationIdF.split("&")[1]+"&"+conversationIdF.split("&")[2]+"&"+conversationIdF.split("&")[3]; //Extract the Task Data From the conversationID, Conv.ID = avatarOriginal & taskData
										sendDelegationTask2(secondDelegate, taskData , conversationIdF, dateF,name,client,socialNetwork,metaAvatar,delegationsManager);
										//System.out.println("							NEW DELEG  "+name+":    2nd delegate = "+secondDelegate+" task  "+taskData+"  conv  "+msg.getConversationId());
									}
									else{
										//TBD URGENT !!!! SEND A FAILURE MSG TO THE DELEGATIONFROM AVATAR
									}
								}
							}
							
							break;
						
						case "failuretimeout":
							String conversationIdTO = u.getXmlElement(response,"conversationId");
							res="<type>okfailureTO</type><content>okFTO</content><sender>"+name+"</sender><conversationId>"+conversationIdTO+"</conversationId><date>date</date>";
							break;
							
						//Inform Message
						case "inform":
							System.out.println("		INFORM "+response);
							break;
						
						default:
							break;
						}

					}catch (Exception e) {e.printStackTrace();}
					
				}
				else{
					System.out.println("AVATAR: HTTP REQUEST ERROR");
				}
			}
			//Ask a delegate to ask its SN about a task
			public void sendDelegationTask2(String agentName, String taskData, String conversation, String date,String name,ClientInterface client,SocialNetwork socialNetwork ,MetaAvatar metaAvatar,DelegationsManager delegationsManager) throws IOException {
				
				String message = "<type>propagate</type>";
				message = u.addXmlElement(message, "content", taskData) ;
				message = u.addXmlElement(message,"sender",name);

				//cptMessages++;

				if (conversation.equals("newConversation")){

					//message.setConversationId(name+"&"+task+"&"+hour+"-"+min+"-"+day+"-"+month+"-"+year);
					message = u.addXmlElement(message, "conversationId",name+"&"+taskData);
				    
					Calendar rightNow = Calendar.getInstance();
				    Date startTime = rightNow.getTime();
				    Instant now = rightNow.toInstant();
					message = u.addXmlElement(message, "date", now.toString());
					
				}
				
				//Delegation of Delegation
				else {
					
					message = u.addXmlElement(message, "conversationId",conversation);
					message = u.addXmlElement(message, "date",date);

					//System.out.println("["+name+":Second Propagate Message to "+agentName+"]: "+message.getContent()+", conversation: "+message.getConversationId()+", nbMessages="+cptMessages) ;
				}
				String friendURL= socialNetwork.getFriend(agentName).getURL();
				System.out.println(friendURL);
				
				Response response2 = client.request(friendURL+"?type=propagate", ORIGINATOR, message);
				nbRequestsA1++;
				System.out.println("["+name+"] Send a propagate Request to "+agentName+", conv="+conversation+", total requests="+nbRequestsA1);		
				ResponsesHandler(response2,name,socialNetwork,client,metaAvatar,delegationsManager);

				
			}

		//BroadCast a msg to its Social Net.
			public int broadcastSN2(String name,String taskData, String conversation, String date, String originalSender, String delegationFrom,ClientInterface client,SocialNetwork socialNetwork ,MetaAvatar metaAvatarG,DelegationsManager delegationsManager) throws IOException 
			{
				
				int msgs=0; 	//Nb of Avatars he sent to this msg
				
				String message = "<type>ask</type>";
				message = u.addXmlElement(message, "content",taskData) ;
				message = u.addXmlElement(message, "sender", name);
				
				//Test if it's a new conversation or not 
				if (conversation.equals("newConversation")){
					Calendar rightNow = Calendar.getInstance();
					
					message = u.addXmlElement(message,"conversationId", name+"&"+taskData);
					String instant = rightNow.toInstant().toString();
					message = u.addXmlElement(message, "date", instant);
				}
				else {
					message = u.addXmlElement(message,"conversationId", conversation);
	    			message = u.addXmlElement(message, "date", date);
				}  
				 String friendName = null;
				 MetaAvatar metaAvatar = null;
				 //Iterator and the SocialNetwork list 
				 Iterator<MetaAvatar> itrFriend = socialNetwork.getSocialNetwork().iterator();
				 while (itrFriend.hasNext()) {
					 metaAvatar = itrFriend.next();
					 friendName = metaAvatar.getName();

					 //To avoid sending to the originalSender
					 if (!friendName.equals(originalSender) && !friendName.equals(delegationFrom)){
						 
						 //System.out.println("			[TEST INTEREST BC]"+name+": Has the avatar friend "+friendName+" the interest "+taskData.split("&")[2]+" for the task: "+taskData.split("&")[0]);
						 //Test if this friend has the the interest of the task
						 String taskInterest = taskData.split("&")[2];
						 if (metaAvatar.ContainsInterest(taskInterest) != -1){
							 //Add the receiver parameters
							 String friendURL = socialNetwork.getFriend(friendName).getURL();
							 
							 Response response2 = client.request(friendURL+"?type=ask", ORIGINATOR, message);
							 System.out.println("la réponse0"+message);		

							 nbRequestsA1++;
							 System.out.println("["+name+"] Send a type Request to "+friendName+", conv="+conversation+", total requests="+nbRequestsA1);		
							 ResponsesHandler(response2,name,socialNetwork,client,metaAvatar,delegationsManager);
							 
							 //System.out.println("["+name+":Request Message to "+friendName+"]: "+message.getContent()+", conversation: "+message.getConversationId()) ;
							 //cptMessagesHTTP++;
							 msgs++;

						 }
						}
				 }
				 
				 //We add this conversation to the delegatesConversations								
				 if(conversation.equals("newConversation")){
					 delegationsManager.AddDelegation(new Delegation(name,u.getXmlElement(message,"conversationId") , msgs));
				 }

				 return msgs;
			}
			public String GETService(String service,ServicesManager servicesManager){
				return servicesManager.ServiceExecution2(service);
				//return "getservice";
			}
			
			public void sendClusterMember(String urlDelegue,ArrayList<String> clustermembers,ClientInterface client) throws IOException
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
			public Response askDeleguate (String taskData,String sender,String reciever,ClientInterface client)
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
