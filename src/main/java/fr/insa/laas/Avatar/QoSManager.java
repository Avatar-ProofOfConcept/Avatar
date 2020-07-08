package fr.insa.laas.Avatar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
 import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

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
     
     public  ArrayList<ClusterMemberQoS>  fillUseCase(int j)
     {
    	 String result[]=new String[2];
    	 double tabtIME []={15.14 ,
    			 23.71, 
    			 36.28, 
    			 27.71 ,
    			 34, 
    			 49.14 ,
    			 52.57, 
    			 65.85, 
    			 48.71,
    			 51.28,
    			 57.85,
    			 52,
    			 56.14,
    			 55.28,
    			 51,
    			 53.14,
    			 22.14,
    			 52.57,
    			 47.57,
    			 53.71,
    			 57.71,
    			 55.85,
    			 28.85,
    			 60.42,
    			 29,
    			 49.28,
    			 44.14,
    			 55,
    			 39,
    			 46,
    			 42.28,
    			 49.14,
    			 45.28,
    			 51.42,
    			 37.42,
    			 55.71,
    			 54.85,
    			 70.57,
    			 44.14,
    			 47.71,
    			 52.71,
    			 49.57,
    			 39.85,
    			 52.28,
    			 38.42
                  };
    	 double tabTROU []={2.42,
    			 3.57,
    			 2.57,
    			 5.42,
    			 7.28,
    			 8,
    			 5.42,
    			 8.57,
    			 3.85,
    			 7.57,
    			 8.57,
    			 7.57,
    			 6.85,
    			 6.14,
    			 6.28,
    			 6,85,
    			 8.14,
    			 7.85,
    			 4.28,
    			 7.57,
    			 6.85,
    			 10,
    			 8.71,
    			 4.85,
    			 9.14,
    			 7.57,
    			 5.57,
    			 5.57,
    			 8.42,
    			 6.85,
    			 6.42,
    			 6,
    			 5.71,
    			 7,
    			 5.71,
    			 6.71,
    			 7.71,
    			 4.42,
    			 9.28,
    			 6.28,
    			 4.42,
    			 9.57,
    			 9.71,
    			 5.71,
    			 7};
    	 double histT[][]={{3, 5, 15, 6, 30, 2, 45},
    			           {8, 10, 7, 90, 2, 26, 23},
    	                   {7, 91, 76, 19, 5, 44, 12},
    	                   {23, 30, 7, 87, 11, 8, 28},
    	                   {12, 23, 9, 19, 7, 78, 90},
    	                   {58, 78, 31, 59, 28, 56, 34},
    	                   {84, 8, 78, 91, 19, 50, 38},
    	                   {56, 94, 48, 70, 81, 18, 94},
    	                   {56, 73, 3, 32, 60, 60, 57},
    	                   {63, 34, 79, 14, 74, 16, 79},
    	                   {76, 91, 72, 12, 59, 53, 42},
    	                   {78, 78, 32, 60, 64, 42, 10},
    	                   {36, 93, 82, 56, 16, 14, 96},
    	                   {74, 63, 33, 83, 15, 30, 89},
    	                   {4, 59, 47, 71, 61, 49, 66},
    	                   {80, 54, 14, 21, 31, 76, 96},
    	                   {14, 59, 15, 23, 30, 7, 7},
    	                   {79, 34, 90, 62, 88, 11, 4},
    	                   {23, 51, 12, 68, 56, 37, 86},
    	                   {66, 62, 39, 62, 85, 49, 13},
    	                   {55, 71, 47, 80, 83, 32, 36},
    	                   { 82, 46, 31, 87, 39, 35, 71},
    	                   {65, 74, 4, 18, 25, 9, 7 },
    	                   {78, 48, 48, 55, 92, 70, 32},
    	                   {13, 24, 30, 2, 25, 22, 87},
    	                   {81, 44, 94, 39, 27, 10, 50},
    	                   {68, 60, 59, 4, 72, 38, 8},
    	                   {58, 49, 48, 96, 52, 76, 6},
    	                   {93, 62, 76, 89, 87, 72, 4},
    	                   {22, 55, 18, 68, 6, 87, 66},
    	                   {12, 34, 48, 65, 20, 64, 53},
    	                   {8, 34, 65, 30, 58, 70, 79},
    	                   {33, 93, 24, 41, 94, 15, 17},
    	                   {34, 56, 78, 2, 65, 55, 70},
    	                   {61, 32, 53, 62, 27, 9, 18},
    	                   {49, 67, 97, 10, 63, 25, 79},
    	                   {17, 78, 17, 84, 56, 73, 59},
    	                   {61, 65, 77, 62, 70, 76, 83},
    	                   { 1, 24, 2, 82, 89, 44, 67},
    	                   { 42, 33, 91, 64, 11, 7, 86},
    	                   {44, 63, 91, 89, 4, 47, 31},
    	                   {15, 88, 63, 68, 38, 9, 66},
    	                   {55, 47, 73, 39, 9, 36, 20 },
    	                   {88, 38, 52, 41, 36, 23, 88 },
    	                   {63, 49, 26, 55, 23, 9, 44 }};
    	 double histD[][]={{1, 3, 0, 5, 4, 4, 0},
    			           {0, 0, 10, 4, 3, 3, 5},
    			           {4, 2, 0, 1, 1, 7, 3},
    			           {2, 2, 10, 13, 3, 7, 1},
    			           {11, 3, 5, 20, 9, 2, 1},
    			           {2, 6, 9, 0, 11, 14, 14},
    			           {2, 6, 0, 8, 5, 9, 8},
    			           {11, 13, 7, 7, 11, 11, 0},
    			           {14, 0, 2, 3, 2, 0, 6},
    			           {11, 14, 10, 6, 0, 9, 3},
    			           {13, 13, 7, 10, 3, 13, 1},
    			           {7, 12, 6, 0, 13, 7, 8},
    			           {11, 1, 13, 9, 10, 4, 0},
    			           { 4, 4, 8, 2, 13, 4, 8},
    			           {7, 6, 11, 5, 7, 5, 3},
    			           {4, 10, 9, 8, 3, 7, 7},
    			           {9, 7, 6, 12, 13, 7, 3},
    			           {11, 3, 14, 2, 10, 2, 13},
    			           {0, 6, 13, 1, 2, 3, 5},
    			           {13, 9, 0, 11, 14, 6, 0},
    			           {4, 7, 2, 1, 8, 13, 13},
    			           {9, 4, 14, 13, 9, 10, 11},
    			           {6, 6, 11, 13, 13, 5, 7},
    			           {11, 1, 0, 14, 0, 5, 3},
    			           {13, 7, 7, 10, 11, 5, 11},
    			           {14, 3, 13, 6, 1, 5, 11},
    			           {4, 11, 6, 2, 6, 10, 0},
    			           {10, 10, 5, 0, 1, 8, 5},
    			           {8, 1, 13, 10, 10, 9, 8},
    			           {8, 4, 11, 3, 5, 8, 9},
    			           { 5, 3, 8, 5, 5, 5, 14},
    			           {10, 2, 4, 0, 3, 13, 10},
    			           {3, 4, 13, 1, 5, 14, 0},
    			           {13, 5, 6, 3, 7, 6, 9},
    			           {9, 0, 0, 9, 10, 11, 1},
    			           {6, 8, 3, 10, 7, 13, 0},
    			           {8, 0, 11, 5, 9, 11, 10},
    			           {6, 2, 12, 2, 4, 1, 4},
    			           {12, 2, 13, 2, 14, 11, 11},
    			           {10, 5, 9, 8, 3, 7, 2},
    			           {4, 3, 7, 3, 1, 7, 6},
    			           {12, 8, 8, 9, 3, 14, 13},
    			           {14, 10, 14, 10, 2, 12, 6},
    			           {0, 3, 6, 3, 12, 9, 7},
    			           {5, 7, 13, 2, 3, 8, 11 }};
    	 
    	 ArrayList<ClusterMemberQoS> ls=new ArrayList<ClusterMemberQoS>(); 
    	 
    	 ArrayList<Integer> used=new ArrayList<Integer>();
    	  
    	 for(int i=0;i<9;i++)
    	 {
    		 //1.get data from dataset
    		 
    			//System.out.println("A"+j+" "+i);
				double qos[]={tabtIME[i+j*9],tabTROU[i+j*9]};
				 
		    	 
				//System.out.println(tabtIME[i+j*9]+" "+tabTROU[i+j*9]);
			//2. fill history
				//getRandomHistory(new File("QoS_Data/tpMatrix.txt"),new File("QoS_Data/rt2Matrix.txt"),i+j*9);
				ClusterMemberQoS a=new ClusterMemberQoS(qos,histD[i+j*9],histT[i+j*9],null);
				//showHist();
				//System.out.println("***************************");
				ls.add(a);
				
			 
    		 
    		 
    	 }
     
    	 return ls;
    	 
     }
     public void getRandomHistory(File f, File f2, int index) throws FileNotFoundException
     {
    	 
    	 Scanner sc1 = new Scanner(f2);
    	 histd=new double[20];
    	 histt=new double[20];
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
    		
    		  
    		  if(cpt2==20) break;
            
             
          }
    	 
     }
     public void showHist()
     {
    	 for (int i=0;i< histd.length;i++)
    	 {
    		 System.out.println(histt[i]+" "+histd[i]);
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
     
     
     public ClusterQoS[] fillCluster(int nba,int nbc)
     {
    	 ThreadLocalRandom random = ThreadLocalRandom.current();
    	 double [] w={0.6,0.4};
    	 ClusterQoS[] result=new ClusterQoS[nbc];
    	
    	 for (int l=0; l<nbc;l++)
    	 {
    		 ArrayList<ClusterMemberQoS> ls=new ArrayList<ClusterMemberQoS>(); 
    		// System.out.println("Cluster ");
         	  
        	 for(int i=0;i<nba;i++)
        	 {
        		// System.out.println("avatar "+i);
        		 histd=new double[20];
            	 histt=new double[20];
            	 //System.out.println("historique ");
            	 for(int k=0;k<20;k++)
            	 {
            		 histt[k]=random.nextInt(0,100);
            		 histd[k]=random.nextInt(0,30);
            		 
            	 }
            	// showHist();
        		 //1.get data from dataset
        		 
        			//System.out.println("A"+j+" "+i);
    				double qos[]={average(histt),average(histd)};
    				// System.out.println(qos[0]+"  "+qos[1]);
    		    	 
    				//System.out.println(tabtIME[i+j*9]+" "+tabTROU[i+j*9]);
    			//2. fill history
    				//getRandomHistory(new File("QoS_Data/tpMatrix.txt"),new File("QoS_Data/rt2Matrix.txt"),i+j*9);
    				ClusterMemberQoS a=new ClusterMemberQoS(qos,histd,histt,null);
    				 
    				ls.add(a);
    				
    			 
        		 
        		 
        	 
        	 
    	 }
	     
         result[l]=new ClusterQoS(ls, w);
    	 }
    	 return result;
    	 
    	 
    	 
     }
     
     public double average(double tab [])
     {
    	 double sum=0;
    	 for(int i=0;i<tab.length;i++)
    	 {
    		 sum=sum+tab[i];
    	 }
    	 return (sum/tab.length);
    	 
    	 
     }
}
