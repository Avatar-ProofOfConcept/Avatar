package fr.insa.laas.Avatar;

 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


public class SocialNetwork {
	
	//Attributs
		
		//Social Network
		private MetaAvatar metaAvatar;
		private int size; // SN size
		private ArrayList <MetaAvatar> friendFromRepo = new ArrayList <MetaAvatar>() ; 
		ArrayList <MetaAvatar> socialNetwork = new ArrayList <MetaAvatar>() ; 
 		//Social Distance Calculs
		private SocialNetworkCalculs socialDistance = new SocialNetworkCalculs();
		//set of data for test
		 ArrayList<MetaAvatar> metaAvatars;
		
	
		public void setSocialNetwork(ArrayList <MetaAvatar> sn)
		{
			this.socialNetwork=sn;
		}
		public SocialNetwork(MetaAvatar m) {
			this.metaAvatar=m;
			ArrayList<String> il=new ArrayList<String>();
		    il.add(new String("FunctionA"));
		    il.add(new String("FunctionM"));
		    il.add(new String("FunctionL"));
		    il.add(new String("FunctionB"));
		    il.add(new String("FunctionN"));
	   
		metaAvatars.add(new MetaAvatar("Avatar2", "in", 555, 555, new HashMap<String,Double>(),new ArrayList<Interest>(), il, 555, "http://localhost:3002/"));
	    il=new ArrayList<String>();
	    il.add(new String("FunctionA"));
	    il.add(new String("FunctionN"));
	    il.add(new String("FunctionB"));
	    il.add(new String("FunctionM"));
	    metaAvatars.add(new MetaAvatar("Avatar3", "in", 555, 555, new HashMap<String,Double>(),new ArrayList<Interest>(), il, 555, "http://localhost:3003/"));
	    il=new ArrayList<String>();
	    il.add(new String("FunctionF"));
	    il.add(new String("FunctionY"));
	    metaAvatars.add(new MetaAvatar("Avatar4", "in", 555, 555, new HashMap<String,Double>(),new ArrayList<Interest>(), il, 555, "http://localhost:3004/"));
	    il=new ArrayList<String>();
	    il.add(new String("FunctionF"));
	    metaAvatars.add(new MetaAvatar("Avatar5", "in", 555, 555, new HashMap<String,Double>(), new ArrayList<Interest>(), il, 555, "http://localhost:3005/"));

	    il=new ArrayList<String>();
	    il.add(new String("FunctionM"));
	    metaAvatars.add(new MetaAvatar("Avatar6", "in", 555, 555, new HashMap<String,Double>(), new ArrayList<Interest>(), il, 555, "http://localhost:3006/"));
	    friendFromRepo=metaAvatars;

 		}
		public void socialNetworkConstruction(int k,ArrayList<String> exclus)
		{
			this.size=k;
			fr.insa.laas.Avatar.Element [] SDs=new fr.insa.laas.Avatar.Element[friendFromRepo.size()];
			for (int i=0;i<this.friendFromRepo.size();i++)
			{
				SDs[i]=new fr.insa.laas.Avatar.Element(i,(float)(socialDistance.SocialDistance(metaAvatar,friendFromRepo.get(i),0.4f,0.4f,0.4f)));
				 
				System.out.println(""+SDs[i].getW()+" "+SDs[i].getId());
			}
			Arrays.sort(SDs,Collections.reverseOrder());
			int cpt=0;
			while(cpt < Math.min(size, friendFromRepo.size()))
			{
				if(!exclus.contains(friendFromRepo.get(cpt).getURL()))
				{
				socialNetwork.add(friendFromRepo.get(SDs[cpt].getId()));
				System.out.println(friendFromRepo.get(SDs[cpt].getId()).getName());
				cpt++;
				}
				
				
			}
		
		}
		public void socialNetworkConstruction(int k)
		{
			this.size=k;
			fr.insa.laas.Avatar.Element [] SDs=new fr.insa.laas.Avatar.Element[friendFromRepo.size()];
			for (int i=0;i<this.friendFromRepo.size();i++)
			{
				SDs[i]=new fr.insa.laas.Avatar.Element(i,(float)(socialDistance.SocialDistance(metaAvatar,friendFromRepo.get(i),0.4f,0.4f,0.4f)));
				 
			}
			Arrays.sort(SDs,Collections.reverseOrder());
 
			for(int j=0;j<Math.min(size,friendFromRepo.size());j++)
			{
				socialNetwork.add(friendFromRepo.get(SDs[j].getId()));
				System.out.println(friendFromRepo.get(SDs[j].getId()).getName());
				
				
			}
		
		}
	 
		public ArrayList <MetaAvatar> getAvatars()
		{
			return this.socialNetwork;
		}
		
		
		
	
	 
		
		
							/****				Getters & Setters				******/
		
	public void addAvatar (MetaAvatar avatar){
			socialNetwork.add(avatar) ;
		}
		public  ArrayList <MetaAvatar> getSocialNetwork (){
			return socialNetwork ;
		}
 
		
	 
		
	 
		
		//Returns true if he has a friend with this name
		public boolean ContainsFriend(String name){
			boolean res=false;
			for (int i=0; i<socialNetwork.size(); i++){
				if (socialNetwork.get(i).getName().equals(name)){
					res=true;
					break;
				}
			}
			return res;
		}
		
		//Return the meta data of an avatar friend from its name
		public MetaAvatar getFriend(String name){
			MetaAvatar res=null;
			for (int i=0; i<socialNetwork.size(); i++){
				if (socialNetwork.get(i).getName().equals(name)){
					res=socialNetwork.get(i);
					break;
				}
			}
			return res;
		}
		

}
