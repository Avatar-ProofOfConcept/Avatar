package fr.insa.laas.Avatar;

import java.util.ArrayList;

public class LevelResponse {
	public ArrayList<String> ls;
	SelectionManager sm;
	Util u=new Util();
	public LevelResponse (SelectionManager sm)
	{
		ls=new ArrayList<String>();
		this.sm=sm;
	}
 

	   synchronized void add(String str){
			ls.add(str);
			sm.getDataFromXML(str, Integer.valueOf(u.getXmlElement(str, "id")));
			System.out.println( str+ " is deposited");
			sm.show();
		 
	   }

}
