package fr.insa.laas.Avatar;

import java.util.ArrayList;

public class RequestTask 
{
	
	private ArrayList<String>listeMemeber=new ArrayList<String>();
    private SocialNetwork sns;
    private int TTL;
	public SocialNetwork getSns() {
		return sns;
	}

	public void setSns(SocialNetwork sns) {
		this.sns = sns;
	}

	public int getTTL() {
		return TTL;
	}

	public void setTTL(int tTL) {
		TTL = tTL;
	}

	public ArrayList<String> getListeMemeber() {
		return listeMemeber;
	}

	public void setListeMemeber(ArrayList<String> listeMemeber) {
		this.listeMemeber = listeMemeber;
	}
	

}
