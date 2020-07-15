package fr.insa.laas.Avatar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
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
	
	
	private String srtm;
	private String srtr;
	private String su;
	private String sf;
	private double [][] utilitiesE;
	private double [][] fluctuationsE;
	private double [][] c;
	
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
	 public SelectionManager()
	 {
		 
	 }
	 
    public SelectionManager(int nbc,int d) 
    {
    	this.nbCluster=nbc;
		this.d=d;
	    cl=new double [2][nbCluster];
		qualityLevel=new double[d][2*nbCluster];
		utilities=new double[d][2*nbCluster];
		fluctuations=new double[d][2*nbCluster];
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
		//System.out.println("fin data fill");
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
	
      /* try {
			cm.sendLocalConstraint(list, cl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
   	cl=new double [2][nbCluster];
   	Double opt=cm.opt;
   	cm.opt=0;
    executeGenetic(100000, 0.0001);
   /* try {
		cm.sendLocalConstraint(list, cl);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    System.out.println("optimalite exacte = "+opt);
    System.out.println("optimalite genetic = "+cm.opt);
    System.out.println("optimalite = "+cm.opt/opt);*/
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
				
				 
				qualityLevelS[j][i*nbCluster+k]=tab[j];
				utilitiesS[j][i*nbCluster+k]=new QualityLevel(c[k],i,tab[j]).p();
				fluctuationsS[j][i*nbCluster+k]=new QualityLevel(c[k],i,tab[j]).f();


			}
		  }
	    }
 
	}
	public void getDataFromTabGenetic(ClusterQoS []c)
	{
	 
		double []tab=new double[d];
		 
	    for(int k=0;k<c.length;k++)
	    {
		for(int i=0; i<2;i++)
		{
			tab=c[k].getLevelTab(i);
			for(int j=0;j<d;j++)
			{
				
				 
				qualityLevel[j][2*k+i]=tab[j];
				utilities[j][2*k+i]=new QualityLevel(c[k],i,tab[j]).p();
				fluctuations[j][2*k+i]=new QualityLevel(c[k],i,tab[j]).f();


			}
		}
	    }
 
	}
	public void fillDataUseCaseGenetic ()
	{
		d=4;
		nbCluster=2;
		qualityLevel=new double[d][2*nbCluster];
		utilities=new double[d][2*nbCluster];
		fluctuations=new double[d][2*nbCluster];
		cl=new double [2][nbCluster];
		//fill ql
		/*qualityLevel[0][0]=31.85;
		qualityLevel[0][1]=5;
		qualityLevel[0][2]=61.62;
		qualityLevel[0][3]=4.28;
		qualityLevel[0][4]=41.42;
		qualityLevel[0][5]=11.71;
		
		qualityLevel[1][0]=47.14;
		qualityLevel[1][1]=11.14;
		qualityLevel[1][2]=79.49;
		qualityLevel[1][3]=5;
		qualityLevel[1][4]=44.57;
		qualityLevel[1][5]=12.57;
		
		qualityLevel[2][0]=48.57;
		qualityLevel[2][1]=14.85;
		qualityLevel[2][2]=92.7;
		qualityLevel[2][3]=7.57;
		qualityLevel[2][4]=50.42;
		qualityLevel[2][5]=13.42;
		
		qualityLevel[3][0]=69.57;
		qualityLevel[3][1]=16.57;
		qualityLevel[3][2]=94.85;
		qualityLevel[3][3]=9.57;
		qualityLevel[3][4]=53.71;
		qualityLevel[3][5]=16.14;
		
		//fill f
		fluctuations[0][0]=0.1;
		fluctuations[0][1]=1;
		fluctuations[0][2]=0.16;
		fluctuations[0][3]=1;
		fluctuations[0][4]=0.07;
		fluctuations[0][5]=0.88;
		
		fluctuations[1][0]=0.2;
		fluctuations[1][1]=0.31;
		fluctuations[1][2]=0.33;
		fluctuations[1][3]=0.58;
		fluctuations[1][4]=0.31;
		fluctuations[1][5]=0.77;
		
		fluctuations[2][0]=0.41;
		fluctuations[2][1]=0.26;
		fluctuations[2][2]=0.42;
		fluctuations[2][3]=0.42;
		fluctuations[2][4]=0.39;
		fluctuations[2][5]=0.39;
		
		fluctuations[3][0]=0.88;
		fluctuations[3][1]=0.15;
		fluctuations[3][2]=0.88;
		fluctuations[3][3]=0.25;
		fluctuations[3][4]=0.77;
		fluctuations[3][5]=0.23;
		
		//fill u
		
		utilities[0][0]=0.22;
		utilities[0][1]=1;
		utilities[0][2]=0.11;
		utilities[0][3]=0.88;
		utilities[0][4]=0.11;
		utilities[0][5]=0.88;
		
		utilities[1][0]=0.44;
		utilities[1][1]=0.66;
		utilities[1][2]=0.44;
		utilities[1][3]=0.77;
		utilities[1][4]=0.44;
		utilities[1][5]=0.77;
		
		utilities[2][0]=0.55;
		utilities[2][1]=0.55;
		utilities[2][2]=0.55;
		utilities[2][3]=0.44;
		utilities[2][4]=0.55;
		utilities[2][5]=0.55;
		
		utilities[3][0]=0.88;
		utilities[3][1]=0.33;
		utilities[3][2]=0.77;
		utilities[3][3]=0.22;
		utilities[3][4]=0.77;
		utilities[3][5]=0.33;
		*/
		/////////////////////////////////////////////////////
		//decompo2
		//fill ql
			/*	qualityLevel[0][0]=61.42;
				qualityLevel[0][1]=4.28;
				qualityLevel[0][2]=46.85;
				qualityLevel[0][3]=7.42;
				 
				
				qualityLevel[1][0]=79.42;
				qualityLevel[1][1]=5;
				qualityLevel[1][2]=48.42;
				qualityLevel[1][3]=12.14;
			 
				
				qualityLevel[2][0]=92.7;
				qualityLevel[2][1]=7.57;
				qualityLevel[2][2]=48.71;
				qualityLevel[2][3]=12.85;
				 
				
				qualityLevel[3][0]=94.85;
				qualityLevel[3][1]=9.57;
				qualityLevel[3][2]=51.28;
				qualityLevel[3][3]=14;
			 
		//fill f
				fluctuations[0][0]=0.04;
				fluctuations[0][1]=1;
				fluctuations[0][2]=0.16;
				fluctuations[0][3]=1;
				 
				
				fluctuations[1][0]=0.25;
				fluctuations[1][1]=0.44;
				fluctuations[1][2]=0.33;
				fluctuations[1][3]=0.58;
				 
				
				fluctuations[2][0]=0.38;
				fluctuations[2][1]=0.31;
				fluctuations[2][2]=0.42;
				fluctuations[2][3]=0.42;
			 
				
				fluctuations[3][0]=0.88;
				fluctuations[3][1]=0.13;
				fluctuations[3][2]=0.88;
				fluctuations[3][3]=0.25;
				//u
				 
		utilities[0][0]=0.11;
		utilities[0][1]=0.88;
		utilities[0][2]=0.22;
		utilities[0][3]=1;
		 
		
		utilities[1][0]=0.44;
		utilities[1][1]=0.77;
		utilities[1][2]=0.44;
		utilities[1][3]=0.77;
		 
		
		utilities[2][0]=0.66;
		utilities[2][1]=0.44;
		utilities[2][2]=0.55;
		utilities[2][3]=0.55;
	 
		
		utilities[3][0]=0.77;
		utilities[3][1]=0.22;
		utilities[3][2]=0.88;
		utilities[3][3]=0.33;*/
	 
		
		
		
		
		
		///
/////////////////////////////////////////////////////
//decompo3
//fill ql
qualityLevel[0][0]=28.14;
qualityLevel[0][1]=4.28;
qualityLevel[0][2]=32.28;
qualityLevel[0][3]=9;


qualityLevel[1][0]=37.42;
qualityLevel[1][1]=5;
qualityLevel[1][2]=42;
qualityLevel[1][3]=12;


qualityLevel[2][0]=41.28;
qualityLevel[2][1]=7.57;
qualityLevel[2][2]=51.42;
qualityLevel[2][3]=18.85;


qualityLevel[3][0]=42.28;
qualityLevel[3][1]=9.57;
qualityLevel[3][2]=52.57;
qualityLevel[3][3]=19;

//fill f
fluctuations[0][0]=0.01;
fluctuations[0][1]=0.55;
fluctuations[0][2]=0.04;
fluctuations[0][3]=1;


fluctuations[1][0]=0.25;
fluctuations[1][1]=0.44;
fluctuations[1][2]=0.17;
fluctuations[1][3]=0.3;


fluctuations[2][0]=0.38;
fluctuations[2][1]=0.31;
fluctuations[2][2]=0.26;
fluctuations[2][3]=0.17;


fluctuations[3][0]=0.88;
fluctuations[3][1]=0.05;
fluctuations[3][2]=0.77;
fluctuations[3][3]=0.13;
//u

utilities[0][0]=0.11;
utilities[0][1]=0.88;
utilities[0][2]=0.11;
utilities[0][3]=1;


utilities[1][0]=0.44;
utilities[1][1]=0.77;
utilities[1][2]=0.44;
utilities[1][3]=0.77;


utilities[2][0]=0.66;
utilities[2][1]=0.55;
utilities[2][2]=0.66;
utilities[2][3]=0.44;


utilities[3][0]=0.88;
utilities[3][1]=0.22;
utilities[3][2]=0.77;
utilities[3][3]=0.33;
		
		executeGenetic(79.49, 7.57);
		
		
	}
	
	
	public void buildDataExaustive(ClusterQoS []c)
	{
		//build utility matrix
	 
		  su="";
		  sf="";
	      srtm="";
		  srtr="";
		for(int i=0;i<c[0].getAvatars().size();i++)
		{
			for(int j=0;j<c.length;j++)
			{
				su=su+c[j].getUtilities().get(i)+" ";
				utilitiesE[i][j]=c[j].getUtilities().get(i);
				sf=sf+c[j].getFluctuations().get(i)+" ";
				fluctuationsE[i][j]=c[j].getFluctuations().get(i);
				srtm=srtm+c[j].getAvatars().get(i).getQos()[0]+" ";
				srtr=srtr+c[j].getAvatars().get(i).getQos()[1]+" ";
				
			}
			su=su.substring(0,su.length()-2);
			sf=sf.substring(0,sf.length()-2);
			srtm=srtm.substring(0,srtm.length()-2);
			srtr=srtr.substring(0,srtr.length()-2);
			  su=su+";";
			  sf=sf+";";
		      srtm=srtm+";";
			  srtr=srtr+";";
		}
		su=su.substring(0,su.length()-2);
		sf=sf.substring(0,sf.length()-2);
		srtm=srtm.substring(0,srtm.length()-2);
		srtr=srtr.substring(0,srtr.length()-2);
		 /*System.out.println(su);
		 System.out.println(sf);
		 System.out.println(srtm);
		 System.out.println(srtr);*/
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
	 	    			sl.add(s);
 	 	    		
	    				
	    			}*/
 	    			//convert to 2d array
 	    			linesave=tab.get(tab.size()/2);
 	    			line=linesave.substring(1,linesave.length()-1);
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
	    						//System.out.print(cl[i][k] +" ");
	    						k++;
	    						}
	    					j++;
	    				}
	    				j=0;
	    				k=0;
	    				
	    				//System.out.println();
	    			}
	     			 long estimatedTime = System.currentTimeMillis() - initTime;
	     			getLocalConstraint(cl);
	 	            System.out.println("exact execution time"+estimatedTime);
	     			
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
        double opt=0;
		for(int i=0;i<d;i++)
		{
			 
			for(int j=0;j<nbCluster;j++)
			{
				
				if(in[i][j]==1.0f) 
					{cl[0][j]=qualityLevelS[i][j];opt=opt+utilitiesS[i][j]-fluctuationsS[i][j];}
			}
		}
		
		for(int i=0;i<d;i++)
		{
			 
			for(int j=nbCluster;j<(2)*nbCluster;j++)
			{
				
				if(in[i][j]==1.0f) 
					{cl[1][j-nbCluster]=qualityLevelS[i][j];opt=opt+utilitiesS[i][j]-fluctuationsS[i][j];}
			}
		}
        System.out.println("optimality decompo exacte ="+opt);
     	//showcl();
		
 
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
                //QualityIndicator q=new QualityIndicator(problem, sl);
	            // Selection Operator
	            parameters = null;
	            selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters);
	            algorithm.addOperator("selection", selection);
	            long initTime = System.currentTimeMillis();
	             
                SolutionSet pop = algorithm.execute();
                getLocalFromChromo(pop.get(pop.size()/2));
                long elapsedTime = System.currentTimeMillis() - initTime;
    			System.out.println("genetic Time "+elapsedTime);
                //System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
               /* 
                
                algorithm.setInputParameter("maxEvaluations", 5000);
	            algorithm.setInputParameter("nr", 4);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                
                
                
                algorithm.setInputParameter("maxEvaluations", 5000);
	            algorithm.setInputParameter("nr", 6);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                
                
                
                algorithm.setInputParameter("maxEvaluations", 5000);
	            algorithm.setInputParameter("nr", 8);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                
                algorithm.setInputParameter("maxEvaluations",10000);
	            algorithm.setInputParameter("nr", 2);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                
                
                algorithm.setInputParameter("maxEvaluations",10000);
	            algorithm.setInputParameter("nr", 4);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                
                
                
                algorithm.setInputParameter("maxEvaluations", 10000);
	            algorithm.setInputParameter("nr", 6);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                
                
                
                algorithm.setInputParameter("maxEvaluations", 10000);
	            algorithm.setInputParameter("nr", 8);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                
                
                algorithm.setInputParameter("maxEvaluations",20000);
	            algorithm.setInputParameter("nr", 2);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                
                
                algorithm.setInputParameter("maxEvaluations",20000);
	            algorithm.setInputParameter("nr", 4);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                
                
                
                algorithm.setInputParameter("maxEvaluations", 20000);
	            algorithm.setInputParameter("nr", 6);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                
                
                
                algorithm.setInputParameter("maxEvaluations", 20000);
	            algorithm.setInputParameter("nr", 8);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                
                
                algorithm.setInputParameter("maxEvaluations",30000);
	            algorithm.setInputParameter("nr", 2);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                
                
                algorithm.setInputParameter("maxEvaluations",30000);
	            algorithm.setInputParameter("nr", 4);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                
                
                
                algorithm.setInputParameter("maxEvaluations", 30000);
	            algorithm.setInputParameter("nr", 6);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                
                
                
                algorithm.setInputParameter("maxEvaluations", 30000);
	            algorithm.setInputParameter("nr", 8);
                pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                /*algorithm.setInputParameter("maxEvaluations", maxFES);
	            algorithm.setInputParameter("nr", 10);
                 pop = algorithm.execute();
                System.out.println("genetic quality :"+(q.getHypervolume(pop)+1/q.getIGD(pop)));
                */
              //  long estimatedTime = System.currentTimeMillis() - initTime;
              //  System.out.println("genetic execution time: " + estimatedTime + "ms");
                //getLocalFromChromo(pop.get(pop.size()/2));
                
	        }
	   public void getLocalFromChromo(Solution solution)
	   {
          double opt=0;
		  for(int i=0;i<2;i++)
		  {
			  System.out.println("#########");
			  for(int j=0;j<nbCluster;j++)
			  {
				  cl[i][j]=qualityLevel[((ArrayInt)(solution.getDecisionVariables()[j])).array_[i]][2*j+i];
				  System.out.println("cluster "+j+"  "+cl[i][j]);
				  opt=opt+utilities[((ArrayInt)(solution.getDecisionVariables()[j])).array_[i]][2*j+i]-fluctuations[((ArrayInt)(solution.getDecisionVariables()[j])).array_[i]][2*j+i];
			  }
		  }
		  System.out.println("optimality decomp genetic = "+opt);
	   }
	   
	   public double getOpt()
	   {
		  double opt=0;
		  System.out.println(utilitiesE.length);
		  for(int i=0;i<utilitiesE.length;i++)
		  {
			  for(int j=0;j<nbCluster;j++)
			  {
				  opt=(utilitiesE[i][j]-fluctuationsE[i][j])*c[i][j]+opt;
			  }
		  }
		  return opt;
	   }
	   
	   
	   
	   public void optimalityEvaluation(int nbc,int nba,int d)
		{
			this.nbCluster=nbc;
			this.d=d;
		    cl=new double [2][nbCluster];
			qualityLevel=new double[d][2*nbCluster];
			utilities=new double[d][2*nbCluster];
			fluctuations=new double[d][2*nbCluster];
			
			qualityLevelS=new double[d][2*nbCluster];
			utilitiesS=new double[d][2*nbCluster];
			fluctuationsS=new double[d][2*nbCluster];
			utilitiesE=new double[nba][nbCluster];
			fluctuationsE=new double[nba][nbCluster];
		 
			 
			ClusterQoS c[]=new QoSManager().fillCluster(nba, nbc);
			double [] w={0.4,0.3};
			for(int i=0;i<nbc;i++)
			{
				//c[i]=new ClusterQoS(new QoSManager().fillClusterData(nba, i), w);
				c[i].getQualitLevel(d);
			}
			getDataFromTabGenetic(c);
			buildDataExaustive(c);
			//System.out.println("\""+srtm+"\" \""+srtr+"\" \""+su+"\" \""+sf+"\"");
			executeExauSolver();
			//System.out.println("End data fill and quality level calculation");
			double opt=0;
			getDataFromTab(c);
			toJuliaArray();
			//System.out.println("\""+srlevel+"\" \""+sru+"\" \""+srf+"\" \"");

			executeSelectionSolver();
		
			 for (int i=0;i<nbc;i++)
		 	{
		         	double []t={cl[0][i],cl[1][i]};
		 			opt=Double.valueOf(c[i].getSelectedAvatars(t))+opt;
		 			
		 	}
			System.out.println("optimality exact= "+opt/getOpt());
	            

	   	   /*  cl=new double [2][nbCluster];
	   	     opt=0;
	         executeGenetic(100000, 0.0001);
	         for (int i=0;i<nbc;i++)
	 		{
	         	double []t={cl[0][i],cl[1][i]};
	         	opt=Double.valueOf(c[i].getSelectedAvatars(t))+opt;	 			
	 		}
	         System.out.println("optimality genetic= "+opt);*/
	    
			/*long startTime = System.nanoTime();
			//exact method
			getDataFromTab(c);
			toJuliaArray();
	        executeSelectionSolver();
	        for (int i=0;i<nbc;i++)
			{
	        	double []t={cl[0][i],cl[1][i]};
				System.out.println("optim c"+i+" "+c[i].getSelectedAvatars(t));
				
			}
	        long elapsedTime = System.nanoTime() - startTime;
			System.out.println("selectionTime "+elapsedTime/1000000f);*/
			//genetic method
			//exaustive method
			
		   
		   
	   }
	   
	   public void executeExauSolver()
		{
		  // System.out.println("\""+srtm+"\" \""+srtr+"\" \""+su+"\" \""+sf+"\"");
			
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
				  
				  
		    		String[] cmd = { "bash", "-c", "./exaustive.jl \""+srtm+"\" \""+srtr+"\" \""+su+"\" \""+sf+"\"" };
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
		 	    			sl.add(s);
	 	 	    		
		    				
		    			}*/
	 	    			//convert to 2d array
	 	    			linesave=tab.get(tab.size()/2);
	 	    			line=linesave.substring(1,linesave.length()-1);
	 	    			 
		    			String [] arr=line.split(";");
		    			 
		    			int j=0,k=0;
		    			c=new double [arr.length][nbCluster];
		     			for(int i=0;i<arr.length;i++)
		    			{
		     				String[] arrc=arr[i].split(" ");
		    				
		    				while(j<arrc.length)
		    				{
		    					if(!arrc[j].isEmpty())
		    						{
		    						
		    						c[i][k]=Float.valueOf(arrc[j]);
		    						//System.out.print(c[i][k] +" ");
		    						k++;
		    						}
		    					j++;
		    				}
		    				j=0;
		    				k=0;
		    				
		    				System.out.println();
		    			}
		     			 long estimatedTime = System.currentTimeMillis() - initTime;
		     			 System.out.println("exaustive method time = "+estimatedTime);
		 	            System.out.println("optimality exaustive ="+getOpt());
		     		 
		    		} else {
		    			//abnormal...
		    		}

		    	} catch (IOException e) {
		    		e.printStackTrace();
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}

		    

		}
	   public void predictionSelection(int windowsSize)
	   {
		   nbAvatars=100;
		   nbCluster=10;
		   utilitiesE=new double[nbAvatars][nbCluster];
			fluctuationsE=new double[nbAvatars][nbCluster];
		  double[][][] cases=new double[2][10][windowsSize];
		  cases[0][0]=new double[] {44.85,41.0,47.4,41.45,50.8};
		  cases[0][1]=new double[] {58.2,43.85,49.9,51.35,51.8};
		  cases[0][2]=new double[] {54.2,43.35,38.3,54.9,40.0};
		  cases[0][3]=new double[] {54.7,54.95,48.6,43.5,51.4};
		  cases[0][4]=new double[] {48.05,52.45,46.55,55.05,53.9};
		  cases[0][5]=new double[] {25.95,57.75,49.65,48.45,50.25};
		  cases[0][6]=new double[] {51.6,47.75,57.5,45.2,50.6};
		  cases[0][7]=new double[] {48.95,56.1,51.7,47.05,58.1};
		  cases[0][8]=new double[] {47.0,45.7,47.6,49.75,48.8};
		  cases[0][9]=new double[] {48.45,49.05,48.45,50.9,50.95};
		  
		  cases[1][0]=new double[] {11.0,15.25,13.1,16.65,13.85};
		  cases[1][1]=new double[] {14.25,14.0,14.35,15.15,13.85};
		  cases[1][2]=new double[] {15.45,14.95,11.5,15.45,13.6};
		  cases[1][3]=new double[] {15.3,15.5,14.15,15.7,14.55};
		  cases[1][4]=new double[] {10.1,16.65,14.9,12.25,13.55};
		  cases[1][5]=new double[] {16.75,14.8,14.85,13.55,12.9};
		  cases[1][6]=new double[] {16.3,14.45,11.55,16.3,14.75};
		  cases[1][7]=new double[] {13.2,14.05,14.75,13.4,11.3};
		  cases[1][8]=new double[] {12.6,13.9,12.95,15.65,14.25};
		  cases[1][9]=new double[] {15.95,16.35,14.85,16.0,15.65};
		  
		  
		   
		   ClusterQoS c[]=new QoSManager().fillCluster(100, 10);
		   buildDataExaustive(c);
			//System.out.println("\""+srtm+"\" \""+srtr+"\" \""+su+"\" \""+sf+"\"");
			executeExauSolver();
		   SVMManager svm=new SVMManager(10,1000,20);
		   //svm.buildCases();
		   svm.buildModels();
		   cl=new double [2][10];
		   long initTime=System.currentTimeMillis();
		    for(int i=0;i<2;i++)
		    {
		    	for(int j=0;j<10;j++)
		    	{
		    		cl[i][j]=svm.predict(svm.getModel(i,j), cases[i][j]);
		    		
		    	}
		    }
		   
		    long estimatedTime = System.currentTimeMillis() - initTime;
			 System.out.println("prediction method time = "+estimatedTime);
			
	   	     double opt=0;
	         
	         for (int i=0;i<10;i++)
	 		{
	         	double []t={cl[0][i],cl[1][i]};
	         	opt=Double.valueOf(c[i].getSelectedAvatars(t))+opt;	 			
	 		}
	         System.out.println("optimality prediction = "+opt);
		   
	   }
	   


}
