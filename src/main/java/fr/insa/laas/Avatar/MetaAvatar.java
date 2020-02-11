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
 		private ArrayList<String> functionsAble= new ArrayList<String>();
		private ArrayList<String> functionsNotAble= new ArrayList<String>();
		//Used to iterate and to get Level interest easily for each task
		//Social Distance with this metaAvatar
		private double socialDistance ;  
		//private String commonInterest;	//TBD Maybe	
		//REST
		private String URL;
		
		//Constructor
		public MetaAvatar(String n, String o, double la, double lo, Map<String,Double> iv,ArrayList<String> lf,ArrayList<String> lfn, double sd, String u){
			name=n;
			owner=o;
			latitude=la;
			longitude=lo;
			interestsVector=iv;
			socialDistance=sd;
			functionsAble=lf;
			functionsNotAble=lfn;
			URL=u;
		}
		
		//Check if an avatar contains a certain interest, if YES: Return its level of interest, if FALSE: Return -1.0
		public double ContainsInterest(String interest){
			double res=-1.0;
			if (interestsVector.containsKey(interest)) res=interestsVector.get(interest);
			return res;
		}
		public ArrayList<String> getFunctions()
		{
			return this.functionsAble;
		}
		public Interest getInterest(String interest){
			Interest res=null;
			
			if (interestsVector.containsKey(interest))
			{
				res= new Interest(interest, interestsVector.get(interest));
			}

			 
			return res;
		}
		public String getFunction(String Function){
			String res=null;
			for (int i=0; i<functionsAble.size(); i++){
				if (functionsAble.get(i).equals(Function)){
					res=functionsAble.get(i);
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
	 
		public void putSocialDistance(double sd){
			socialDistance=sd;
		}
				
}
