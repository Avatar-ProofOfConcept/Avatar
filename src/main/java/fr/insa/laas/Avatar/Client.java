package fr.insa.laas.Avatar;


import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient; 
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class Client implements ClientInterface {

	private static final String ORIGIN_HEADER = "X-M2M-Origin";
	private static final String CT_HEADER = "Content-Type";
	private static final String ACCEPT = "accept";
	private static final String XML = "application/xml";

 
	public Response request(String url, String originator, String xmlContent) throws IOException {
		Response response = new Response();
		 
		HttpClient client = new DefaultHttpClient();		 
		HttpPost req = new HttpPost(url);
		 

		 
		req.addHeader(ORIGIN_HEADER, originator);
		req.addHeader(ACCEPT, XML);
		
 		
		HttpEntity entity = new ByteArrayEntity(xmlContent.getBytes("UTF-8"));
		req.setEntity(entity);
		 

		
		try {
		 
			HttpResponse reqResp = client.execute(req);
 
			 
			response.setStatusCode(reqResp.getStatusLine().getStatusCode());
			response.setRepresentation(IOUtils.toString(reqResp.getEntity().getContent(), "UTF-8"));
 		 
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			//client.close();
		}
	 
		return response;
	}

}
