package fr.insa.laas.Avatar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
 import java.util.Scanner;

public class QoSManager 
{
      
     double histd[];
	 double histt[];
     
     public  ArrayList<ClusterMemberQoS>  fillClusterData(int nbAvatars,int idCluster)
     {
    	 String result[]=new String[2];
    	 ArrayList<ClusterMemberQoS> ls=new ArrayList<ClusterMemberQoS>(); 
    	 ArrayList<Integer> used=new ArrayList<Integer>();
    	 for(int i=0;i<nbAvatars;i++)
    	 {
    		 //1.get data from dataset
    		try {
				result=getLine(new File("QoS_Data/tpMatrix.txt"),new File("QoS_Data/rt2Matrix.txt"),idCluster);
			//1. fill qos vector
				 
				String rest[]=result[0].split(" ");
				String resd[]=result[1].split(" ");
				int cpt=0;
				while (Double.valueOf(rest[cpt])==-1 || Double.valueOf(resd[cpt])==-1|| used.contains(cpt) )
				{
					cpt++;
					
				}
				used.add(cpt);
				double qos[]={Double.valueOf(rest[cpt]),Double.valueOf(resd[cpt])};
			//2. fill history
				getRandomHistory(new File("QoS_Data/tpMatrix.txt"),new File("QoS_Data/rt2Matrix.txt"),cpt);
				ClusterMemberQoS a=new ClusterMemberQoS(qos,histd,histt,null);
				ls.add(a);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		 
    		 
    	 }
    	 return ls;
    	 
     }
     public void getRandomHistory(File f, File f2, int index) throws FileNotFoundException
     {
    	 
    	 Scanner sc1 = new Scanner(f2);
    	 histd=new double[200];
    	 histt=new double[200];
    	 int cpt=0;
    	 int cpt2=0;
    	  for(Scanner sc = new Scanner(f); sc.hasNext(); )
          {
         	 
    		  String[] line = sc.nextLine().split(" ");
    		  String[] line2 = sc1.nextLine().split(" ");
    		  if(Double.valueOf(line[index])!=-1 && Double.valueOf(line2[index])!=-1)
    		  { 
    			  histt[cpt]=Double.valueOf(line[index]);
    			 
    			  cpt++;
    			 
    			  histd[cpt2]=Double.valueOf(line2[index]);
    			  
    			  cpt2++;
    			  
    		  }
    		
    		  
    		  if(cpt2==200) break;
            
             
          }
    	 
     }
     public String[] getLine(File f,File f2,int id) throws FileNotFoundException
     {
    	 String[] result=new String[2];
         int cpt=0;
         String line = null,line2 = null;
         Scanner sc1 = new Scanner(f2);
         for(Scanner sc = new Scanner(f); sc.hasNext(); )
         {
        	 
             line = sc.nextLine();
             line2 = sc1.nextLine();
          
           
            if(cpt == id)
            {
               result[0] = line; 
               result[1] = line2; 
                
               break;
            }
            cpt++;
         }
 
         return result;
     }
}
