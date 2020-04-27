package fr.insa.laas.Avatar;

import java.io.IOException;

/**
 * Interface for the client part
 *
 */
public interface ClientInterface {
 

	public Response request(String url, String originator, String content)
			throws IOException;
	public Response whenUseHttpAsyncClient_thenCorrect(String url, String originator, String content)
			throws Exception;
	public void whenUseMultipleHttpAsyncClient_thenCorrect(String[] toGet, String originator, String xmlContent[],SelectionManager sm) throws Exception; 

}
