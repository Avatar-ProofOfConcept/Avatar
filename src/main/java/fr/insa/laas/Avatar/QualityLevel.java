package fr.insa.laas.Avatar;

public class QualityLevel 
{

	private ClusterQoS c;
	private int qosId;
	private double valueQoS;
	private int l;
	private double umax;
	private double fmin;
	public ClusterQoS getC() {
		return c;
	}
	public void setC(ClusterQoS c) {
		this.c = c;
	}
	public int getQosId() {
		return qosId;
	}
	public void setQosId(int qosId) {
		this.qosId = qosId;
	}
	public double getValueQoS() {
		return valueQoS;
	}
	public void setValueQoS(double valueQoS) {
		this.valueQoS = valueQoS;
	}
	public QualityLevel(ClusterQoS c,int qos,double value)
	{
		this.c=c;
		this.qosId=qos;
		this.valueQoS=value;
		nbAvatarsSelected();
		
		
	}
	
	public void nbAvatarsSelected()
	{
		int cpt=0;
		double max=c.getUtilities().get(0);
		double min=c.getFluctuations().get(0);
 		for(int i=0; i<c.getAvatars().size();i++)
		{
			 
			switch(qosId)
			{
			case 0://time
				if (c.getAvatars().get(i).getQos()[0]<=valueQoS)
				{
					if(c.getUtilities().get(i)> max) max=c.getUtilities().get(i);
					if(c.getFluctuations().get(i)<min)min=c.getFluctuations().get(i);
					cpt++;
				}
				break;
			case 1: //avail
				if (c.getAvatars().get(i).getQos()[1]>=valueQoS)
				{
					if(c.getUtilities().get(i)> max)max=c.getUtilities().get(i);
					if(c.getFluctuations().get(i)<min)min=c.getFluctuations().get(i);
					cpt++;
				}
				break;
			case 2://reputation
				if (c.getAvatars().get(i).getQos()[2]>=valueQoS)
				{
					if(c.getUtilities().get(i)> max)max=c.getUtilities().get(i);
					if(c.getFluctuations().get(i)<min)min=c.getFluctuations().get(i);
					cpt++;
				}
				break;
			case 3://cost
				if (c.getAvatars().get(i).getQos()[3]<=valueQoS)
				{
					if(c.getUtilities().get(i)> max)max=c.getUtilities().get(i);
					if(c.getFluctuations().get(i)<min)min=c.getFluctuations().get(i);
					cpt++;
				}
				break;
			
			
			}
		
		}
 		this.umax=max;
 		this.fmin=min;
		this.l=cpt;
	}
	public double p()
	{
		
		return (((double)l/(double)this.c.getAvatars().size())*(umax/this.c.maxUtility()));
	}
	public double f()
	{
		System.out.println("fluctuation");
		System.out.println("long"+l);
		System.out.println("fmin"+fmin);
		System.out.println("fmin level"+this.c.minFluctuation());
		System.out.println("cluster size"+this.c.getAvatars().size());
		return (((double)l/(double)this.c.getAvatars().size())*(this.c.minFluctuation()/fmin));
	}
}
