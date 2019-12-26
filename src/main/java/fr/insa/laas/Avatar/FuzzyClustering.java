package fr.insa.laas.Avatar;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class FuzzyClustering {
    public ArrayList<ArrayList<Float>> data;
    public ArrayList<ArrayList<Float>> clusterCenters;
    private float u[][];
    private float u_pre[][];
    private int clusterCount;
    private int iteration;
    private int dimension;
    private int fuzziness;
    private int p;
    private double epsilon;
    public double finalError;
    private ArrayList<Element[]> avatars;

    public FuzzyClustering(){
        data = new ArrayList<>();
        clusterCenters = new ArrayList<>();
        avatars=new ArrayList();
        fuzziness = 2;
        epsilon = 0.01;
        p=3;
    }
   public int getClusterNumber()
   {
	   return clusterCount;
	   
   } 
   public void setdimension(int d)
   {
	   this.dimension=d;
   }public ArrayList<Element[]> getAvatarsList()
   {
	   return this.avatars;
   }
    public void run(int clusterNumber, int iter){
        this.clusterCount = clusterNumber;
        this.iteration = iter;
        //this.data = data;

        //start algorithm
        //1 assign initial membership values
        assignInitialMembership();
        showU ();


        for (int i = 0; i < iteration; i++) {
            //2 calculate cluster centers
            calculateClusterCenters();

            //3
            updateMembershipValues();

            //4
            finalError = checkConvergence();
            if(finalError <= epsilon)
                break;
        }
        getAvatarCluster(p);
        showAvatars(p);
    }
    public void initData (int numberOfData,int dimension)
    {
        this.dimension=dimension;
    	for(int i=0;i<numberOfData;i++)
    	{
            ArrayList<Float> tmp = new ArrayList<>();

    		for(int k=0;k<dimension;k++)
    		{
    			tmp.add((float) Math.random());
    			System.out.print(tmp.get(k)+" ");

    		}
    		System.out.println();
    		this.data.add(tmp);
    	}
    	
    }
    public void showU ()
    {
     for (int i=0;i<data.size();i++)
     {
    	 for (int k=0;k<clusterCount;k++)
    	 {
    		 System.out.print(data.get(i).get(k)+"  ");
    	 }
    	 System.out.println();
     }
    }
    /**
     * in this function we generate random data with specific option
     * @param numberOfData
     * @param dimension
     * @param minRange
     * @param maxRange
     */
    public void createRandomData(int numberOfData, int dimension, int minRange, int maxRange, int clusterCount){
        this.dimension = dimension;
        ArrayList<ArrayList<Integer>> centroids = new ArrayList<>();
        centroids.add(new ArrayList<Integer>());
        int[] numberOfDataInEachArea = new int[clusterCount];
        int range = maxRange - minRange + 1;
        int step = range / (clusterCount + 1);
        for (int i = 1; i <= clusterCount; i++) {
            centroids.get(0).add(minRange + i * step);
        }

        for (int i = 0; i < dimension - 1; i++) {
            centroids.add((ArrayList<Integer>) centroids.get(0).clone());
        }
        double variance = (centroids.get(0).get(1) - centroids.get(0).get(0))/ 2.5;
        for (int i = 0; i < dimension; i++) {
            Collections.shuffle(centroids.get(i));
        }
        Random r = new Random();
        int sum = 0;
        for (int i = 0; i < clusterCount; i++) {
            int rg = r.nextInt(50) + 10;
            numberOfDataInEachArea[i] = (rg);
            sum += rg;
        }
        for (int i = 0; i < clusterCount; i++)
            numberOfDataInEachArea[i] = (int)((((double)numberOfDataInEachArea[i]) / sum) * numberOfData);

        Random fRandom = new Random();
        for (int i = 0; i < clusterCount; i++) {
            for (int j = 0; j < numberOfDataInEachArea[i]; j++) {
                ArrayList<Float> tmp = new ArrayList<>();
                for (int k = 0; k < dimension; k++) {
                    tmp.add((float)(centroids.get(k).get(i) + fRandom.nextGaussian() * variance));
                }
                data.add(tmp);
            }
        }
    }

    /**
     * this function generate membership value for each data
     */
    private void assignInitialMembership(){
    	System.out.println(data.size());
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
    		
    		avatars.add(tmpP);
    	}
    	
    }
    public void showAvatars(int p)
    {
    	for (int i=0;i<clusterCount;i++)
    	{
    		Element [] tmp=avatars.get(i);
    		for (int j=0;j<p;j++)
    		{
    			System.out.print (" id= "+tmp[j].getId());
    			System.out .print(" W="+tmp[j].getW());
    			
    		}
    		System.out.println();
    		 
    	}
    	
    }

    /**
     * in this function we will update membership value
     */
    private void updateMembershipValues(){
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < clusterCount; j++) {
                u_pre[i][j] = u[i][j];
                float sum = 0;
                float upper = Distance(data.get(i), clusterCenters.get(j));
                for (int k = 0; k < clusterCount; k++) {
                    float lower = Distance(data.get(i), clusterCenters.get(k));
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
    private float Distance(ArrayList<Float> p1, ArrayList<Float> p2){
        float sum = 0;
        for (int i = 0; i < p1.size(); i++) {
            sum += Math.pow(p1.get(i) - p2.get(i), 2);
        }
        sum = (float) Math.sqrt(sum);
        return sum;
    }

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

    /**
     * write random generated data to file for visualizing
     * @throws IOException
     */
    public void writeDataToFile(ArrayList<ArrayList<Float>> inpData, String fileName) throws IOException {

        FileWriter fileWriter = new FileWriter("./" + fileName + ".csv");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (int i = 0; i < inpData.size(); i++) {
             String res = "";
            for (int j = 0; j < inpData.get(i).size(); j++) {
                if(j == inpData.get(i).size() - 1)
                    res += inpData.get(i).get(j);
                else
                    res += inpData.get(i).get(j) +",";
            }
            printWriter.println(res);
        }
        printWriter.close();
    }
	public ArrayList<String> getClusterMembers(int i,ArrayList<MetaAvatar> ls) {
		Element [] tmp=avatars.get(i);
		ArrayList<String> tmpls=new ArrayList<String>();
		for (int j=0;j<p;j++)
		{
			tmpls.add(ls.get(tmp[j].getId()).getURL());
			
		}
 		return tmpls;
	}

}

