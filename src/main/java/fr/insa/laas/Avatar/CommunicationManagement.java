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

	
	public CommunicationManagement (int port,IExtract kb,MetaAvatar m)
	{
	  this.kb=kb;
	  this.metaavatar=m;
	  this.socialNetwork=new SocialNetwork(m);
	  SocialNetwork.setParameters(0.4f, 0.4f,0.2f);
	  SocialNetwork.setSize(40);
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
			public String sendMembersSelection(String urlDelegue,ArrayList<String> clustermembers,int d) throws IOException
			{
				String message=new String();
				message=u.addXmlElement(message,"membersNumber",String.valueOf(clustermembers.size()));
				message=u.addXmlElement(message,"level",String.valueOf(d));

 

				for(int i=0;i<clustermembers.size();i++)
				{
				message=u.addXmlElement(message,"avatar"+i,clustermembers.get(i));
				}
				 
				Response response2 = client.request(urlDelegue+"receiveMembersSelection/", ORIGINATOR, message);
				System.out.println("AVATAR: HTTP RESPONSE :"+ response2.getRepresentation());	
				
				return response2.getRepresentation();
 

			}
			
			public String getMemberListSelection(String response)
			{
				 
		    	int nbMembers=Integer.parseInt(u.getXmlElement(response, "membersNumber"));
		    	int d=Integer.parseInt(u.getXmlElement(response, "level"));			
                listeMemberSelection.clear();
		    	System.out.println("number of Cluster members "+nbMembers);
		    	for (int i=0;i<nbMembers;i++)
		    	{
		    		listeMemberSelection.add(u.getXmlElement(response, "avatar"+i));
		    	}
		    
		    	System.out.println("Liste cluster member= "+this.listeMemberSelection.toString());
		    	//resup de repo : construire une liste de cluster memeberQoS
		    	//construire un cluster
		    	
		    	double [] qos5={ 82.01032,
						0.11535,0.23347,
						 1177.57631
						 };
				double []histT={ 646.45092,
						  527.73580,
						  498.19995,
						  66.92103,
						  928.72121
						 };
				double []histD={0.15851,
						 0.10799,
						 0.82196,
						 0.19954,
						 0.69532
						 };
				double []histR={
						 0.45472,
						 0.30162,
						 0.65701,
						 0.00646,
						 0.67320

				};
				ClusterMemberQoS a5=new ClusterMemberQoS(qos5,histD,histT,histR);
				double [] qos7={106.76806  ,
						0.51119, 0.91815,
						 698.33104
						};
				
				
				double []histT1={ 746.45092,
						  527.73580,
						  328.19995,
						  68.92103,
						  128.72121
						 };
				double []histD1={0.15851,
						 0.10799,
						 0.82196,
						 0.49954,
						 0.9532
						 };
				double []histR1={
						 0.3547,
						 0.38162,
						 0.65701,
						 0.10646,
						 0.77320

				};
				ClusterMemberQoS a7=new ClusterMemberQoS(qos7,histD1,histT1,histR1);
				double [] qos1={ 108.22717,0.71044,
						 0.65604,
						 1228.66591
						};
				ClusterMemberQoS a1=new ClusterMemberQoS(qos1,histD,histT,histR);
				
				double [] qos10={82.88946,0.33892,
						 0.51071,
						 1186.29805
						};
				ClusterMemberQoS a10=new ClusterMemberQoS(qos10,histD,histT,histR);
				
				double [] qos4={ 85.03852, 0.50077,0.66878,
						 725.77046
						 };
				ClusterMemberQoS a4=new ClusterMemberQoS(qos4,histD,histT,histR);
				
				double [] qos6={16.85412,0.23927, 0.70881,
						 717.45341
						};
				ClusterMemberQoS a6=new ClusterMemberQoS(qos6,histD,histT,histR);
				
				double [] qos8={ 110.16563,0.81851,0.57681,
						 
						 1496.13120
						 };
				ClusterMemberQoS a8=new ClusterMemberQoS(qos8,histD,histT,histR);
				ArrayList<ClusterMemberQoS>a=new ArrayList<ClusterMemberQoS>();
				a.add(a5);
				a.add(a7);
				a.add(a1);
				a.add(a10);
				a.add(a4);
				a.add(a6);
				a.add(a8);
				
				double [] w={0.4,0.3,0.2,0.1};
				ClusterQoS c=new ClusterQoS(a,w);
				c.getQualitLevel(4);
				
				double [][] utiliteToSend=new double [4][d];
				double [][] FluctToSend=new double [4][d];
				
				String message=new String();

				double []tab=new double[d];
				for(int i=0; i<4;i++)
				{
					tab=c.getLevelTab(i);
					for(int j=0;j<d;j++)
					{
						utiliteToSend[i][j]=new QualityLevel(c,i,tab[j]).p();
						FluctToSend[i][j]=new QualityLevel(c,i,tab[j]).f();
						System.out.println("**"+tab[j]);
						System.out.println("utili "+i+j+"="+utiliteToSend[i][j]);
						System.out.println("fluc "+i+j+"="+FluctToSend[i][j]);
						message=u.addXmlElement(message,"level"+i+j,String.valueOf(tab[j])+"/"+String.valueOf(utiliteToSend[i][j])+"/"+String.valueOf(FluctToSend[i][j]));



					}
				}
			 
				 
				return message;
		    	

			}
			public void sendDataSelection(String initiator,ArrayList<String> clustermembers) throws IOException
			{
					
 

			}
			

}
