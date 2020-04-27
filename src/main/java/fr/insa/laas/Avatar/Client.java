package fr.insa.laas.Avatar;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient; 
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;

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
	public Response whenUseHttpAsyncClient_thenCorrect(String url, String originator, String xmlContent) throws Exception {
		Response resp=new Response();
	    CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
	    client.start();
	    HttpPost req = new HttpPost(url);
		 

		 
		req.addHeader(ORIGIN_HEADER, originator);
		req.addHeader(ACCEPT, XML);
		
 		
		
		 
	     
	    Future<HttpResponse> future = client.execute(req, null);
	    HttpResponse response = future.get();
	    resp.setStatusCode(response.getStatusLine().getStatusCode());
	    resp.setRepresentation(response.getEntity().toString());
	    
	    client.close();
	    return resp;
	}
 
	public void whenUseMultipleHttpAsyncClient_thenCorrect(String[] toGet, String originator, String xmlContent[],SelectionManager sm) throws Exception {
	    ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
	    PoolingNHttpClientConnectionManager cm = 
	      new PoolingNHttpClientConnectionManager(ioReactor);
	    CloseableHttpAsyncClient client = 
	      HttpAsyncClients.custom().setConnectionManager(cm).build();
	    client.start();
	     
	  
	    Thread[] threads = new Thread[toGet.length];
	    GetThread[] Gthreads = new GetThread[toGet.length];
	    float time = 0;
	    for (int i = 0; i < threads.length; i++) {
	        HttpPost request = new HttpPost(toGet[i]);
	        request.addHeader(ORIGIN_HEADER, originator);
			request.addHeader(ACCEPT, XML);
			HttpEntity entity = new ByteArrayEntity(xmlContent[i].getBytes("UTF-8"));
			request.setEntity(entity);
			Gthreads[i]=new GetThread(client, request);
	        threads[i] = new Thread(Gthreads[i]);
	    }
	 
	    for (Thread thread : threads) {
	        thread.start();
	     

	    }
	   
	   for (Thread thread : threads)
	    {
	        thread.join();
	    }
	 
	   for (GetThread thread : Gthreads)
	    {
		   sm.getDataFromXML(thread.getValue(),Integer.valueOf(new Util().getXmlElement(thread.getValue(), "id")));
		   if (time<Float.valueOf(new Util().getXmlElement(thread.getValue(), "time")))
			   time=Float.valueOf(new Util().getXmlElement(thread.getValue(), "time"));
		   
		 
	    }
	   
	 
	    
	    sm.setMax(time);
	    System.out.println("max time "+time);
	    client.close();
	    
	}

}
