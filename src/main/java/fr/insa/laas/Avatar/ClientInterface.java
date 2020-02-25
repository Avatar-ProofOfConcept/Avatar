package fr.insa.laas.Avatar;

import java.io.IOException;

/**
 * Interface for the client part
 *
 */
public interface ClientInterface {
 

	public Response request(String url, String originator, String content)
			throws IOException;

}
