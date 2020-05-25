package fr.insa.laas.Avatar;



import java.util.List;


public class ClusterMemberQoS 
{
	private double [] qos;//0 T 1 D 2 R 3 C
	private double [] histD;
	private double [] histT;
	private double [] histR;
	private double CVt;
	private double CVd;
	private double CVr;
	private String Url;

	public double[] getQos() {
		return qos;
	}
	public ClusterMemberQoS (double [] qos)
	{
		this.qos=qos;
	}
	public ClusterMemberQoS (double [] qos,double [] histD,double [] histT,String Url)
	{
		this.qos=qos;
		this.histD=histD;
		this.Url=Url;
		this.histT=histT;
		this.CVt=Util.CV(this,0);
		//System.out.println(CVt);
		this.CVd=Util.CV(this,1);
		//System.out.println(CVd);
		//this.CVr=Util.CV(this,2);
		//
		//System.out.println(CVr);
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public double getCVt() {
		return CVt;
	}
	public void setCVt(double cVt) {
		CVt = cVt;
	}
	public double getCVd() {
		return CVd;
	}
	public void setCVd(double cVd) {
		CVd = cVd;
	}
	public double getCVr() {
		return CVr;
	}
	public void setCVr(double cVr) {
		CVr = cVr;
	}
	public void setQos(double[] qos) 
	{
		this.qos = qos;
	}
	public double[] getHistD() {
		return histD;
	}
	public void setHistD(double[] histD) {
		this.histD = histD;
	}
	public double[] getHistT() {
		return histT;
	}
	public void setHistT(double[] histT) {
		this.histT = histT;
	}
	public double[] getHistR() {
		return histR;
	}
	public void setHistR(double[] histR) 
	{
		this.histR = histR;
	}

	public double[] getHist(int index)
	{
		switch(index)
		{
		case 0 :
			return getHistT();
		case 1 :
			return getHistD();
		/*case 2 :
			return getHistR();*/
		default :
			return null;
		}
		 
	}


	

}
