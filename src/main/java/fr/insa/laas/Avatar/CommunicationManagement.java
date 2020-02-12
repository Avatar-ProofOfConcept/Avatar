package fr.insa.laas.Avatar;
 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map.Entry; 
import java.util.HashMap;
 



public class CommunicationManagement {
	Util u=new Util();
	private final String ORIGINATOR = "admin:admin";
	IExtract kb;
	private ArrayList <MetaAvatar> friendFromRepo = new ArrayList <MetaAvatar>() ; 
	MetaAvatar metaavatar;
 	private int nbRequestsA1;
	ArrayList<RequestTask> ls=new ArrayList<RequestTask>();//cluster members
 	ClientInterface client=new Client();
  	private SocialNetwork socialNetwork ;
  	private HashMap<String,ArrayList<String>> propositions ;

	
	public CommunicationManagement (int port,IExtract kb,MetaAvatar m)
	{
	  this.kb=kb;
	  this.metaavatar=m;
	  this.socialNetwork=new SocialNetwork(m);
	  this.propositions=new HashMap<String, ArrayList<String>>() ;
	  this.friendFromRepo=kb.ExtractMetaAvatars(m.getInterestsVector().keySet(),m.getName());


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
	    		int nbTask=Integer.parseInt(u.getXmlElement(request,"nbTask"));
	    		
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
     					 

                    	 resp=ask(content, sender,ls.get(id).getListeMemeber().get(i)+"askmembers/",id,nbTask);
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
	    		int nbTask=Integer.parseInt(u.getXmlElement(request,"nbTask"));
	    		//int nbTask=Integer.parseInt(u.getXmlElement(request, "nbTask"));
	    		System.out.println("list exclus from extended discovery method : "+list.toString());
	    		if(incrTTL(id) < 6)
	    		{
	    			if (this.ls.size()==0) 
					{   for(int i=0;i<nbTask;i++) 
						{
						ls.add(new RequestTask());
						ls.get(i).setSns(new SocialNetwork(metaavatar));
						}
					}
 				String taskLabel = content.split("&")[1];
				ArrayList<String> exclus=new ArrayList<String>();
				 
				//construction of SN without cluster member
           	 	//this.socialNetwork=new SocialNetwork();
				ls.get(id).getSns().setMetaAvatar(this.friendFromRepo);
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
 					 

                	 resp=ask(content, sender,ls.get(id).getSns().getSocialNetwork().get(i).getURL()+"askmembers/",id,nbTask);
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
			public Response ask (String taskData,String sender,String reciever,int id,int nbTask)
			{
				String message = "<type>ask</type>";
				Response response2 = null;
				message = u.addXmlElement(message, "content",taskData) ;
				message = u.addXmlElement(message, "sender", sender);
				message = u.addXmlElement(message, "id", String.valueOf(id));
				message = u.addXmlElement(message, "nbTask", String.valueOf(nbTask));
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
		 
			public void initTTL(int size)
			{
				OutputStream ops = null;
		        ObjectOutputStream objOps = null;
		        ArrayList<Integer> ls=new ArrayList<Integer>();
		        for(int i=0;i<size;i++) ls.add(0);
		        try {
		            ops = new FileOutputStream("TTL.txt");
		            objOps = new ObjectOutputStream(ops);
		            objOps.writeObject(ls);
		            objOps.flush();
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        } finally{
		            try{
		                if(objOps != null) objOps.close();
		            } catch (Exception ex){
		                 
		            }
		        }
				
			}
			public void showTTLs()
			{
				 InputStream fileIs = null;
			        ObjectInputStream objIs = null;
			        try {
			            fileIs = new FileInputStream("TTL.txt");
			            objIs = new ObjectInputStream(fileIs);
			            ArrayList<Integer> emp = ( ArrayList<Integer>) objIs.readObject();
			            System.out.println(emp.toString());
			        } catch (FileNotFoundException e) {
			            e.printStackTrace();
			        } catch (IOException e) {
			            e.printStackTrace();
			        } catch (ClassNotFoundException e) {
			            e.printStackTrace();
			        } finally {
			            try {
			                if(objIs != null) objIs.close();
			            } catch (Exception ex){
			                 
			            }
			        }
			
			         
			    }
			public int incrTTL(int index)
			{
				InputStream fileIs = null;
		        ObjectInputStream objIs = null; OutputStream ops = null;
		        ObjectOutputStream objOps = null;
		        int ttl=0;
		        try {
		            fileIs = new FileInputStream("TTL.txt");
		            objIs = new ObjectInputStream(fileIs);
		            ArrayList<Integer> emp = ( ArrayList<Integer>) objIs.readObject();
		            ttl=emp.get(index)+1;
		            emp.set(index, emp.get(index)+1);
		            ops = new FileOutputStream("TTL.txt");
		            objOps = new ObjectOutputStream(ops);
		            objOps.writeObject(emp);
		            objOps.flush();
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (ClassNotFoundException e) {
		            e.printStackTrace();
		        } finally {
		            try {
		                if(objIs != null) objIs.close();
		                if(objOps != null) objOps.close();
		            } catch (Exception ex){
		                 
		            }
		        }
		        return ttl;
				
			}

}
