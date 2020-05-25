package fr.insa.laas.Avatar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jmetal.CMOEAs.Utils;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.encodings.variable.ArrayInt;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.Exemple;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.FileUtils;
import jmetal.util.NonDominatedSolutionList;



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
	///solver
	private double [][] qualityLevelS;
	private double [][] utilitiesS;
	private double [][] fluctuationsS;
	private float max;
	private SolutionSet sl= new NonDominatedSolutionList();
	
	public float getMax() {
		return max;
	}
	public void setMax(float max) {
		this.max = max;
	}

	private Util u=new Util();
  	private HashMap<String, ArrayList<String>> selectionresult=new HashMap<String, ArrayList<String>>();
  	private ArrayList<String> list;

	public void sendSelectionRequest(CommunicationManagement cm, int nbAvatar, int nbCluster, int d)
	{
		//selection Test
		this.nbCluster=nbCluster;
		this.d=d;
	    cl=new double [2][nbCluster];
		qualityLevel=new double[d][2*nbCluster];
		utilities=new double[d][2*nbCluster];
		fluctuations=new double[d][2*nbCluster];
		
		qualityLevelS=new double[d][2*nbCluster];
		utilitiesS=new double[d][2*nbCluster];
		fluctuationsS=new double[d][2*nbCluster];
		list=new ArrayList<String>();
		ArrayList<String> ls =new ArrayList<String>();
		for(int i=0;i<nbAvatar;i++)
		{
			ls.add("3003");
		}
		for(int i=2;i<nbCluster+2;i++)
		{
		selectionresult.put("http://localhost:300"+i+"/", ls);
		list.add("http://localhost:300"+i+"/");
		}
		
		 
        cm.sendAsynTest(nbCluster,nbAvatar, d,this);
        launchSelection(cm);
      
	
	}
	public void centralizeSelection(int nbc,int nba,int d)
	{
		this.nbCluster=nbc;
		this.d=d;
	    cl=new double [2][nbCluster];
		qualityLevel=new double[d][2*nbCluster];
		utilities=new double[d][2*nbCluster];
		fluctuations=new double[d][2*nbCluster];
		ClusterQoS c[]=new ClusterQoS[nbc];
		double [] w={0.4,0.3};
		
	 
			c[0]=new ClusterQoS(new QoSManager().fillClusterData(nba, 0), w);
			 
	 
		 System.out.println("fin data fill");
		long startTime = System.nanoTime();
		 
 			c[0].getQualitLevel(d);
		 
		/*getDataFromTab(c);
		toJuliaArray();
        executeSelectionSolver();
        for (int i=0;i<nbc;i++)
		{
        	double []t={cl[0][i],cl[1][i]};
			System.out.println("optim c"+i+" "+c[i].getSelectedAvatars(t));
			
		}*/
        long elapsedTime = System.nanoTime() - startTime;
		System.out.println("selectionTime "+elapsedTime/1000000f);
	}
	public void launchSelection(CommunicationManagement cm)
	{
		toJuliaArray();
		executeSelectionSolver();
	
       try {
			cm.sendLocalConstraint(list, cl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	cl=new double [2][nbCluster];
   	Double opt=cm.opt;
   	cm.opt=0;
    executeGenetic(100000, 0.0001);
    try {
		cm.sendLocalConstraint(list, cl);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    System.out.println("optimalite exacte = "+opt);
    System.out.println("optimalite genetic = "+cm.opt);
    System.out.println("optimalite = "+cm.opt/opt);
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
				qualityLevelS[j][i*nbCluster+idCluster]=Double.parseDouble(sr.split("/")[0]);
				utilitiesS[j][i*nbCluster+idCluster]=Double.parseDouble(sr.split("/")[1]);
				fluctuationsS[j][i*nbCluster+idCluster]=Double.parseDouble(sr.split("/")[2]);


			}
		}
 
	}
	public void getDataFromXMLGenetic(String message,int idCluster)
	{
		String sr="";
		 
	 
		for(int i=0; i<2;i++)
		{
			 
			for(int j=0;j<d;j++)
			{
				
				sr=u.getXmlElement(message,"level"+i+j);
				qualityLevel[j][2*idCluster+i]=Double.parseDouble(sr.split("/")[0]);
				utilities[j][2*idCluster+i]=Double.parseDouble(sr.split("/")[1]);
				fluctuations[j][2*idCluster+i]=Double.parseDouble(sr.split("/")[2]);


			}
		}
 
	}
	public void getDataFromTab(ClusterQoS []c)
	{
	 
		double []tab=new double[d];
		 
	    for(int k=0;k<c.length;k++)
	    {
		for(int i=0; i<2;i++)
		{
			tab=c[k].getLevelTab(i);
			for(int j=0;j<d;j++)
			{
				
				 
				qualityLevel[j][i*nbCluster+k]=tab[j];
				utilities[j][i*nbCluster+k]=new QualityLevel(c[k],i,tab[j]).p();
				fluctuations[j][i*nbCluster+k]=new QualityLevel(c[k],i,tab[j]).f();


			}
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
			  
			  
	    		String[] cmd = { "bash", "-c", "./hello.jl \""+srlevel+"\" \""+sru+"\" \""+srf+"\"" };
	    		long initTime = System.currentTimeMillis();
               
	    		Process process = Runtime.getRuntime().exec(cmd);
	    		  
	       
 	    		 
	    		BufferedReader reader = new BufferedReader(
	    				new InputStreamReader(process.getInputStream()));

	    		String line;
	    		String linesave = null;
	    		ArrayList<String> tab=new ArrayList<String>();
	    		while ((line = reader.readLine()) != null) {
	    			//output.append(line + "\n");
	    			tab.add(line);
	    			//System.out.println("line "+line);
	    			 
	    		}
	    		
	    		 
	    		int exitVal = process.waitFor();
	    		if (exitVal == 0) {
	    			/*Solution s;
	    			
	    			for(int i=0;i<tab.size();i++)
	    			{
	 	    			line=tab.get(i).substring(1,tab.get(i).length()-1);
	 	    			s=new Solution(2);
	 	    			s.setObjective(0, Double.valueOf(line.split(", ")[0]));
	 	    			s.setObjective(1, Double.valueOf(line.split(", ")[1]));
	 	    			//sl.add(s);
 	 	    		
	    				
	    			}*/
 	    			//convert to 2d array
 	    			linesave=tab.get(tab.size()/2);
 	    			line=linesave.substring(1,linesave.length()-1);
 	    			System.out.println(line.split(";")[0]);
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
	     			 long estimatedTime = System.currentTimeMillis() - initTime;
	 	            
	 	            System.out.println("exact execution time"+estimatedTime);
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
 
		for(int i=0;i<d;i++)
		{
			 
			for(int j=0;j<nbCluster;j++)
			{
				
				if(in[i][j]==1.0f) 
					{cl[0][j]=qualityLevel[i][j];System.out.println(cl[0][j]);}
			}
		}
		
		for(int i=0;i<d;i++)
		{
			 
			for(int j=nbCluster;j<(2)*nbCluster;j++)
			{
				
				if(in[i][j]==1.0f) 
					{cl[1][j-nbCluster]=qualityLevel[i][j];System.out.println(cl[1][j-nbCluster]);}
			}
		}
     
     	showcl();
		
 
	}
	public void showcl()
	{
		for(int i=0;i<2;i++)
		{
			for(int j=0;j<2;j++) System.out.print(cl[i][j]+" ");
			System.out.println();
				
		}
	}

	public double[][] getCl()
	{
		return cl;
	}

	public void setCl(double[][] cl) {
		this.cl = cl;
	}
	public void executeGenetic(double ctm , double ctr)
	{
		int crossoverMethod = 1;  // 0 represents for DE, 1 represents for SBX
        //batchRun(new String[]{"MOEAD_IEpsilon","MOEAD_Epsilon","MOEAD_SR","MOEAD_CDP","C_MOEAD"},crossoverMethod);

        try {
 			singleRun("MOEAD_IEpsilon",crossoverMethod, ctm, ctr); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	    private  void singleRun(String algorithmName, int crossMethod,double ctm , double ctr) throws Exception {
	        Problem problem;                // The problem to solve
	        Algorithm algorithm;            // The algorithm to use
	        Operator crossover;            // Crossover operator
	        Operator mutation;             // Mutation operator
	        Operator selection;            // Selection operator
	        HashMap parameters;           // Operator parameters

	/////////////////////////////////////////// parameter setting //////////////////////////////////

	        int popSize = 300;
	        int neighborSize = (int) (0.1 * popSize);
	        int maxFES = 3000;
	        int updateNumber = 2;
	        double deDelta = 0.9;
	        double DeCrossRate = 1.0;
	        double DeFactor = 0.5;

	        double tao = 0.1;
	        double alpha = 0.9;

	        String AlgorithmName = algorithmName;


	        String mainPath = System.getProperty("user.dir");
	        String weightPath = mainPath + "/weight";
	        int runtime = 1;
	        Boolean isDisplay = false;
	        int plotFlag = 0; // 0 for the working population; 1 for the external archive


	        // MOEAD_SR parameters
	        double srFactor = 0.05;


  



	//////////////////////////////////////// End parameter setting //////////////////////////////////
	        
	    
	            problem = new Exemple(nbCluster,ctm,ctr,qualityLevel, utilities,  fluctuations,d);
	            Object[] algorithmParams = {problem};
	            algorithm = (new Utils()).getAlgorithm(AlgorithmName, algorithmParams);

	            //define pareto file path
	            String paretoPath = mainPath + "/pf_data/Exemple.pf";
	            // Algorithm parameters
	            algorithm.setInputParameter("AlgorithmName", AlgorithmName);
	            algorithm.setInputParameter("populationSize", popSize);
	            algorithm.setInputParameter("maxEvaluations", maxFES);
	            algorithm.setInputParameter("dataDirectory", weightPath);
	            algorithm.setInputParameter("T", neighborSize);
	            algorithm.setInputParameter("delta", deDelta);
	            algorithm.setInputParameter("nr", updateNumber);
	            algorithm.setInputParameter("isDisplay", isDisplay);
	            algorithm.setInputParameter("plotFlag", plotFlag);
	            algorithm.setInputParameter("paretoPath", paretoPath);
	            algorithm.setInputParameter("srFactor", srFactor);
	            algorithm.setInputParameter("tao", tao);
	            algorithm.setInputParameter("alpha", alpha);
	            algorithm.setInputParameter("pr", 0.8);

	            // Crossover operator
	            if (crossMethod == 0)
	            {                      // DE operator
	                parameters = new HashMap();
	                parameters.put("CR", DeCrossRate);
	                parameters.put("F", DeFactor);
	                crossover = CrossoverFactory.getCrossoverOperator(
	                        "DifferentialEvolutionCrossover", parameters);
	                algorithm.addOperator("crossover", crossover);
	            }
	            else if (crossMethod == 1) {                // SBX operator
	                parameters = new HashMap();
	                parameters.put("probability", 1.0);
	                //parameters.put("distributionIndex", 20.0);
	                crossover = CrossoverFactory.getCrossoverOperator("BlocCrossover",
	                        parameters);
	                algorithm.addOperator("crossover", crossover);
	            }

	            // Mutation operator
	            parameters = new HashMap();
	            parameters.put("probability", 1.0 / problem.getNumberOfVariables());
	            mutation = MutationFactory.getMutationOperator("MyMutation", parameters);
	            algorithm.addOperator("mutation", mutation);

	            // Selection Operator
	            parameters = null;
	            selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters);
	            algorithm.addOperator("selection", selection);
	            long initTime = System.currentTimeMillis();
                SolutionSet pop = algorithm.execute();
                long estimatedTime = System.currentTimeMillis() - initTime;
                System.out.println("genetic execution time: " + estimatedTime + "ms");
                getLocalFromChromo(pop.get(pop.size()/2));
                
	        }
	   public void getLocalFromChromo(Solution solution)
	   {

		  for(int i=0;i<2;i++)
		  {
			  for(int j=0;j<nbCluster;j++)
			  {
				  cl[i][j]=qualityLevel[((ArrayInt)(solution.getDecisionVariables()[j])).array_[i]][2*j+i];
			  }
		  }
		   
	   }


}
