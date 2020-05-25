package fr.insa.laas.Avatar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

 

public class ClusterQoS {
	private ArrayList<ClusterMemberQoS> avatars;
	private double seuilminT;
	private double seuilmaxT;

	private double seuilminD;
	private double seuilmaxD;

	private double seuilminR;
	private double seuilmaxR;

	private double seuilminC;
	private double seuilmaxC;


	private double CVminT;
	private double CVmaxT;

	private double CVminD;
	private double CVmaxD;

	private double CVminR;
	private double CVmaxR;

	private ArrayList<Double> utilities;
	private ArrayList<Double> fluctuations;
	 double levelTime[];
	  double levelDisp[];
	private double levelCost[];
	private double levelRep[];




	public double getCVminT() {
		return CVminT;
	}
	public void setCVminT(double cVminT) {
		CVminT = cVminT;
	}
	public double getCVmaxT() {
		return CVmaxT;
	}
	public void setCVmaxT(double cVmaxT) {
		CVmaxT = cVmaxT;
	}
	public double getCVminD() {
		return CVminD;
	}
	public void setCVminD(double cVminD) {
		CVminD = cVminD;
	}
	public double getCVmaxD() {
		return CVmaxD;
	}
	public void setCVmaxD(double cVmaxD) {
		CVmaxD = cVmaxD;
	}
	public double getCVminR() {
		return CVminR;
	}
	public void setCVminR(double cVminR) {
		CVminR = cVminR;
	}
	public double getCVmaxR() {
		return CVmaxR;
	}
	public void setCVmaxR(double cVmaxR) {
		CVmaxR = cVmaxR;
	}
	public ArrayList<ClusterMemberQoS> getAvatars() {
		return avatars;
	}
	public void setAvatars(ArrayList<ClusterMemberQoS> avatars) {
		this.avatars = avatars;
	}
	public double getSeuilminT() {
		return seuilminT;
	}
	public void setSeuilminT(double seuilminT) {
		this.seuilminT = seuilminT;
	}
	public double getSeuilmaxT() {
		return seuilmaxT;
	}
	public void setSeuilmaxT(double seuilmaxT) {
		this.seuilmaxT = seuilmaxT;
	}
	public double getSeuilminD() {
		return seuilminD;
	}
	public void setSeuilminD(double seuilminD) {
		this.seuilminD = seuilminD;
	}
	public double getSeuilmaxD() {
		return seuilmaxD;
	}
	public void setSeuilmaxD(double seuilmaxD) {
		this.seuilmaxD = seuilmaxD;
	}
	public double getSeuilminR() {
		return seuilminR;
	}
	public void setSeuilminR(double seuilminR) {
		this.seuilminR = seuilminR;
	}
	public double getSeuilmaxR() {
		return seuilmaxR;
	}
	public void setSeuilmaxR(double seuilmaxR) {
		this.seuilmaxR = seuilmaxR;
	}
	public double getSeuilminC() {
		return seuilminC;
	}
	public void setSeuilminC(double seuilminC) {
		this.seuilminC = seuilminC;
	}
	public double getSeuilmaxC() {
		return seuilmaxC;
	}
	public void setSeuilmaxC(double seuilmaxC) {
		this.seuilmaxC = seuilmaxC;
	}

