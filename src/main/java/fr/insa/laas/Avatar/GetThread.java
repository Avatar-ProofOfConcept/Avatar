package fr.insa.laas.Avatar;
 
import java.util.ArrayList;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

public class GetThread implements Runnable {
    private CloseableHttpAsyncClient client;
    private HttpClientContext context;
    private HttpPost request;
    private volatile String value;
  
 
    public GetThread(CloseableHttpAsyncClient client,HttpPost req){
        this.client = client;
        context = HttpClientContext.create();
        this.request = req;
       
       
    }
   
 
    @Override
    public void run() {
        try {
            Future<HttpResponse> future = client.execute(request, context, null);
            HttpResponse response = future.get();
           // System.out.println(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
            value=IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            
            //sm.getDataFromXML(IOUtils.toString(response.getEntity().getContent(), "UTF-8"),Integer.valueOf(u.getXmlElement(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), "id")));
            
            //assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }


	public String getValue() {
		return value;
	}


	 
}