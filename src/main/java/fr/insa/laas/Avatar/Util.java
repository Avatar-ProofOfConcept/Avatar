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

}
