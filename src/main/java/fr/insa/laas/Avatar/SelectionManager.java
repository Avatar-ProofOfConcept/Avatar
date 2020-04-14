package fr.insa.laas.Avatar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class SelectionManager {
	private int nbCluster=2;
	private int nbAvatars;
	private int d=3;
	private String srlevel;
	private String sru;
	private String srf;
	
	private double [][] qualityLevel=new double[d][4*nbCluster];
	private double [][] utilities=new double[d][4*nbCluster];
	private double [][] fluctuations=new double[d][4*nbCluster];
	
	private Util u=new Util();
  	private HashMap<String, ArrayList<String>> selectionresult=new HashMap<String, ArrayList<String>>();

	public void sendSelectionRequest(CommunicationManagement cm)
	{
		//selection Test
		ArrayList<String> ls =new ArrayList<String>();
		ls.add("3003");ls.add("3004");
		selectionresult.put("http://localhost:3002/", ls);
		selectionresult.put("http://localhost:3003/", ls);
		String mssge="";
        Iterator hmIterator = selectionresult.entrySet().iterator();
        int k=0;
        while (hmIterator.hasNext()) { 
            Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
            try {
				mssge=cm.sendMembersSelection((String)mapElement.getKey(),(ArrayList<String>)mapElement.getValue(),d);
				getDataFromXML(mssge,k);k++;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } 
		 
        toJuliaArray(mssge);
        executeSelectionSolver();
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
	 
		for(int i=0; i<4;i++)
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
	public void toJuliaArray(String message)
	{
		srlevel="";
		sru="";
		srf="";
		 
		 
		for (int i=0;i<d;i++)
		{
			for (int j=0;j<4*nbCluster;j++)
			{
				srlevel=srlevel+qualityLevel[i][j]+" ";
				sru=sru+utilities[i][j]+" ";
				srf=srf+fluctuations[i][j]+" ";
			}
			srlevel=srlevel+";";
			sru=sru+";";
			srf=srf+";";
		}
		
		System.out.println("julia tabs");
		srlevel = srlevel.substring(0, srlevel.length() - 1);
		System.out.println("l"+srlevel);
		sru = sru.substring(0, sru.length() - 1);
		System.out.println("u"+sru);
		srf = srf.substring(0, srf.length() - 1);
		System.out.println("f"+srf);
		
	}
	public void show()
	{
		for (int i=0;i<d;i++)
		{
			for (int j=0;j<4*nbCluster;j++)
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
	    		while ((line = reader.readLine()) != null) {
	    			output.append(line + "\n");
	    		}

	    		int exitVal = process.waitFor();
	    		if (exitVal == 0) {
	    			System.out.println("Success!");
	    			System.out.println(output);
	    			//System.exit(0);
	    		} else {
	    			//abnormal...
	    		}

	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	} catch (InterruptedException e) {
	    		e.printStackTrace();
	    	}

	    

	}

}
