package fr.insa.laas.Avatar;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
 

  public class FuzzyClustering {
    public ArrayList<ArrayList<Integer>> data;
    public ArrayList<ArrayList<Float>> clusterCenters;
    private float u[][];
    private float u_pre[][];
    private int clusterCount;
    private int dimension;
    private float fuzziness;
    private double epsilon;
    public double finalError;
    private ArrayList<Cluster> avatars;
	private int idDistance;
	
	 
    public FuzzyClustering(){
        data = new ArrayList<>();
        clusterCenters = new ArrayList<>();
        avatars=new ArrayList<Cluster>();
        fuzziness = 2;
        epsilon = 0.2;
    }
    public void setDimension(int d)
    {
    	this.dimension=d;
    }
    public int getClusterNumber()
    {
    	return this.clusterCount;
    }
    public ArrayList<Cluster> getAvatarsList()
    {
    	return this.avatars;
    }
    public ArrayList<String> getClusterMembers(int i,ArrayList<MetaAvatar> ls) {
		Element [] tmp=avatars.get(i).getElements();
		ArrayList<String> tmpls=new ArrayList<String>();
		for (int j=1;j<tmp.length;j++)
		{
			tmpls.add(ls.get(tmp[j].getId()).getURL());
			
		}
 		return tmpls;
	}
    public void run(int clusterNumber, ArrayList<ArrayList<Integer>> data,int idD,float m,double e,int p){
        this.clusterCount = clusterNumber;
        this.fuzziness=m;
        this.idDistance=idD;
        this.data = data;
        this.epsilon=e;
        this.dimension=clusterNumber;
        showData();
        float lStartTime = System.nanoTime();
        assignInitialMembership();
      
     
        while(true) {
        	 
        	 
        	 calculateClusterCenters();
             updateMembershipValues();
            finalError = checkConvergence();
            if(finalError <= epsilon)
                break;
        }
      //end
        float lEndTime = System.nanoTime();

		//time elapsed
       float output = lEndTime - lStartTime;

        System.out.println("Clustering time in milliseconds: " + output / 1000000f);
      /*  System.out.println("BPC  "+ValidationIndices.bezdekPartitionCoefficient(u));
        System.out.println("XB  "+ValidationIndices.compactnessAndSeparationMetric(data, u, clusterCenters, fuzziness));
        System.out.println("PE    "+ValidationIndices.partitionEntropyIndex(u));
        System.out.println("ITER    "+iter);*/
        //showU ();
        getAvatarCluster(p);
        frequency();
        //showAvatars();
        
    }
    public void showData ()
    {
        
        System.out.println("Data matrix");

    	for(int i=0;i<this.data.size();i++)
    	{
           
    		ArrayList<Integer> tmp = new ArrayList<>();
    		tmp=data.get(i);
 
            
    		for(int k=0;k<this.clusterCount;k++)
    		{
    			//tmp.add(random.nextInt(2));
    			System.out.print(tmp.get(k)+" ");

    		}
    		System.out.println();
     	}
    	
    }
    public void assignInitialcenters()
    {
    	  u = new float[data.size()][clusterCount];
          u_pre = new float[data.size()][clusterCount];
     	for(int i=0;i<this.dimension;i++)
    	{
            ArrayList<Float> tmp = new ArrayList<>();
            Random random = new Random();

            
    		for(int k=0;k<dimension;k++)
    		{
    			tmp.add((float)random.nextInt(2));
    			System.out.print(tmp.get(k)+" ");

    		}
    		System.out.println();
    		this.clusterCenters.add(tmp);
    	}
     	
       
    	
    }
    public void showU ()
    {
     for (int i=0;i<data.size();i++)
     {
    	 for (int k=0;k<clusterCount;k++)
    	 {
    		 System.out.print(u[i][k]+"  ");
    	 }
    	 System.out.println();
     }
    }
    public void showC ()
    {
    	System.out.println(clusterCenters.size()+"  "+clusterCenters.get(0).size());
     for (int i=0;i<clusterCount;i++)
     {
    	 for (int k=0;k<clusterCount;k++)
    	 {
    		 System.out.print(clusterCenters.get(i).get(k)+"  ");
    	 }
    	 System.out.println();
     }
    }
  

    /**
     * this function generate membership value for each data
     */
    private void assignInitialMembership(){
    	//System.out.println(data.size());
        u = new float[data.size()][clusterCount];
        u_pre = new float[data.size()][clusterCount];
        Random r = new Random();
        for (int i = 0; i < data.size(); i++) {
            float sum = 0;
            for (int j = 0; j < clusterCount; j++) {
                u[i][j] = r.nextFloat() * 10 + 1;
                sum += u[i][j];
            }
            for (int j = 0; j < clusterCount; j++) {
                u[i][j] = u[i][j] / sum;
            }
        }
    }

    /**
     * in this function we calculate value of each cluster
     */
    private void calculateClusterCenters(){
    	clusterCenters.clear();
        for (int i = 0; i < clusterCount; i++) {
            ArrayList<Float> tmp = new ArrayList<>();
            for (int j = 0; j < dimension; j++) {
                float cluster_ij;
                float sum1 = 0;
                float sum2 = 0;
                for (int k = 0; k < data.size(); k++) {
                    double tt = Math.pow(u[k][i], fuzziness);
                    sum1 += tt * data.get(k).get(j);
                    sum2 += tt;
                }
                cluster_ij = sum1/sum2;
                tmp.add(cluster_ij);
            }
            clusterCenters.add(tmp);
        }
    }
    
    public void getAvatarCluster (int p)
    {
    	Cluster c=new Cluster();
    	for (int i=0;i<clusterCount;i++)
    	{
    		Element [] tmp=new Element[data.size()];
    		Element [] tmpP=new Element[p];
    		for (int j=0;j<data.size();j++)
    		{
    			tmp[j]=new Element(j,u[j][i]);
    			 
    			
    		}
    		Arrays.sort(tmp,Collections.reverseOrder());
    		for(int k=0;k<p;k++)
    		{
    			tmpP[k]=new Element(tmp[k].getId(),tmp[k].getW());

    		}
    		c=new Cluster();
    		c.setElements(tmpP);
    		avatars.add(c);
    	}
    	
    }
    public void showAvatars()
    {
        

    	for (int i=0;i<clusterCount;i++)
    	{
    		System.out.println(" feature ="+avatars.get(i).getFeature());
    		Element [] tmp=avatars.get(i).getElements();
    		for (int j=0;j<tmp.length;j++)
    		{
    		 
    			System.out.print (" id= "+tmp[j].getId());
    			System.out .print(" W="+tmp[j].getW());
    			
    		}
    		 
    		System.out.println();
    		 
    	}
    	
    }
    public void frequency()
    {
        int frequency[][]=new int[clusterCount][clusterCount];
       // System.out.println(" start frequency");
    	for (int i=0;i<clusterCount;i++)
    	{ 
    		for (int k=0;k<clusterCount;k++)
    		{
    			//System.out.println(" start frequency b2");
    			frequency[i][k]=0;
    		Element [] tmp=avatars.get(i).getElements();
    		for (int j=0;j<tmp.length;j++)
    		{
    			//System.out.println(" start frequency b3");
    			if(data.get(tmp[j].getId()).get(k)==1) frequency[i][k]++;
    			
    		}
    		//System.out.print(frequency[i][k]+" ");
    		}
    		//System.out.println();
    		 
    	}
    	ArrayList<Integer> exclus=new ArrayList<Integer>();
    	int indice=0;
    	for (int i=0;i<clusterCount;i++)
    	{
    		indice =maxTab(frequency[i],exclus);
    		avatars.get(i).setFeature(indice);
    		
    		
    	}
    	 
    	 
    	
    }
    public int maxTab (int tab [],ArrayList<Integer> exclus)
    {
    	int results =0;
        int max=tab[0];
        boolean ok=false;
        int k=0;
        while(!ok)
        {
        	 
        	if(!exclus.contains(k)) {ok=true;max=tab[k];results=k;}
        	else k++;
        }
        
     	for(int i=0;i<tab.length;i++)
    	{
    		if (max < tab[i] && !exclus.contains(i))
    			{ 
    			  max=tab[i];
    			  results=i;
    			}
    	}
    	exclus.add(results);
     	//System.out.println(" exclus = "+exclus.toString());


    	return results;
    }

    /**
     * in this function we will update membership value
     */
    private void updateMembershipValues(){
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < clusterCount; j++) {
                u_pre[i][j] = u[i][j];
                float sum = 0;
                float upper = Distances.select(idDistance,data.get(i), clusterCenters.get(j));
                for (int k = 0; k < clusterCount; k++) {
                    float lower = Distances.select(idDistance,data.get(i), clusterCenters.get(k));
                    sum += Math.pow((upper/lower), 2/(fuzziness -1));
                }
                u[i][j] = 1/sum;
            }
        }
    }

    /**
     * get norm 2 of two point
     * @param p1
     * @param p2
     * @return
     */
   
    /**
     * we calculate norm 2 of ||U - U_pre||
     * @return
     */
    private double checkConvergence(){
        double sum = 0;
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < clusterCount; j++) {
                sum += Math.pow(u[i][j] - u_pre[i][j], 2);
            }
        }
        return Math.sqrt(sum);
    }



}
