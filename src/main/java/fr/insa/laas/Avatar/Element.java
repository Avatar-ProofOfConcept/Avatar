package fr.insa.laas.Avatar;



public class Element implements Comparable<Element> {
	private int id;
	private float w;
	
	public Element(int i, float d) {
		this.id=i;
		this.w=d;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getW() {
		return w;
	}
	public void setW(float w) {
		this.w = w;
	}
	 
	@Override
	public int compareTo(Element arg0) {
		// TODO Auto-generated method stub
		if (this.w > arg0.w) return 1;
		else {
			if (this.w == arg0.w) return 0;
			else return -1;
		}
		 
	}
	

}

