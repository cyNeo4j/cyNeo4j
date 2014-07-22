package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.extension;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;

public class PassThroughResponseHandler implements ResponseHandler<Object> {

	@Override
	public Object handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		int responseCode = response.getStatusLine().getStatusCode();

		Object retVal = null;

		System.out.println("responseCode: " + responseCode);
		if(responseCode >= 200 && responseCode < 300){

			ObjectMapper mapper = new ObjectMapper();
			retVal = mapper.readValue(response.getEntity().getContent(), Object.class);
			
//			retVal = IOUtils.toString(response.getEntity().getContent());

		} else {
			System.out.println("ERROR " + responseCode);
			ObjectMapper mapper = new ObjectMapper();

			Map<String,String> error = mapper.readValue(response.getEntity().getContent(),Map.class);
			System.out.println(error);
		}

		return retVal;
	}

}
