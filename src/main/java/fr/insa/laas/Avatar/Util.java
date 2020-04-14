package fr.insa.laas.Avatar;



public class Util {
	//Get an element from XML
	public String getXmlElement(String xml, String element){
		String res = "notFound";
		res = xml.split("<"+element+">")[1].split("</"+element+">")[0];
		return res;
	}
	
	//Add information to XML 
	public String addXmlElement(String xml, String element, String value){
		xml = xml+"<"+element+">"+value+"</"+element+">";
		return xml;
	}
	public boolean IsServiceOK(String taskLabel, String serviceLabel){
		if (taskLabel.equals(serviceLabel))
			return true;	
		else
			return false;
	}
/*******************Fluctuation****************/
	
	public static double fluctAvatar(ClusterQoS c,ClusterMemberQoS a,double [] w)
	{
		double tmp=0,f=0;
	
		for(int i=0;i<a.getQos().length-1;i++)
		{
			  
				switch(i)
				{
				case 0:
					 System.out.println(" temps  cv "+a.getCVt()+"cvmax= "+c.getCVmaxT()+"cvmin= "+c.getCVminT());
					tmp=w[i]*((c.getCVmaxT()-a.getCVt())/(c.getCVmaxT()-c.getCVminT()));
				break;
				
				case 1:
					System.out.println(" DISPO  cv "+a.getCVt()+"cvmax= "+c.getCVmaxD()+"cvmin= "+c.getCVminD());
					tmp=w[i]*((c.getCVmaxD()-a.getCVd())/(c.getCVmaxD()-c.getCVminD()));
			    break;
			    
				case 2 :
					System.out.println(" REP  cv "+a.getCVt()+"cvmax= "+c.getCVmaxR()+"cvmin= "+c.getCVminR());
			        tmp=w[i]*((c.getCVmaxR()-a.getCVr())/(c.getCVmaxR()-c.getCVminR()));
				break;
				
				 
				
				}
				//System.out.println(tmp);
			  
			  f=f+tmp;	
			
		}
		 System.out.println("F(a7)= "+f);
		return f;
	}
		
 
	
	public static double CV(ClusterMemberQoS a,int index)
	{
		return (S(a,index)/E(a,index))*100;
	}
	public static double E(ClusterMemberQoS a, int index)
	{
		double sum=0;
		for (int i=0;i<a.getHist(index).length;i++)
		{
			sum=sum+a.getHist(index)[i];
		}
		return (sum/a.getHist(index).length);
	}
	
	public static double S(ClusterMemberQoS a, int index)
	{
		double e=E(a,index);
		double sum=0;
		for (int i=0;i<a.getHist(index).length;i++)
		{
			sum=sum+Math.pow((a.getHist(index)[i]-e),2);
			
		}
		return Math.sqrt(sum/(a.getHist(index).length-1));
	}
	
	
/**************Utility**************/
	public static double Utility(ClusterMemberQoS a,ClusterQoS c,double [] w)
	{
		double tmp=0,u=0;
		for(int i=0;i < a.getQos().length;i++)
		{ 
			switch(i)
			{
			case 0:
				//System.out.println("maxt= "+c.getSeuilmaxT()+" mint= "+c.getSeuilminT());
				tmp=w[i]*((c.getSeuilmaxT()-a.getQos()[i])/(c.getSeuilmaxT()-c.getSeuilminT()));
			break;
			
			case 1: 
				//System.out.println("maxt= "+c.getSeuilmaxD()+" mint= "+c.getSeuilminD());

				tmp=w[i]*((a.getQos()[i]-c.getSeuilminD())/(c.getSeuilmaxD()-c.getSeuilminD()));
		    break;
		    
			case 2 :
				//System.out.println("maxt= "+c.getSeuilmaxR()+" mint= "+c.getSeuilminR());

				tmp=w[i]*((a.getQos()[i]-c.getSeuilminR())/(c.getSeuilmaxR()-c.getSeuilminR()));
			break;
			
			case 3 :
				//System.out.println("maxt= "+c.getSeuilmaxC()+" mint= "+c.getSeuilminC());

				tmp=w[i]*((c.getSeuilmaxC()-a.getQos()[i])/(c.getSeuilmaxC()-c.getSeuilminC()));
			break;
			
			}
		  
		  u=u+tmp;
			 
		}
	// System.out.println("U(a7)= "+u);
		
		return u;
	}
 
	
	
	
	


}
