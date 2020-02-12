package fr.insa.laas.Avatar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
 

 

public class Avatar {
	
	 

  
    private String name;
    private String owner;
 	private double latitude=99;
	private double longitude=99;
 	private Map<String,Double> interestsVector = new HashMap<String, Double>();	//Used to calculate the Social Distance using its vector shape
    private ArrayList <Service> servicesList = new ArrayList <Service> ();
	private ArrayList <Goal> goalList = new ArrayList <Goal> ();
	private CommunicationManagement cm;
    private IExtract kb;
    private ArrayList<String> FunctionTasksListAble=new ArrayList<String>();
    private ArrayList<String> FunctionTasksListNotAble=new ArrayList<String>();
  	private MetaAvatar metaAvatar = null ; 		 
    private String URL;
  	private FuzzyClustering cmean ;
    
   
	public Avatar(int port) {
		
		System.out.println("--------------------------- [Actif AVATAR] Adresse IP : localhost - Port : "+port+" -------------------------------");
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("--------------------------- [First Step : Semantic Data Extraction ] -------------------------------");

 		this.kb=new KnowledgeManagement("src/main/resources/OntologyFiles/avatar"+port+".owl");
 		long startTime = System.nanoTime();
		 
		
		this.name=kb.ExtractName();
		 
 		URL="http://localhost:"+port+"/";
        this.owner=kb.ExtractOwner();
		this.latitude=kb.ExtractLatitude();
		this.longitude=kb.ExtractLongitude();
		this.interestsVector=kb.ExtractInterests();
 		this.goalList=kb.ExtractGoals(FunctionTasksListAble,FunctionTasksListNotAble);
        this.servicesList=kb.ExtractServices(this.name);
 		long elapsedTime = System.nanoTime() - startTime;
 		//kb.ExtractMetaAvatars(this.interestsVector.keySet());
 		System.out.println("--------------------------- [NAME : "+name+" URL : "+URL+" OWNER : "+owner+"] -------------------------------");
		System.out.println("--------------------------- [LATITUDE : "+latitude+" LONGITUDE : "+longitude+"] -------------------------------");
		 
        System.out.println("Total execution time For semantic Extraction in millis: "+ elapsedTime/1000000f+" ms");
 		cm=new CommunicationManagement(port,this.kb,new MetaAvatar(name, owner, latitude, longitude, interestsVector,FunctionTasksListAble,FunctionTasksListNotAble,2,URL));

 		 
		
		/**********************************/		
		if (port==3001)
		{
			System.out.println("--------------------------- [I'm the initiator  ] -------------------------------");
			System.out.println(" functionnalities to discover "+this.FunctionTasksListNotAble.toString());
			System.out.println("--------------------------- [Meta data extraction and social network construction size = 9  α = 0.4  β=0.4  γ=0.4] -------------------------------");

			 cmean = new FuzzyClustering();
			 startTime = System.nanoTime();
			 cm.getSocialNetwork().setMetaAvatar(kb.ExtractMetaAvatars(interestsVector.keySet(),name));
			 cm.getSocialNetwork().socialNetworkConstruction(9);
			 elapsedTime = System.nanoTime() - startTime;
		     System.out.println("Total execution time in millis: "+ elapsedTime/1000000f);
            
		 
		}
		
		 
		 
	}
 



	public MetaAvatar getMetaAvatar() {
		return metaAvatar;
	}



	public void setMetaAvatar(MetaAvatar metaAvatar) {
		this.metaAvatar = metaAvatar;
	}


 