	public ClusterQoS(ArrayList<ClusterMemberQoS>a,double [] w)
	{
		this.avatars=a;
		utilities=new ArrayList<Double>();
		fluctuations=new ArrayList<Double>();
		getSeuils();
		getCVs();
		calcule(w);
		
	}
	public void getQualitLevel(int d)
	{
		levelTime=new double[d];
		levelDisp=new double[d];
		/*levelRep=new double[d];
		levelTime=new double[d];*/
		//fill tabs for sort
		double[] lt=new double[avatars.size()];
		double[] ld=new double[avatars.size()];
		/*double[] lc=new double[avatars.size()];
		double[] lr=new double[avatars.size()];*/
		for(int i=0;i<avatars.size();i++)
		{
			lt[i]=avatars.get(i).getQos()[0];
			ld[i]=avatars.get(i).getQos()[1];
			/*lc[i]=avatars.get(i).getQos()[3];
			lr[i]=avatars.get(i).getQos()[2];*/
			
		}
		Arrays.sort(lt);
		Arrays.sort(ld);
		//Arrays.sort(lr);
		//Arrays.sort(lc);
		
		levelTime[0]=lt[0];
		levelDisp[0]=ld[0];
		/*levelRep[0]=lr[0];
		levelCost[0]=lc[0];*/
		
	
		/*System.out.println("l[0]"+levelRep[0]);
		System.out.println("l[0]"+levelCost[0]);*/
		levelTime[d-1]=lt[avatars.size()-1];
		levelDisp[d-1]=ld[avatars.size()-1];
		/*levelRep[d-1]=lr[avatars.size()-1];
		levelCost[d-1]=lc[avatars.size()-1];*/
		

		 
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int p=0, index=1,i=1;
		//TIME
		System.out.println("temps");
		System.out.println(levelTime[0]);
		while(index <d-1)
		{
			if (i+(avatars.size()-2)/(d-2) > avatars.size()-2) p=random.nextInt(i, avatars.size()-1);
			else p=random.nextInt(i, i+(avatars.size()-2)/(d-2));
			i=i+(avatars.size()-2)/(d-2);
			levelTime[index]=lt[p];
			System.out.println( levelTime[index]);
			index++;
			
		}
		
		System.out.println(levelTime[d-1]);
		//DISP
		System.out.println("dispo");
		System.out.println(levelDisp[0]);
		index=1;i=1;p=0;
				while(index <d-1)
				{
					if (i+(avatars.size()-2)/(d-2) > avatars.size()-2) p=random.nextInt(i, avatars.size()-1);
					else p=random.nextInt(i, i+(avatars.size()-2)/(d-2));
					i=i+(avatars.size()-2)/(d-2);
					levelDisp[index]=ld[p];
					System.out.println(levelDisp[index]);
					index++;
					
				}
				System.out.println(levelDisp[d-1]);
	/*	//rep
				System.out.println("rep");
		 index=1;i=1;p=0;
			while(index <d-1)
			{
				if (i+(avatars.size()-2)/(d-2) > avatars.size()-2) p=random.nextInt(i, avatars.size()-1);
				else p=random.nextInt(i, i+(avatars.size()-2)/(d-2));
				i=i+(avatars.size()-2)/(d-2);
				levelRep[index]=lr[p];
				System.out.println("l["+index+"]"+levelRep[index]);
				index++;
							
			}
		//cost
			System.out.println("cost");
		index=1;i=1;p=0;
		while(index <d-1)
		{
					if (i+(avatars.size()-2)/(d-2) > avatars.size()-2) p=random.nextInt(i, avatars.size()-1);
					else p=random.nextInt(i, i+(avatars.size()-2)/(d-2));
					i=i+(avatars.size()-2)/(d-2);
					levelCost[index]=lc[p];
					System.out.println("l["+index+"]"+levelCost[index]);
					index++;
								
		}*/
		 
	}

	public void calcule(double [] w)
	{
		for(int i=0; i<avatars.size();i++)
		{
			utilities.add(Util.Utility(avatars.get(i), this, w));
			fluctuations.add(Util.fluctAvatar(this, avatars.get(i), w));
		}

	}

