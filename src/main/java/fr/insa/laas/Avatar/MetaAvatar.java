package fr.insa.laas.Avatar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

 
public class MetaAvatar {
	//Attributs
		private String name ;
		private String owner;
		//location
		private double latitude;
		private double longitude;
		//Interests
		private Map<String,Double> interestsVector = new HashMap<String, Double>();			//Used to calculate the Social Distance using its vector shape
		private ArrayList <Interest> interestsList = new ArrayList <Interest> () ;
		private ArrayList<String> functions= new ArrayList<String>();
		//Used to iterate and to get Level interest easily for each task
		//Social Distance with this metaAvatar
		private double socialDistance ;  
		//private String commonInterest;	//TBD Maybe	
		//REST
		private String URL;
		
		//Constructor
		public MetaAvatar(String n, String o, double la, double lo, Map<String,Double> iv,ArrayList <Interest> il,ArrayList<String> lf, double sd, String u){
			name=n;
			owner=o;
			latitude=la;
			longitude=lo;
			interestsVector=iv;
			interestsList=il;
			socialDistance=sd;
			functions=lf;
			URL=u;
		}
		
		//Check if an avatar contains a certain interest, if YES: Return its level of interest, if FALSE: Return -1.0
		public double ContainsInterest(String interest){
			double res=-1.0;
			for (int i=0; i<interestsList.size(); i++){
				if (interestsList.get(i).getName().equals(interest)){
					res=interestsList.get(i).getLevel();
					break;
				}
			}
			return res;
		}
		public Interest getInterest(String interest){
			Interest res=null;
			for (int i=0; i<interestsList.size(); i++){
				if (interestsList.get(i).getName().equals(interest)){
					res=interestsList.get(i);
					break;
				}
			}
			return res;
		}
		public String getFunction(String Function){
			String res=null;
			for (int i=0; i<functions.size(); i++){
				if (functions.get(i).equals(Function)){
					res=functions.get(i);
					break;
				}
			}
			return res;
		}
		//Getters & Setters
		public String getName(){
			return name ;
		}
		public String getOwner(){
			return owner ;
		}
		public String getURL(){
			return URL ;
		}
		public Double getLatitude(){
			return latitude ;
		}
		public Double getLongitude(){
			return longitude ;
		}
		public Map<String,Double> getInterestsVector(){
			return interestsVector;
		}
		public ArrayList <Interest> getInterestsList(){
			return interestsList;
		}
		public void putSocialDistance(double sd){
			socialDistance=sd;
		}
				
}