	public double getLatitude() {
		return latitude;
	}



	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}



	public double getLongitude() {
		return longitude;
	}



	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}



	 
	public ArrayList<Goal> getGoalsList() {
		return goalList;
	}



	 
  public CommunicationManagement getComManager()
  {
	  return cm;
  }
   public ArrayList<Service> getServicesList ()
   {
	   return servicesList;
   }

	public String getName() {
        return name;
    }
	public String getOwner() {
        return owner;
    }
	public void cluster ()
	{ 
    	 
		System.out.println("--------------------------- [Start Fuzzy Clustering : m=2 Distance = Eucidienne standard epsilon=0.2 ] -------------------------------");

		 
    /******Build clustering matrix**********/
    for (int i=0;i< cm.getSocialNetwork().getSocialNetwork().size();i++)
    {
    	//System.out.println("Avatar"+cm.getSocialNetwork().getSocialNetwork().get(i).getName()+" Function list "+cm.getSocialNetwork().getSocialNetwork().get(i).getFunctions().toString());

        ArrayList<Integer> tmp = new ArrayList<>();
    	for (int k=0;k<FunctionTasksListNotAble.size();k++)
    	{     	 

    		String it=cm.getSocialNetwork().getSocialNetwork().get(i).getFunction(FunctionTasksListNotAble.get(k));
    		if (it==null)
    			tmp.add(0);
    		else
    			tmp.add(1);
    	}
    	 
    	cmean.data.add(tmp);
    }

     
    cmean.run(FunctionTasksListNotAble.size(),cmean.data,1,2f,0.2,3);

    
    
		 
		
	}
	public void discovery()
	{
		System.out.println("--------------------------- [Start the discovery Process ] -------------------------------");
		long startTime = System.nanoTime();
		HashMap<String, String> ClusteringTable=new HashMap<String, String>();
		for(int i=0 ;i<cmean.getClusterNumber();i++)
		{   //1 design an elected
			ClusteringTable.put(FunctionTasksListNotAble.get(cmean.getAvatarsList().get(i).getFeature()),this.cm.getSocialNetwork().getAvatars().get(cmean.getAvatarsList().get(i).getElements()[0].getId()).getURL());
            System.out.print(FunctionTasksListNotAble.get(cmean.getAvatarsList().get(i).getFeature())+"   "+this.cm.getSocialNetwork().getAvatars().get(cmean.getAvatarsList().get(i).getElements()[0].getId()).getURL());
			//2 send cluster membership to the elected
			try {
				this.cm.sendClusterMember(cmean.getClusterNumber(),cmean.getAvatarsList().get(i).getFeature(),ClusteringTable.get(FunctionTasksListNotAble.get(cmean.getAvatarsList().get(i).getFeature())),this.cmean.getClusterMembers(i,cm.getSocialNetwork().socialNetwork));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
           
	}
	    
		
		 
		int cpt=0;
		cm.initTTL(FunctionTasksListNotAble.size());
				for (int s=0; s<goalList.get(0).getTasksList().size();s++){
					//Able
					if(goalList.get(0).getTasksList().get(s).getIsAble()){
						System.out.println("["+name+"] "+goalList.get(0).getTasksList().get(s).getLabel()+": Able");
						//cptTasks++;
						System.out.println("	[CAN DO TASK ITSELF]"+name+": "+goalList.get(0).getTasksList().get(s).getLabel());
						goalList.get(0).getTasksList().get(s).setActor(name);

					}
					//Non Able ==> Check if grouped
					else {
						 
						System.out.println("["+name+"] "+goalList.get(0).getTasksList().get(s).getLabel()+": not Able");
 						System.out.println("	[CAN NOT DO TASK ITSELF]"+name+": "+goalList.get(0).getTasksList().get(s).getLabel() + " Functionnality : "+goalList.get(0).getTasksList().get(s).getFunction());
 						Response resp=cm.ask(goalList.get(0).getTasksList().get(s).getContent()+"&"+goalList.get(0).getTasksList().get(s).getLabel()+"&"+goalList.get(0).getTasksList().get(s).getFunction(),URL,ClusteringTable.get(goalList.get(0).getTasksList().get(s).getFunction())+"delegu/",cpt,FunctionTasksListNotAble.size());
 						if (resp.getRepresentation().isEmpty()==false)
 						{
 							System.out.println(resp.getRepresentation());
 						cm.savePropositions(resp.getRepresentation(),goalList.get(0).getTasksList().get(s).getContent()+"&"+goalList.get(0).getTasksList().get(s).getLabel()+"&"+goalList.get(0).getTasksList().get(s).getFunction());
 						}
 						cpt++;
					}
				}
				long elapsedTime = System.nanoTime() - startTime;
				System.out.println("Discovery execution time in millis: "+ elapsedTime/1000000f);
				System.out.println("Discovery Resulats: ");
				cm.showTTLs();
				
				cm.showPropositions();
		
	}
	public void receivePropo(String response)
	{
		this.cm.savePropositions(response);
	}
 
	//Browse the tasks to deal with the tasks he can't execute
/*	public void BrowseTasks(ArrayList <Task> tasksList) throws IOException{
		//System.out.println("[BROWSE TASKS]"+name+": "+goalsList.get(0).getName());
		for (int s=0; s<tasksList.size();s++){
			//Able
			if(tasksList.get(s).getIsAble()){
				//System.out.println("["+name+"] "+tasksList.get(s).getContent()+": Able");
				//cptTasks++;
				//System.out.println("	[CAN DO TASK ITSELF]"+name+": "+tasksList.get(s).getContent()+", total="+cptTasks);
				tasksList.get(s).setActor(name);

			}
			//Non Able ==> Check if grouped
			else {
				//Grouped ==> Recursion on this tasks list composing this grouped Task
				if(tasksList.get(s).getGrouped()){
					//System.out.println(tasksList.get(s).getContent()+": Not Able and Grouped task");
					//WARNING!!! TBD: Can't he ask someone about the entire Grouped Task, before looking at its atomic tasks components
					BrowseTasks(tasksList.get(s).getTasksList());
				}
				//Non Grouped
				else {
					//ASK
					System.out.println(socialNetwork);
					String interest = tasksList.get(s).getInterest();
					String delegate = socialNetwork.getDelegate(interest);
					System.out.println("["+name+"] "+tasksList.get(s).getContent()+": Not Able and NOT Grouped task and will ask "+delegate+", it's an "+interest);
					//Check if he's its own delegate, if yes, he don't have to send this message
					if (delegate.equals(name)){
						//BroadCast to its SN
						cm.broadcastSN2(name,tasksList.get(s).getContent()+"&"+tasksList.get(s).getLabel()+"&"+tasksList.get(s).getInterest(),"newConversation",null, this.name,name,client,socialNetwork,metaAvatar,dm);					//Content: Task7&Label7&InterestP
					}
					//Send a message to the delegate to ask him to propagate the research of a friend of him who can do this task
					else{
						cm.sendDelegationTask2(delegate,tasksList.get(s).getContent()+"&"+tasksList.get(s).getLabel()+"&"+tasksList.get(s).getInterest(), "newConversation", null,name,client,socialNetwork,metaAvatar,dm);				//Content: Exp: Task7&Label7&InterestY
					}
				
				}

			}
		}
	}
*/
}