	public ArrayList<Double> getUtilities() {
		return utilities;
	}
	public void setUtilities(ArrayList<Double> utilities) {
		this.utilities = utilities;
	}
	public ArrayList<Double> getFluctuations() {
		return fluctuations;
	}
	public void setFluctuations(ArrayList<Double> fluctuations) {
		this.fluctuations = fluctuations;
	}
	public double maxUtility()
	{
		return Collections.max(utilities);
	}
	public double minFluctuation()
	{
		return Collections.min(fluctuations);
	}
	public void getSeuils()
	{
		seuilminT=avatars.get(0).getQos()[0];
		seuilmaxT=avatars.get(0).getQos()[0];
		seuilminD=avatars.get(0).getQos()[1];
		seuilmaxD=avatars.get(0).getQos()[1];
	/*	seuilminR=avatars.get(0).getQos()[2];
	 	seuilmaxR=avatars.get(0).getQos()[2];
	 	seuilminC=avatars.get(0).getQos()[3];
	 	seuilmaxC=avatars.get(0).getQos()[3];*/
	 	for (int i=0;i<avatars.size();i++)
	 	{
	 		if (this.seuilminT>avatars.get(i).getQos()[0]) seuilminT=avatars.get(i).getQos()[0];
	 		if (this.seuilmaxT<avatars.get(i).getQos()[0]) seuilmaxT=avatars.get(i).getQos()[0];
	 		
	 		if (this.seuilminD>avatars.get(i).getQos()[1]) seuilminD=avatars.get(i).getQos()[1];
	 		if (this.seuilmaxD<avatars.get(i).getQos()[1]) seuilmaxD=avatars.get(i).getQos()[1];
	 		
	 		/*if (this.seuilminR>avatars.get(i).getQos()[2]) seuilminR=avatars.get(i).getQos()[2];
	 		if (this.seuilmaxR<avatars.get(i).getQos()[2]) seuilmaxR=avatars.get(i).getQos()[2];
	 		
	 		if (this.seuilminC>avatars.get(i).getQos()[3]) seuilminC=avatars.get(i).getQos()[3];
	 		if (this.seuilmaxC<avatars.get(i).getQos()[3]) seuilmaxC=avatars.get(i).getQos()[3];*/
		 
	 	}
	}

	public void getCVs()
	{
	    CVminT=Util.CV(avatars.get(0),0);
	    CVmaxT=Util.CV(avatars.get(0),0);
	    CVminD=Util.CV(avatars.get(0),1);
	    CVmaxD=Util.CV(avatars.get(0),1);
	    /*CVminR=Util.CV(avatars.get(0),2);
	    CVmaxR=Util.CV(avatars.get(0),2);*/
	  
	 	for (int i=0;i<avatars.size();i++)
	 	{
	 		if (this.CVminT>Util.CV(avatars.get(i),0)) CVminT=Util.CV(avatars.get(i),0);
	 		if (this.CVmaxT<Util.CV(avatars.get(i),0)) CVmaxT=Util.CV(avatars.get(i),0);
	 		
	 		if (this.CVminD>Util.CV(avatars.get(i),1)) CVminD=Util.CV(avatars.get(i),1);
	 		if (this.CVmaxD<Util.CV(avatars.get(i),1)) CVmaxD=Util.CV(avatars.get(i),1);
	 		
	 	/*	if (this.CVminR>Util.CV(avatars.get(i),2)) CVminR=Util.CV(avatars.get(i),2);
	 		if (this.CVmaxR<Util.CV(avatars.get(i),2)) CVmaxR=Util.CV(avatars.get(i),2);*/
	 	}
	}
	public double[] getLevelTab(int index)
	{
		switch (index)
		{
		case 0: return levelTime;
		case 1: return levelDisp;
		/*case 2: return levelRep;
		case 3: return levelCost;*/
		default :return null;
		}
		
	}
	public String getSelectedAvatars(double []cls)
	{
		//ArrayList<String> selected=new ArrayList<String>();
		double opt=0;
		for (int i=0; i< avatars.size();i++)
		{
			 
			if (avatars.get(i).getQos()[0]<=cls[0] && avatars.get(i).getQos()[1]>=cls[1]/* && avatars.get(i).getQos()[2]>=cls[2]&& avatars.get(i).getQos()[3]<= cls[3]*/ )
			{
				//selected.add(avatars.get(i).getUrl());
				if (opt < (utilities.get(i)-fluctuations.get(i))) opt=utilities.get(i)-fluctuations.get(i);
			}
			 
		}
		//selected.add(String.valueOf(opt));
		return String.valueOf(opt);
	}


}
