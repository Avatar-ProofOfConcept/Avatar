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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry; 
import java.util.HashMap;
import java.util.Set;
 



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
  	private ArrayList<String>listeMemberSelection=new ArrayList<String>();
  	private ClusterQoS c;
  	private QoSManager q=new QoSManager();
	double opt=0;
  	
	
	public ClusterQoS getC() {
		return c;
	}

	public void setC(ClusterQoS c) {
		this.c = c;
	}

	public CommunicationManagement (int port,IExtract kb,MetaAvatar m)
	{
	  this.kb=kb;
	  this.metaavatar=m;
	  this.socialNetwork=new SocialNetwork(m);
	  SocialNetwork.setParameters(0.4f, 0.4f,0.2f);
	  SocialNetwork.setSize(40);
	  this.propositions=new HashMap<String, ArrayList<String>>() ;
	  //this.friendFromRepo=kb.ExtractMetaAvatars(m.getInterestsVector().keySet(),m.getName());


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
 
			
 			public String AskRequest(String request,String name){
	        	String res = "";
	        	
	        	String sender = u.getXmlElement(request,"sender");
	    		String content = u.getXmlElement(request,"content");
	    		int id=Integer.parseInt(u.getXmlElement(request,"id"));
	    		int nbTask=Integer.parseInt(u.getXmlElement(request,"nbTask"));
				String taskLabel = content.split("&")[1];
				if(kb.IsAbleTaskFriend(taskLabel)){
 					res = u.addXmlElement(res,"type","propose");
					res = u.addXmlElement(res,"sender",name);
					 
 					res = u.addXmlElement(res,"content",kb.ExtractServiceFromLabel(taskLabel)+"&"+name) ;
					 
					 
				}
				else{
					 
					System.out.println(name + " Ask cluster members  "+"ls= "+ls.get(id).getListeMemeber().toString());
					Response resp=null;
					boolean fail=true;
                     for(int i=0;i<ls.get(id).getListeMemeber().size();i++)
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
 									e.printStackTrace();
								}
                     		 }
                     }
                     if (fail)
                     {
                     	 extendedDiscovery(ls.get(id).getListeMemeber(), request);
                     }
                     
         				 
				}
	    		return res;
			}
			public void extendedDiscovery(ArrayList<String> list,String request)
			{
				String sender = u.getXmlElement(request,"sender");
	    		String content = u.getXmlElement(request,"content");
	    		int id=Integer.parseInt(u.getXmlElement(request,"id"));
	    		int nbTask=Integer.parseInt(u.getXmlElement(request,"nbTask"));
 	    		System.out.println("list exclus from extended discovery method : "+list.toString());
	    		if(incrTTL(id) < 6)//TTL=6
	    		{
	    			if (this.ls.size()==0) 
					{   for(int i=0;i<nbTask;i++) 
						{
						ls.add(new RequestTask());
						ls.get(i).setSns(new SocialNetwork(metaavatar));
						}
					}
 				String taskLabel = content.split("&")[1];
				Set<String> exclus=new HashSet<String>();
				 
			 
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
            	 System.out.println("Send exclus");
           		 exclus.add(this.metaavatar.getURL());
           		 exclus.add(urlChosen);
           		 exclus.addAll(list);
				 sendExclusList(urlChosen, exclus,request);
				} catch (IOException e) {
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
 					e.printStackTrace();
				}

            }
			public String AskMembers(String request,String name){
	        	String res = "";

 	    		String content = u.getXmlElement(request,"content");
	    		 
	    		
 				String taskLabel = content.split("&")[1];
				 
	    		
		 
				if(kb.IsAbleTaskFriend(taskLabel)){
				 
					res = u.addXmlElement(res,"type","propose");
					res = u.addXmlElement(res,"sender",name);
					 
 					res = u.addXmlElement(res,"content",kb.ExtractServiceFromLabel(taskLabel)+"&"+name) ;	//ServiceX & LabelX & QosX & name
					 
					 
				}
				else{
					//Answer that he can't 
					res = u.addXmlElement(res,"type","failure");
					res = u.addXmlElement(res,"sender",name);
					res = u.addXmlElement(res,"content","No Service available for this task") ;
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
			public void sendExclusList(String urlDelegue,Set<String> members, String request) throws IOException
			{
				String message=new String();
				message=u.addXmlElement(message, "request", request);
				message=u.addXmlElement(message,"membersNumber",String.valueOf(members.size()));
				int i=0;
				for(String member : members)
				{
					message=u.addXmlElement(message,"avatar"+i,member);
					i++;
					   
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
 					
					System.out.println("AVATAR: HTTP RESPONSE :"+ response2.getRepresentation());
					

				} catch (IOException e) {
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
			/****************************Selection*******************************/
			public void sendMembersSelection(String urlDelegue,ArrayList<String> clustermembers,int d,int id) throws IOException
			{
				String message=new String();
				message=u.addXmlElement(message,"membersNumber",String.valueOf(clustermembers.size()));
				message=u.addXmlElement(message,"level",String.valueOf(d));
				message=u.addXmlElement(message,"id",String.valueOf(id));

 

				for(int i=0;i<clustermembers.size();i++)
				{
				message=u.addXmlElement(message,"avatar"+i,clustermembers.get(i));
				}
				
				 
				 
				try {
				client.whenUseHttpAsyncClient_thenCorrect(urlDelegue+"receiveMembersSelection/", ORIGINATOR, message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("AVATAR: HTTP RESPONSE :"+ response2.getRepresentation());	
				
			 
 

			}
			public void sendAsynTest(int nbCluster,int nbAvatar,int d,SelectionManager sm)
			{
				//init toget
				String toGet[]=new String[nbCluster];
				for(int k=2;k<nbCluster+2;k++)
				{
					toGet[k-2]="http://localhost:300"+k+"/receiveMembersSelection/";
				}
				 
				String[] cnt=new String[nbCluster];
				for(int i=0;i<nbCluster;i++)
				{
					String message=new String();
					message=u.addXmlElement(message,"membersNumber",String.valueOf(nbAvatar));
					message=u.addXmlElement(message,"level",String.valueOf(d));
					message=u.addXmlElement(message,"id",String.valueOf(i));
					cnt[i]=message;
					
				}
				try {
					client.whenUseMultipleHttpAsyncClient_thenCorrect(toGet,ORIGINATOR, cnt,sm);
					 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
			public String getMemberListSelection(String response)
			{
				 
		    	int nbMembers=Integer.parseInt(u.getXmlElement(response, "membersNumber"));
		    	int d=Integer.parseInt(u.getXmlElement(response, "level"));		
		    	int id=Integer.parseInt(u.getXmlElement(response, "id"));	
              /*  listeMemberSelection.clear();
		    	System.out.println("number of Cluster members "+nbMembers);
		    	for (int i=0;i<nbMembers;i++)
		    	{
		    		listeMemberSelection.add(u.getXmlElement(response, "avatar"+i));
		    	}
		    
		    	System.out.println("Liste cluster member= "+this.listeMemberSelection.toString());*/
		    
				 
				
				double [] w={0.4,0.3};
				 long startTime = System.nanoTime();
				c=new ClusterQoS(q.fillClusterData(nbMembers, id), w);

				 long elapsedTime = System.nanoTime() - startTime;
		         System.out.println("Total execution time For dataset extraction in millis: "+elapsedTime/1000000f+" ms");
				c.getQualitLevel(d);
				
				double [][] utiliteToSend=new double [2][d];
				double [][] FluctToSend=new double [2][d];
				
				String message=new String();

				double []tab=new double[d];
				for(int i=0; i<2;i++)
				{
					tab=c.getLevelTab(i);
					for(int j=0;j<d;j++)
					{
						utiliteToSend[i][j]=new QualityLevel(c,i,tab[j]).p();
						FluctToSend[i][j]=new QualityLevel(c,i,tab[j]).f();
						message=u.addXmlElement(message,"level"+i+j,String.valueOf(tab[j])+"/"+String.valueOf(utiliteToSend[i][j])+"/"+String.valueOf(FluctToSend[i][j]));



					}
				}
			 message=u.addXmlElement(message, "id", String.valueOf(id));
			 message=u.addXmlElement(message,"time",String.valueOf(elapsedTime/1000000f));
			 System.out.println(message);
			 
			return(message);
				 
		    	

			}
			public void initCluster(int nbMembers, int id)
			{
				double [] w={0.4,0.3};
				c=new ClusterQoS(q.fillClusterData(nbMembers, id), w);
			}
			public void initDeleguates(int nbCluster)
			{
				
			 
				for (int i=2;i<nbCluster+2;i++)
				{
					 
					try {
						  client.request("http://localhost:300"+i+"/initD/", ORIGINATOR, "");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 				}
		         
		      
			}
			public void sendCalculatedQoS(int d,int id,int nbMembers)
			{
				
				double [] w={0.4,0.3};
				c=new ClusterQoS(q.fillClusterData(nbMembers, id), w);
				c.getQualitLevel(d);
				
				double [][] utiliteToSend=new double [2][d];
				double [][] FluctToSend=new double [2][d];
				
				String message=new String();

				double []tab=new double[d];
				for(int i=0; i<2;i++)
				{
					tab=c.getLevelTab(i);
					for(int j=0;j<d;j++)
					{
						utiliteToSend[i][j]=new QualityLevel(c,i,tab[j]).p();
						FluctToSend[i][j]=new QualityLevel(c,i,tab[j]).f();
						message=u.addXmlElement(message,"level"+i+j,String.valueOf(tab[j])+"/"+String.valueOf(utiliteToSend[i][j])+"/"+String.valueOf(FluctToSend[i][j]));



					}
				}
				message=u.addXmlElement(message,"id",String.valueOf(id));
				Response response2 = null;
				try {
					response2 = client.request("http://localhost:3001/receiveMembersSelection/", ORIGINATOR, message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("AVATAR: HTTP RESPONSE :"+ response2.getRepresentation());	
				
			}
			public void sendLocalConstraint(ArrayList<String> urlDelegue,double [][] cl) throws IOException
			{
				String message=new String();
				 
			     for(int k=0;k<urlDelegue.size();k++)
			     {
			    	 
			    	 for(int i=0;i<2;i++)
						{
				    	 message=u.addXmlElement(message,"cl"+i,String.valueOf(cl[i][k]));
				    	 

						}
			    	 
			    	  
			     
			    		Response response2 = client.request(urlDelegue.get(k)+"receiveCL/", ORIGINATOR, message);
			    		opt=opt+Double.valueOf(response2.getRepresentation());
						System.out.println("AVATAR: HTTP RESPONSE :"+ response2.getRepresentation());	
						message=new String();
			     }


			}
			 
			

}
