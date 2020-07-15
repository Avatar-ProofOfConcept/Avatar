package fr.insa.laas.Avatar;

public class Model 
{
	private double[] wights;
	private double intercept;
	
	public Model (double [] w,double i)
	{
		this.wights=w;
		this.intercept=i;
	}

	public double[] getWights() {
		return wights;
	}

	public void setWights(double[] wights) {
		this.wights = wights;
	}

	public double getIntercept() {
		return intercept;
	}

	public void setIntercept(double intercept) {
		this.intercept = intercept;
	}
	

}
