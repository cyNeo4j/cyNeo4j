package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;

public class TransCreateIdRetRespHandler implements ResponseHandler<Long> {

	@Override
	public Long handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		int responseCode = response.getStatusLine().getStatusCode();

		Long id = null;

		System.out.println("responseCode: " + responseCode);
		if(responseCode >= 200 && responseCode < 300){

			ObjectMapper mapper = new ObjectMapper();
			Map<String,Object> transactionResponse = mapper.readValue(response.getEntity().getContent(), Map.class);

			List<Object> results = (List<Object>)transactionResponse.get("results");
//			
			Map<String,Object> result = (Map<String,Object>)results.get(0);
			
			List<Map<String,Object>> datas = (List<Map<String,Object>>)result.get("data");
			
			Map<String,Object> data = datas.get(0);
			
			List<Integer> rows = (List<Integer>)data.get("row");
			
			id = new Long(rows.get(0).longValue());
//			
////			List<List<Integer>> queryRes = (List<List<Integer>>)wrapper.get("data");
//			
//			List<Map<String,Object>> result = 
			
//			id = new Long(queryRes.get(0).get(0).longValue());

		} else {
			System.out.println("ERROR " + responseCode);
			ObjectMapper mapper = new ObjectMapper();

			Map<String,String> error = mapper.readValue(response.getEntity().getContent(),Map.class);
			System.out.println(error);
		}

		return id;
	}


}
