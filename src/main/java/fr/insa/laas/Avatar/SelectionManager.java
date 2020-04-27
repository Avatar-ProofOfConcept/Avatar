package fr.insa.laas.Avatar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class SelectionManager {
	private int nbCluster;
	private int nbAvatars;
	private int d;
	private String srlevel;
	private String sru;
	private String srf;
	private double [][] cl;
	private double [][] qualityLevel;
	private double [][] utilities;
	private double [][] fluctuations;
	private float max;
	
	public float getMax() {
		return max;
	}
	public void setMax(float max) {
		this.max = max;
	}

	private Util u=new Util();
  	private HashMap<String, ArrayList<String>> selectionresult=new HashMap<String, ArrayList<String>>();

	public void sendSelectionRequest(CommunicationManagement cm, int nbAvatar, int nbCluster, int d)
	{
		//selection Test
		this.nbCluster=nbCluster;
		this.d=d;
	    cl=new double [2][nbCluster];
		qualityLevel=new double[d][2*nbCluster];
		utilities=new double[d][2*nbCluster];
		fluctuations=new double[d][2*nbCluster];
		ArrayList<String> ls =new ArrayList<String>();
		for(int i=0;i<nbAvatar;i++)
		{
			ls.add("3003");
		}
		for(int i=2;i<nbCluster+2;i++)
		{
		selectionresult.put("http://localhost:300"+i+"/", ls);
		}
		
		 
        cm.sendAsynTest(nbCluster,nbAvatar, d,this);
        launchSelection(cm);
      
	
	}
	public void launchSelection(CommunicationManagement cm)
	{
		toJuliaArray();
        executeSelectionSolver();
        try {
			cm.sendLocalConstraint(selectionresult.keySet(), cl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public int getNbCluster() {
		return nbCluster;
	}

	public void setNbCluster(int nbCluster) {
		this.nbCluster = nbCluster;
	}

	public int getNbAvatars() {
		return nbAvatars;
	}

	public void setNbAvatars(int nbAvatars) {
		this.nbAvatars = nbAvatars;
	}

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}

	public String getSrlevel() {
		return srlevel;
	}

	public void setSrlevel(String srlevel) {
		this.srlevel = srlevel;
	}

	public String getSru() {
		return sru;
	}

	public void setSru(String sru) {
		this.sru = sru;
	}

	public String getSrf() {
		return srf;
	}

	public void setSrf(String srf) {
		this.srf = srf;
	}

	public double[][] getQualityLevel() {
		return qualityLevel;
	}

	public void setQualityLevel(double[][] qualityLevel) {
		this.qualityLevel = qualityLevel;
	}

	public double[][] getUtilities() {
		return utilities;
	}

	public void setUtilities(double[][] utilities) {
		this.utilities = utilities;
	}

	public double[][] getFluctuations() {
		return fluctuations;
	}

	public void setFluctuations(double[][] fluctuations) {
		this.fluctuations = fluctuations;
	}

	public Util getU() {
		return u;
	}

	public void setU(Util u) {
		this.u = u;
	}

	public HashMap<String, ArrayList<String>> getSelectionresult() {
		return selectionresult;
	}

	public void setSelectionresult(
			HashMap<String, ArrayList<String>> selectionresult) {
		this.selectionresult = selectionresult;
	}

	public void getDataFromXML(String message,int idCluster)
	{
		String sr="";
		 
	 
		for(int i=0; i<2;i++)
		{
			 
			for(int j=0;j<d;j++)
			{
				
				sr=u.getXmlElement(message,"level"+i+j);
				qualityLevel[j][i*nbCluster+idCluster]=Double.parseDouble(sr.split("/")[0]);
				utilities[j][i*nbCluster+idCluster]=Double.parseDouble(sr.split("/")[1]);
				fluctuations[j][i*nbCluster+idCluster]=Double.parseDouble(sr.split("/")[2]);


			}
		}
 
	}
	public void toJuliaArray()
	{
		srlevel="";
		sru="";
		srf="";
		 
		 
		for (int i=0;i<d;i++)
		{
			for (int j=0;j<2*nbCluster;j++)
			{
				srlevel=srlevel+qualityLevel[i][j]+" ";
				sru=sru+utilities[i][j]+" ";
				srf=srf+fluctuations[i][j]+" ";
			}
			srlevel = srlevel.substring(0, srlevel.length() - 1);
			srlevel=srlevel+";";
			sru = sru.substring(0, sru.length() - 1);
			sru=sru+";";
			srf = srf.substring(0, srf.length() - 1);
			srf=srf+";";
		}
		
		 
		srlevel = srlevel.substring(0, srlevel.length() - 1);
	 
		sru = sru.substring(0, sru.length() - 1);
		 
		srf = srf.substring(0, srf.length() - 1);
		 
		
	}
	public void show()
	{
		for (int i=0;i<d;i++)
		{
			for (int j=0;j<2*nbCluster;j++)
			{
				System.out.println("quality"+qualityLevel[i][j]);
				System.out.println("utilities"+utilities[i][j]);
				System.out.println("fluctuation"+fluctuations[i][j]);
			}
		}
	}
	public void executeSelectionSolver()
	{
		 try {

	    		// -- Linux --
	    		
	    		// Run a shell command
	    		// Process process = Runtime.getRuntime().exec("ls /home/mkyong/");

	    		// Run a shell script
	    		// Process process = Runtime.getRuntime().exec("path/to/hello.sh");

	    		// -- Windows --
	    		
	    		// Run a command
	    		//Process process = Runtime.getRuntime().exec("cmd /c dir C:\\Users\\mkyong");

	    		//Run a bat file
	    		 
	    		String[] cmd = { "bash", "-c", "/home/amel/hello.jl \""+srlevel+"\" \""+sru+"\" \""+srf+"\"" };
	    		Process process = Runtime.getRuntime().exec(cmd);
	    		StringBuilder output = new StringBuilder();

	    		BufferedReader reader = new BufferedReader(
	    				new InputStreamReader(process.getInputStream()));

	    		String line;
	    		String linesave = null;
	    		 
	    		while ((line = reader.readLine()) != null) {
	    			//output.append(line + "\n");
	    			linesave=line;
	    			 
	    		}

	    		int exitVal = process.waitFor();
	    		if (exitVal == 0) {
	    			 
	    			//convert to 2d array
	    			System.out.println(linesave);
	    			line=linesave.substring(1,linesave.length()-1);
	    			//System.out.println(line);
	    			//System.out.println(line.split(";")[0]);
	    			String [] arr=line.split(";");
	    			 
	    			int j=0,k=0;
	    			float [][] cl=new float [arr.length][nbCluster*2];
	     			for(int i=0;i<arr.length;i++)
	    			{
	     				String[] arrc=arr[i].split(" ");
	    				
	    				while(j<arrc.length)
	    				{
	    					if(!arrc[j].isEmpty())
	    						{
	    						
	    						cl[i][k]=Float.valueOf(arrc[j]);
	    						System.out.print(cl[i][k] +" ");
	    						k++;
	    						}
	    					j++;
	    				}
	    				j=0;
	    				k=0;
	    				
	    				System.out.println();
	    			}
	     			getLocalConstraint(cl);
	    		} else {
	    			//abnormal...
	    		}

	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	} catch (InterruptedException e) {
	    		e.printStackTrace();
	    	}

	    

	}
	public void getLocalConstraint(float[][] in)
	{
		
		int k=0;
		
	for(int l=0;l<2;l++)
	{
		for(int i=0;i<d;i++)
		{
			k=0;
			for(int j=l*nbCluster;j<(l+1)*nbCluster;j++)
			{
				
				if(in[i][j]==1.0f) 
					{cl[l][k]=qualityLevel[i][j];System.out.println(cl[l][k]);k++;}
			}
		}
	}
		
 
	}

	public double[][] getCl()
	{
		return cl;
	}

	public void setCl(double[][] cl) {
		this.cl = cl;
	}

}
