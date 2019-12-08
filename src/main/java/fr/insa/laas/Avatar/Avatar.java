package fr.insa.laas.Avatar;

import java.util.ArrayList;

 


 

public class Avatar {
	
	 

   
    private  String name;
   
    private String owner;
    private double latitude=99;
	private double longitude=99;
	private ArrayList <Interest> interestsList =new ArrayList <Interest> ();
	private ArrayList <Service> servicesList = new ArrayList <Service> ();
	private ArrayList <Goal> goalList = new ArrayList <Goal> ();


    private KnowledgeManagement kb;

  
	public Avatar(int port) {
		
		 
		this.kb=new KnowledgeManagement("src/main/resources/OntologyFiles/Avatar"+port+".owl");
		this.name=kb.ExtractName();
		this.owner=kb.ExtractOwner();
		this.latitude=kb.ExtractLatitude();
		this.longitude=kb.ExtractLongitude();
		this.interestsList=kb.ExtractInterests();
		this.goalList=kb.ExtractGoals();
		this.servicesList=kb.ExtractServices(this.name);
		
		 
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

}
