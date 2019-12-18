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
    private ArrayList<String> InteretsTasksList=new ArrayList<String>();
  	private SocialNetwork socialNetwork ;
  	private MetaAvatar metaAvatar = null ; 		 
  	private final String ORIGINATOR = "admin:admin";
  	private ClientInterface client=new Client();
  	private ServicesManager sm ;
  	DelegationsManager dm;
  	private String URL;
  	private FuzzyClustering cmean ;
    

  
	public Avatar(int port) {
		
		
 		this.kb=new KnowledgeManagement("src/main/resources/OntologyFiles/Avatar"+port+".owl");
 		cm=new CommunicationManagement(port,this.kb);
		this.name=kb.ExtractName();
		this.id=Integer.parseInt(name.split("Avatar")[1]);
		System.out.println("My id is "+id);
		URL="http://localhost:"+port+"/"+name+"/";
        this.owner=kb.ExtractOwner();
		this.latitude=kb.ExtractLatitude();
		this.longitude=kb.ExtractLongitude();
		this.interestsList=kb.ExtractInterests();
		System.out.println("interests "+interestsList);
		this.goalList=kb.ExtractGoals(InteretsTasksList);
 		System.out.println("interests tasks "+InteretsTasksList.toString());

 
 		this.servicesList=kb.ExtractServices(this.name);
		dm=new DelegationsManager(this.name);
		sm = new ServicesManager(this.name);
		cmean = new FuzzyClustering();
		any ();
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

	public SocialNetwork getSocialNetwork() {
		return socialNetwork;
	}



	public void setSocialNetwork(SocialNetwork socialNetwork) {
		this.socialNetwork = socialNetwork;
	}



	public MetaAvatar getMetaAvatar() {
		return metaAvatar;
	}



	public void setMetaAvatar(MetaAvatar metaAvatar) {
		this.metaAvatar = metaAvatar;
	}



	public ClientInterface getClient() {
		return client;
	}



	public void setClient(ClientInterface client) {
		this.client = client;
	}



	public DelegationsManager getDm() {
		return dm;
	}
	public ServicesManager getSm()
	{
		return sm;
	}



	public void setDm(DelegationsManager dm) {
		this.dm = dm;
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
	public void any ()
	{ 
		/************INIT DATA****/
		ArrayList<MetaAvatar> metaAvatars =new ArrayList<MetaAvatar>();
	    ArrayList<Interest> il=new ArrayList<Interest>();
	    il.add(new Interest("InterestA",0.5 ));
	    il.add(new Interest("InterestM", 0.7));
	    il.add(new Interest("InterestL", 0.6));
	    il.add(new Interest("InterestB",0.8 ));
	    il.add(new Interest("InterestN",0.35 ));
    metaAvatars.add(new MetaAvatar("Avatar2", "in", 555, 555, new HashMap<String,Double>(), il, 555, "jjj"));
    il=new ArrayList<Interest>();
    il.add(new Interest("InterestA",0.7 ));
    il.add(new Interest("InterestN", 0.6));
    il.add(new Interest("InterestB", 0.5));
    il.add(new Interest("InterestM",0.1 ));
    metaAvatars.add(new MetaAvatar("Avatar3", "in", 555, 555, new HashMap<String,Double>(), il, 555, "jjj"));
    il=new ArrayList<Interest>();
    il.add(new Interest("InterestF",0.9 ));
    il.add(new Interest("InterestY", 0.7));
    metaAvatars.add(new MetaAvatar("Avatar4", "in", 555, 555, new HashMap<String,Double>(), il, 555, "jjj"));
    il=new ArrayList<Interest>();
    il.add(new Interest("InterestF",0.5 ));
    metaAvatars.add(new MetaAvatar("Avatar5", "in", 555, 555, new HashMap<String,Double>(), il, 555, "jjj"));

    il=new ArrayList<Interest>();
    il.add(new Interest("InterestM",0.95 ));
    metaAvatars.add(new MetaAvatar("Avatar6", "in", 555, 555, new HashMap<String,Double>(), il, 555, "jjj"));
    /******Build clustering matrix**********/
    for (int i=0;i< metaAvatars.size();i++)
    {
    	System.out.println(" meta size "+metaAvatars.size());

        ArrayList<Float> tmp = new ArrayList<>();
    	for (int k=0;k<InteretsTasksList.size();k++)
    	{     	System.out.println(" it size "+InteretsTasksList.size());

    		Interest it=metaAvatars.get(i).getInterest(InteretsTasksList.get(k));
    		if (it==null)
    			tmp.add(0f);
    		else
    			tmp.add((float)it.getLevel());
    	}
    	System.out.println(" tmppp "+tmp.toString());
    	cmean.data.add(tmp);
    }

    this.cmean.setdimension(4);
    this.cmean.run(4, 100);

    
    
		 
		
	}
	public void FriendsResearch(){
		
		//Create its metaAvatar to use it to calculate the Social Distance
		metaAvatar = new MetaAvatar(name, owner, latitude, longitude, interestsVector, interestsList, -99.0, URL);	//-99: It is a symolic value, as the Avatar don't have to calculate the SD with itself
		socialNetwork = new SocialNetwork(metaAvatar,InteretsTasksList);

 			try {
				Response resp = client.retrieve("http://localhost:8080/~/mn-cse/mn-name/Repository04/Repository04_DATA?rcn=4", ORIGINATOR);
				System.out.println("RESP: "+resp.getRepresentation());
				socialNetwork.SocialNetworkUpdate(resp.getRepresentation(), metaAvatar,InteretsTasksList);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("FriendsRes ERROR!");
			}
		 
		
		
	}	//TBD: RS Update !!!
	
	//Browse the tasks to deal with the tasks he can't execute
	public void BrowseTasks(ArrayList <Task> tasksList) throws IOException{
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

}
