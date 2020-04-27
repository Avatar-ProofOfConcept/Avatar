package fr.insa.laas.Avatar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
 

 

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
  	private SelectionManager sm=new SelectionManager();
  	private QoSManager q=new QoSManager();
    
   
	public Avatar(int port) {
		
		System.out.println("--------------------------- [Actif AVATAR] Adresse IP : localhost - Port : "+port+" -------------------------------");
		System.out.println();
		System.out.println();
		System.out.println();
	 

 		this.kb=new KnowledgeManagement("avatars/avatar"+port+".owl");
 		
		 
		
		this.name=kb.ExtractName();
		 
 		URL="http://localhost:"+port+"/";
        this.owner=kb.ExtractOwner();
		this.latitude=kb.ExtractLatitude();
		this.longitude=kb.ExtractLongitude();
		this.interestsVector=kb.ExtractInterests();
 		this.goalList=kb.ExtractGoals(FunctionTasksListAble,FunctionTasksListNotAble);
        this.servicesList=kb.ExtractServices(this.name);
 		
 		//kb.ExtractMetaAvatars(this.interestsVector.keySet());
 		//System.out.println("--------------------------- [NAME : "+name+" URL : "+URL+" OWNER : "+owner+"] -------------------------------");
		//System.out.println("--------------------------- [LATITUDE : "+latitude+" LONGITUDE : "+longitude+"] -------------------------------");
		 
        
 		cm=new CommunicationManagement(port,this.kb,new MetaAvatar(name, owner, latitude, longitude, interestsVector,FunctionTasksListAble,FunctionTasksListNotAble,2,URL));

 		 
		
		/**********************************/		
		if (port==3001)
		{
			System.out.println("--------------------------- [I'm the initiator  ] -------------------------------");
			//this.cm.initCluster(10,port-3002);
		     
			//this.cm.sendCalculatedQoS(4, port-3002,10);
			long startTime = System.nanoTime();
			 sm.sendSelectionRequest(this.cm,10,5,4);//level qualité as parameter
			 long elapsedTime = System.nanoTime() - startTime;
	         System.out.println("Total execution time For service selection in millis: "+elapsedTime/1000000f+ " "+(elapsedTime/1000000f - sm.getMax())+" ms");

			
			/*System.out.println(" functionnalities to discover "+this.FunctionTasksListNotAble.toString());
			 System.out.println("social network size");
			 Scanner sc= new Scanner(System.in);
		     String avatar = sc.nextLine();
		     System.out.println("alpha, beta, gama");
		     String a = sc.nextLine();
		     String b = sc.nextLine();
		     String c = sc.nextLine();
		     SocialNetwork.setSize(Integer.valueOf(avatar));
		     SocialNetwork.setParameters(Float.valueOf(a),Float.valueOf(b),Float.valueOf(c));
			System.out.println("--------------------------- [Meta data extraction and social network construction size ="+ avatar+"  α ="+a+"  β="+b+"  γ="+c+"] -------------------------------");

			 cmean = new FuzzyClustering();
			 System.out.println("cluster size");
		     String p = sc.nextLine();
		     cmean.setP(Integer.valueOf(p));
			 startTime = System.nanoTime();
			 cm.getSocialNetwork().setMetaAvatar(kb.ExtractMetaAvatars(interestsVector.keySet(),name));
			
			 cm.getSocialNetwork().socialNetworkConstruction(Integer.valueOf(avatar));
			 elapsedTime = System.nanoTime() - startTime;
		     System.out.println("Total execution time in millis: "+ elapsedTime/1000000f);*/
            
		 
		}
		
		 
		 
	}
 



	public Map<String, Double> getInterestsVector() {
		return interestsVector;
	}




	public void setInterestsVector(Map<String, Double> interestsVector) {
		this.interestsVector = interestsVector;
	}




	public ArrayList<Goal> getGoalList() {
		return goalList;
	}




	public void setGoalList(ArrayList<Goal> goalList) {
		this.goalList = goalList;
	}




	public CommunicationManagement getCm() {
		return cm;
	}




	public void setCm(CommunicationManagement cm) {
		this.cm = cm;
	}




	public IExtract getKb() {
		return kb;
	}




	public void setKb(IExtract kb) {
		this.kb = kb;
	}




	public ArrayList<String> getFunctionTasksListAble() {
		return FunctionTasksListAble;
	}




	public void setFunctionTasksListAble(ArrayList<String> functionTasksListAble) {
		FunctionTasksListAble = functionTasksListAble;
	}




	public ArrayList<String> getFunctionTasksListNotAble() {
		return FunctionTasksListNotAble;
	}




	public void setFunctionTasksListNotAble(
			ArrayList<String> functionTasksListNotAble) {
		FunctionTasksListNotAble = functionTasksListNotAble;
	}




	public String getURL() {
		return URL;
	}




	public void setURL(String uRL) {
		URL = uRL;
	}




	public FuzzyClustering getCmean() {
		return cmean;
	}




	public void setCmean(FuzzyClustering cmean) {
		this.cmean = cmean;
	}




	public SelectionManager getSm() {
		return sm;
	}




	public void setSm(SelectionManager sm) {
		this.sm = sm;
	}




	public void setName(String name) {
		this.name = name;
	}




	public void setOwner(String owner) {
		this.owner = owner;
	}




	public void setServicesList(ArrayList<Service> servicesList) {
		this.servicesList = servicesList;
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

     if (cm.getSocialNetwork().getSocialNetwork().size() > 0)
     {
    	 cmean.run(FunctionTasksListNotAble.size(),cmean.data,1,2f,0.2);
     }
     else
    	 System.out.println("SOCIAL NETWORK NULL");

    
    
		 
		
	}
	public void discovery(ArrayList <Task> tasksList)
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
				for (int s=0; s<tasksList.size();s++){
					//Able
					if(tasksList.get(s).getIsAble()){
						System.out.println("["+name+"] "+tasksList.get(s).getLabel()+": Able");
						//cptTasks++;
						System.out.println("	[CAN DO TASK ITSELF]"+name+": "+tasksList.get(s).getLabel());
						tasksList.get(s).setActor(name);

					}
					//Non Able ==> Check if grouped
					else 
					{
						 
						System.out.println("["+name+"] "+tasksList.get(s).getLabel()+": not Able");
 						System.out.println("	[CAN NOT DO TASK ITSELF]"+name+": "+tasksList.get(s).getLabel() + " Functionnality : "+tasksList.get(s).getFunction());
 						Response resp=cm.ask(tasksList.get(s).getContent()+"&"+tasksList.get(s).getLabel()+"&"+tasksList.get(s).getFunction(),URL,ClusteringTable.get(tasksList.get(s).getFunction())+"delegu/",cpt,FunctionTasksListNotAble.size());
 						if (resp.getRepresentation().isEmpty()==false)
 						{
 							System.out.println(resp.getRepresentation());
 							cm.savePropositions(resp.getRepresentation(),tasksList.get(s).getContent()+"&"+tasksList.get(s).getLabel()+"&"+tasksList.get(s).getFunction());
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


}
