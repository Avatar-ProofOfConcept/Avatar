package fr.insa.laas.Avatar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class SVMManager 
{
	private int numberOfTask;
	private int numberOfExecution;
	private int windowsSize;
	private double [][] casesTime;
	private double [][] casesThroughput;
	private ArrayList<Model> modelsTime;
	private ArrayList<Model> modelsTro;
	
	
	public SVMManager(int p, int e,int l)
	{
		numberOfExecution=e;
		numberOfTask=p;
		windowsSize=l;
		casesTime=new double[p][e];
		casesThroughput=new double[p][e];
		modelsTime=new ArrayList<Model>();
		modelsTro=new ArrayList<Model>();
		
	}
	public Model getModel(int i,int j)
	{
		switch(i)
		{
		      case 0: return modelsTime.get(j);
		      case 1: return modelsTro.get(j);
		      default: return null;
		}
	}
	public void buildCases()
	{
		//build a random process with p cluster
		for(int i=0;i<numberOfExecution;i++)
		{
		System.out.println("Execution "+i);
		ClusterQoS c[]=new QoSManager().fillCluster(1000, numberOfTask);
		double [] w={0.4,0.3};
		for(int j=0;j<numberOfTask;j++)
		{
			//c[i]=new ClusterQoS(new QoSManager().fillClusterData(nba, i), w);
			c[j].getQualitLevel(80);
		}
		SelectionManager sm=new SelectionManager(numberOfTask,80);
		sm.getDataFromTabGenetic(c);
		sm.executeGenetic(100000, 0.0001);
		    for (int k=0;k<numberOfTask;k++)
		    {
			  casesTime[k][i]=sm.getCl()[0][k];
			  casesThroughput[k][i]=sm.getCl()[1][k];
		    }
		}
		//showCases();
		buildDataSets(); 
		
	}
	
	public void buildDataSets()
	{
 		double [][] tab=new double [numberOfExecution-windowsSize][windowsSize];
		for (int k=0;k<2;k++)
		{
		for (int i=0;i<numberOfTask;i++)
		{
			 
			tab=new double [numberOfExecution-windowsSize][windowsSize];
			for (int j=0;j<numberOfExecution-windowsSize;j++)
			{
				tab[j]=getElementWindow(j, i, k);
				//showTab(tab[j]);
			}
	 
			writeCaseFile(tab,i,k);
		}
		}
		System.out.println("the end");
	}
	public void showTab(double [] tab)
	{
		for (int i=0;i<tab.length;i++)
		{
			System.out.println(tab[i]);
		}
	}
	private void writeCaseFile(double[][] tab, int i, int k) 
	{
		
	    String caseLine="";
		for (int j=0;j<numberOfExecution-windowsSize;j++)
		{
			caseLine=""+tab[j][windowsSize-1];
			for (int l=0;l<windowsSize;l++)
			{
				//caseLine=caseLine+" "+(l+1)+":"+tab[j][l]; //for libsvm
				caseLine=caseLine+","+tab[j][l];
				 
			}
			 
			switch(k)
			{
			case 0:
				File fout = new File("libsvm/"+"0"+"/Task"+i+"_"+0);
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(fout,true);
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
					 
					 
						bw.write(caseLine);
						bw.newLine();
					    bw.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
			break;
			case 1:
				File fout1 = new File("libsvm/"+"1"+"/Task"+i+"_"+1);
				FileOutputStream fos1;
				try {
					fos1 = new FileOutputStream(fout1,true);
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos1));
					 
					 
						bw.write(caseLine);
						bw.newLine();
					    bw.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			
		    }
		}
	}
	public void showCases()
	{
		for(int i=0;i<numberOfTask;i++)
		{
			System.out.println("Cases of task "+i);
			for(int j=0;j<numberOfExecution;j++)
			{
				System.out.println(casesTime[i][j]+"  "+casesThroughput[i][j]);
			}
		}
		
	}
	
	public double [] getElementWindow(int index,int taskId,int parameter)
	{
		double []result=new double[this.windowsSize];
		int k=0;
		switch(parameter)
		{
		case 0 :
		for(int i=index;i<index+windowsSize;i++)
		{
			result[k]=casesTime[taskId][i];
			k++;
		}
		 
		return result;
		case 1 :
		for(int i=index;i<index+windowsSize;i++)
		{
			result[k]=casesThroughput[taskId][i];
			k++;
		}
		return result;
		default :
		return null;
		}
		
	}
	public void executeLibSVM()
	{
		  //model generation
		/*for (int k=0;k<2;k++)
		{
		for(int i=0;i<numberOfTask;i++)
		{
		try {
    		String[] cmdtrain = {"bash", "-c", "java -classpath libsvm/libsvm.jar svm_train -s 3 -c 1.5 -t 0 -g 0.0000005 -e 0.1 libsvm/"+k+"/Task"+i+"_"+k};
    		long initTime = System.currentTimeMillis();
           
    		Process process = Runtime.getRuntime().exec(cmdtrain);
    		  
       
	    		 
    		BufferedReader reader = new BufferedReader(
    				new InputStreamReader(process.getInputStream()));

    		String line;
    		 
    		ArrayList<String> tab=new ArrayList<String>();
    		while ((line = reader.readLine()) != null) {
    			//output.append(line + "\n");
    			tab.add(line);
    			System.out.println("line "+line);
    			 
    		}
    		 long estimatedTime = System.currentTimeMillis() - initTime;
 			 System.out.println(estimatedTime);
    		 
    		int exitVal = process.waitFor();
    		if (exitVal == 0) {
    			 
	    	 
     			
     			 
     		 
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
		
		*/
		
		
		
		  //prediction
		long initTime = System.currentTimeMillis();
		   for (int k=0;k<2;k++)
				{
				for(int i=0;i<numberOfTask;i++)
				{
				try {
					String[] cmdpredict = {"bash", "-c", "java -classpath libsvm/libsvm.jar svm_predict libsvm/"+k+"/Task"+i+"_"+k+" Task"+i+"_"+k+".model libsvm/output"+i+"_"+k };
		    		
		           
		    		Process process = Runtime.getRuntime().exec(cmdpredict);
		    		  
		       
			    		 
		    		BufferedReader reader = new BufferedReader(
		    				new InputStreamReader(process.getInputStream()));

		    		String line;
		    		 
		    		ArrayList<String> tab=new ArrayList<String>();
		    		while ((line = reader.readLine()) != null) {
		    			//output.append(line + "\n");
		    			tab.add(line);
		    			System.out.println("line "+line);
		    			 
		    		}
		    	
		    		 
		    		int exitVal = process.waitFor();
		    		if (exitVal == 0) {
		    			 
			    	 
		     			
		     			 
		     		 
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
			 long estimatedTime = System.currentTimeMillis() - initTime;
 			 System.out.println(estimatedTime);
	}
	
	public void buildModels()
	{
		for (int k=0;k<2;k++)
		{
		for(int i=0;i<numberOfTask;i++)
		{
		try {
    		String[] cmdtrain = {"bash", "-c", "python3 model.py libsvm/"+k+"/Task"+i+"_"+k};
    	 
           
    		Process process = Runtime.getRuntime().exec(cmdtrain);
    		  
       
	    		 
    		BufferedReader reader = new BufferedReader(
    				new InputStreamReader(process.getInputStream()));

    		String line;
    		 
    		ArrayList<String> tab=new ArrayList<String>();
    		while ((line = reader.readLine()) != null) {
    			//output.append(line + "\n");
    			tab.add(line);
 
    			 
    			 
    		}
    		
    		 
    		int exitVal = process.waitFor();
    		if (exitVal == 0)
    		{
    			for (int l=0;l<tab.size();l++)
    			{
    				System.out.println(tab.get(l));
    				String datamodel=tab.get(l).substring(1,tab.get(l).length()-1);
    				String[] list=datamodel.split(" ");
    				ArrayList<String> tmp=new ArrayList<String>();
    				for(int h=0;h<list.length;h++)
    				{
    					if(!list[h].isEmpty() && list[h]!=" " ) tmp.add(list[h]);
    					
    				}
    				double[] w=new double [tmp.size()-1];
    				for(int g=0;g<tmp.size()-1;g++)
    				{
    					w[g]=Double.valueOf(tmp.get(g+1));
    				}
    				Model m=new Model(w,Double.valueOf(tmp.get(0)));
    				if(k==0) modelsTime.add(m);
    				if(k==1) modelsTro.add(m);
    				
    			}
    			 
	    	 
     			
     			 
     		 
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
		showModels();
		testPrediction();
	}

	public void showModels()
	{
		System.out.println("Time");
		for(int i=0;i<modelsTime.size();i++)
		{
			System.out.println("wights");
			for(int j=0;j<modelsTime.get(i).getWights().length;j++)
			{
				System.out.println(modelsTime.get(i).getWights()[j]);
			}
			System.out.println("intercept");
			System.out.println(modelsTime.get(i).getIntercept());
		}
		System.out.println("Throughput");
		for(int i=0;i<modelsTro.size();i++)
		{
			System.out.println("wights");
			for(int j=0;j<modelsTro.get(i).getWights().length;j++)
			{
				System.out.println(modelsTro.get(i).getWights()[j]);
			}
			System.out.println("intercept");
			System.out.println(modelsTro.get(i).getIntercept());
			
		}
		
	}
	public void testPrediction()
	{
		System.out.print("test");
		double []x=new double[] {44.85,41.0,47.4,41.45,50.8};
		System.out.println(predict(modelsTime.get(0),x));
		x=new double[] {11.0,15.25,13.1,16.65,13.85};
		System.out.println(predict(modelsTro.get(0),x));
	}
	public double predict(Model m,double[]x)
	{
		double sum=0;
		for(int i=0;i<x.length;i++)
		{
			sum=m.getWights()[i]*x[i]+sum;
		}
		sum=sum+m.getIntercept();
		return sum;
	}
}
