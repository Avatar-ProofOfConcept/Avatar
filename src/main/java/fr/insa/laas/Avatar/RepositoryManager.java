package fr.insa.laas.Avatar;

import org.eclipse.om2m.commons.resource.Container;
import fr.laas.mooc.helper.http.HTTPGet;
import fr.laas.mooc.helper.http.HTTPPost;
import fr.laas.mooc.helper.om2m.ResourceCreator;
import fr.laas.mooc.helper.om2m.Serializer;

public class RepositoryManager {
	
	
	public void createRepo(String name){
		HTTPPost request = new HTTPPost();
		request.setDestinator("http://localhost:8080/~/in-cse/in-name/");
		request.addHeader("X-M2M-Origin", "admin:admin");
		request.addHeader("Content-Type", "application/xml;ty=3");
		Container cnt = ResourceCreator.createContainer(name);
  		request.setBody(Serializer.toXML(cnt));
		request.send();
		System.out.println(request.send());
	}

}
