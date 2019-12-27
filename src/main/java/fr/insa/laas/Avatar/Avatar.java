package fr.insa.laas.Avatar;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

 

 

public class Avatar {
	
	 

    private int id;
    private String name;
    private String owner;
 	private double latitude=99;
	private double longitude=99;
	private ArrayList <Interest> interestsList =new ArrayList <Interest> ();
	private Map<String,Double> interestsVector = new HashMap<String, Double>();	//Used to calculate the Social Distance using its vector shape
    private ArrayList <Service> servicesList = new ArrayList <Service> ();
	private ArrayList <Goal> goalList = new ArrayList <Goal> ();
	private CommunicationManagement cm;
    private IExtract kb;
    private ArrayList<String> FunctionTasksList=new ArrayList<String>();
  	private MetaAvatar metaAvatar = null ; 		 
  	private final String ORIGINATOR = "admin:admin";
   	private ServicesManager sm ;
   	private String URL;
  	private FuzzyClustering cmean ;
    
   
	public Avatar(int port) {
		
		
 		this.kb=new KnowledgeManagement("src/main/resources/OntologyFiles/Avatar"+port+".owl");
		this.name=kb.ExtractName();
		this.id=Integer.parseInt(name.split("Avatar")[1]);
 		URL="http://localhost:"+port+"/";
        this.owner=kb.ExtractOwner();
		this.latitude=kb.ExtractLatitude();
		this.longitude=kb.ExtractLongitude();
		this.interestsList=kb.ExtractInterests();
 		this.goalList=kb.ExtractGoals(FunctionTasksList);
 		System.out.println("interssssst"+FunctionTasksList.toString());
 		cm=new CommunicationManagement(port,this.kb,new MetaAvatar(name, owner, latitude, longitude, interestsVector, interestsList,FunctionTasksList,2,URL));

        this.servicesList=kb.ExtractServices(this.name);
 		sm = new ServicesManager(this.name);
		cmean = new FuzzyClustering();
		/**********************************/
		
		
	     
    //this.socialNetwork.socialNetworkConstruction(metaAvatar,3);
		
		if (port==3001){
			cluster();
			discovery();
		 
		}
		else { System.out.println("je suis le 3002");}
		try {TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
        /*FriendsResearch();
		sm.UpdateSN(socialNetwork);
		if (port==3001)
		{
		if (!goalList.isEmpty() ){//&& name.equals("Avatar1")){
			try {
				BrowseTasks(goalList.get(0).getTasksList());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		}
		else System.out.println("je suis fournisseur");*/
		 
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



	public ArrayList<Interest> getInterestsList() {
		return interestsList;
	}
	public ArrayList<Goal> getGoalsList() {
		return goalList;
	}



	public void setInterestsList(ArrayList<Interest> interestsList) {
		this.interestsList = interestsList;
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
		 
    /******Build clustering matrix**********/
    for (int i=0;i< cm.getSocialNetwork().metaAvatars.size();i++)
    {
    	System.out.println(" meta size "+cm.getSocialNetwork().metaAvatars.size());

        ArrayList<Integer> tmp = new ArrayList<>();
    	for (int k=0;k<FunctionTasksList.size();k++)
    	{     	System.out.println(" it size "+FunctionTasksList.size());

    		String it=cm.getSocialNetwork().metaAvatars.get(i).getFunction(FunctionTasksList.get(k));
    		if (it==null)
    			tmp.add(0);
    		else
    			tmp.add(1);
    	}
    	System.out.println(" tmppp "+tmp.toString());
    	cmean.data.add(tmp);
    }

    this.cmean.setdimension(4);
    this.cmean.run(4, 100);

    
    
		 
		
	}
	public void discovery()
	{
		//1 design an elected
		HashMap<String, String> ClusteringTable=new HashMap<String, String>();
		for(int i=0 ;i<cmean.getClusterNumber();i++)
		{
			ClusteringTable.put(FunctionTasksList.get(i),this.cm.getSocialNetwork().getAvatars().get(cmean.getAvatarsList().get(i)[0].getId()).getURL());
 
  			
			try {
				this.cm.sendClusterMember(ClusteringTable.get(FunctionTasksList.get(i)),this.cmean.getClusterMembers(i,cm.getSocialNetwork().metaAvatars));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("elected "+ClusteringTable[i][1]);

		}
		//2 send cluster membership to the elected
		//3 build clustering table
		//System.out.println("[BROWSE TASKS]"+name+": "+goalsList.get(0).getName());
				for (int s=0; s<goalList.get(0).getTasksList().size();s++){
					//Able
					if(goalList.get(0).getTasksList().get(s).getIsAble()){
						System.out.println("["+name+"] "+goalList.get(0).getTasksList().get(s).getContent()+": Able");
						//cptTasks++;
						System.out.println("	[CAN DO TASK ITSELF]"+name+": "+goalList.get(0).getTasksList().get(s).getContent());
						goalList.get(0).getTasksList().get(s).setActor(name);

					}
					//Non Able ==> Check if grouped
					else {
						System.out.println("["+name+"] "+goalList.get(0).getTasksList().get(s).getContent()+": not Able");
 						System.out.println("	[CAN NOT DO TASK ITSELF]"+name+": "+goalList.get(0).getTasksList().get(s).getContent());
 						String function =goalList.get(0).getTasksList().get(s).getFunction();
 						System.out.println("Function "+function+" value "+ClusteringTable.get(function));
 						cm.ask(goalList.get(0).getTasksList().get(s).getContent()+"&"+goalList.get(0).getTasksList().get(s).getLabel()+"&"+goalList.get(0).getTasksList().get(s).getFunction(),URL,ClusteringTable.get(function));

					}
				}
		
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
