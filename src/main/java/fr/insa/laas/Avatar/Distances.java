package fr.insa.laas.Avatar;
import java.util.ArrayList;


public class Distances {
	
	 public static float select (int id,ArrayList<Integer> p1, ArrayList<Float> p2)
	 {
		 switch(id)
		 {
		 case 0:
			 return DistanceEuclidien(p1,p2);
		  case 1:
			 return DistanceEuclidienStandard(p1,p2);
		  case 2 :
			  return distanceManhattan(p1, p2);
		  case 3 :
			  return distanceCanberra(p1, p2);
		  default :
			  return 0f;
		  
			 
		 }
	 }
	 public static float DistanceEuclidien(ArrayList<Integer> p1, ArrayList<Float> p2){
	        float sum = 0;
	        for (int i = 0; i < p1.size(); i++) {
	            sum += Math.pow(p1.get(i) - p2.get(i), 2);
	        }
	       sum = (float) Math.sqrt(sum);
	        return sum;
	    }
	 public static float DistanceEuclidien2(ArrayList<Float> p1, ArrayList<Float> p2){
	        float sum = 0;
	        for (int i = 0; i < p1.size(); i++) {
	            sum += Math.pow(p1.get(i) - p2.get(i), 2);
	        }
	       sum = (float) Math.sqrt(sum);
	        return sum;
	    }
	    public static  float DistanceJACCARD(ArrayList<Integer> a, ArrayList<Integer> b){
	    	  
	    	      
	    			int p=0, q=0,r=0;
	    	        for (int i = 0; i < b.size(); i++)
	    	        {
	    	             
	    	        	 if (a.get(i)==b.get(i) && a.get(i)==1) p++;
	    	        	 else 
	    	        	 {
	    	        		 if (a.get(i)==1 && b.get(i)==0) q++;
	    	        		 else
	    	        		 {
	    	        			 if(a.get(i)==0 && b.get(i)==1) r++;
	    	        		 }
	    	        	 }
	    	        }
	    	            

	    	        if (p+r+q ==0) return 0.00005f;
	    	        else return (float) p / (p+r+q);
	    	    
	    }
	    public static  float DistanceEuclidienStandard(ArrayList<Integer> p1, ArrayList<Float> p2){
	        float sum = 0;
	        for (int i = 0; i < p1.size(); i++) {
	            sum += Math.pow(p1.get(i) - p2.get(i), 2);
	        }
	       
	        return sum;
	    }
	    public static float distanceCosine(ArrayList<Integer> data, ArrayList<Float> centroid) {
	        
	    	float cos = 0.0f;
	    	float normdata = 0.0f;
	    	float normcentroid = 0.0f;        
	    	float dotprod = 0.0f;     
	        
	        for(int i = 0; i < data.size(); i++) {
	            dotprod += data.get(i) * centroid.get(i);
	            normdata += data.get(i) * data.get(i);
	            normcentroid += centroid.get(i) * centroid.get(i);
	        }        
	        
	        normdata = (float) Math.sqrt(normdata);
	        normcentroid = (float) Math.sqrt(normcentroid);
	        
	        cos = dotprod / (normdata * normcentroid);
	        
	        return 1 - cos;
	        
	    }
	    public static float distanceManhattan(ArrayList<Integer> a, ArrayList<Float> b)
	    {
	    	float s=0f;
	    	for(int i=0;i< a.size();i++)
	    	{
	    		s=s+ Math.abs(a.get(i)-b.get(i));
	    	}
	    	return s;
	    }
	    public static float distanceCanberra (ArrayList<Integer> a, ArrayList<Float> b)
	    {
	    	float sum=0f;
	    	for(int i=0;i< a.size();i++)
	    	{
	    	 if (a.get(i) != 0 || b.get(i) != 0) {
	                sum += (Math.abs((float) (a.get(i) - b.get(i)))
	                        / (Math.abs(a.get(i)) + Math.abs(
	                        		b.get(i))));
	            }
	    	}
	    	return sum;
	    }

}
