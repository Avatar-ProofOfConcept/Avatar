package fr.insa.laas.Avatar;

 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


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
 		
	   public void setMetaAvatar(ArrayList<MetaAvatar> ls)
	   {
		   this.friendFromRepo=ls;
	   }
		public void setSocialNetwork(ArrayList <MetaAvatar> sn)
		{
			this.socialNetwork=sn;
		}
		public SocialNetwork(MetaAvatar m) {
			this.metaAvatar=m;
 	    //fill meta avatrs from KB to do
        
	   

 		}
		public MetaAvatar getMetaAvatar()
		{
			return this.metaAvatar;
		}
		public SocialNetwork() {
			 

 		}
		public ArrayList<String> socialNetworkConstruction(int k,ArrayList<String> exclus)
		{
			this.size=k;
			ArrayList<String> ls = new ArrayList<String>();
			fr.insa.laas.Avatar.Element [] SDs=new fr.insa.laas.Avatar.Element[friendFromRepo.size()];
			for (int i=0;i<this.friendFromRepo.size();i++)
			{
				SDs[i]=new fr.insa.laas.Avatar.Element(i,(float)(socialDistance.SocialDistance(metaAvatar,friendFromRepo.get(i),0.4f,0.4f,0.4f)));
				 
				System.out.println(""+SDs[i].getW()+" "+SDs[i].getId());
			}
			Arrays.sort(SDs,Collections.reverseOrder());
			int cpt=0;
			int nb=0;
			while(cpt < friendFromRepo.size())
			{
				if(!exclus.contains(friendFromRepo.get(SDs[cpt].getId()).getURL()))
				{
					System.out.println("exclusssss from sn :"+exclus.toString());
					System.out.println("elmmmeeent" +friendFromRepo.get(SDs[cpt].getId()).getURL());
				if (!socialNetwork.contains(friendFromRepo.get(SDs[cpt].getId())))
				{	
					socialNetwork.add(friendFromRepo.get(SDs[cpt].getId()));
					ls.add(friendFromRepo.get(SDs[cpt].getId()).getURL());
					System.out.println("nammme from SN construction"+friendFromRepo.get(SDs[cpt].getId()).getName());
				}
				 
		
				nb++;
				if(nb==size) break;
				}
				
				cpt++;
			}
			return ls;
		
		}
		public String getChosen()
		{
			if (socialNetwork.size()>0) return this.socialNetwork.get(0).getURL();
			else return "";
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
