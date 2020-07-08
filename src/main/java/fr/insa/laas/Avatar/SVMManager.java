package fr.insa.laas.Avatar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class SVMManager 
{
	private int numberOfTask;
	private int numberOfExecution;
	private int windowsSize;
	private double [][] casesTime;
	private double [][] casesThroughput;
	
	
	public SVMManager(int p, int e,int l)
	{
		numberOfExecution=e;
		numberOfTask=p;
		windowsSize=l;
		casesTime=new double[p][e];
		casesThroughput=new double[p][e];
		
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
				caseLine=caseLine+" "+(l+1)+":"+tab[j][l];
			}
			 System.out.println(caseLine);
			switch(k)
			{
			case 0:
				File fout = new File("Time/Task"+i);
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
			 
				
			case 1:
				File fout1 = new File("Dispo/Task"+i);
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

}
